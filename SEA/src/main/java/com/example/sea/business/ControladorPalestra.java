package com.example.sea.business;

import com.example.sea.data.IRepositorioPalestra;
import com.example.sea.data.RepositorioPalestra;
import com.example.sea.exceptions.*;
import com.example.sea.model.Evento;
import com.example.sea.model.Inscricao;
import com.example.sea.model.Palestra;
import com.example.sea.model.Palestrante;
import com.example.sea.model.Sala;

import java.time.LocalDateTime;
import java.util.List;

public class ControladorPalestra implements IControladorPalestra {

    private IRepositorioPalestra repositorioPalestra;

    public ControladorPalestra() {
        this.repositorioPalestra = new RepositorioPalestra();
    }

    private void validarDataDentroDoEvento(LocalDateTime dataPalestra, Evento evento) throws DataInvalidaException {
        if (evento == null || evento.getDataInicio() == null) return;
        LocalDateTime inicioEvento = evento.getDataInicio().atStartOfDay();
        LocalDateTime fimEvento = evento.getDataFim().atTime(23, 59, 59);
        if (dataPalestra.isBefore(inicioEvento) || dataPalestra.isAfter(fimEvento)) {
            throw new DataInvalidaException("Data da Palestra", "Fora do período do evento.");
        }
    }

    private void validarConflitos(Palestra novaPalestra) throws ConflitoHorarioException {
        LocalDateTime inicioNova = novaPalestra.getDataHoraInicio();
        long minutosNova = (long) (novaPalestra.getDuracaoHoras() * 60);
        LocalDateTime fimNova = inicioNova.plusMinutes(minutosNova);

        for (Palestra existente : this.repositorioPalestra.listarTodas()) {
            if (existente.getTitulo().equals(novaPalestra.getTitulo())) continue;

            if (existente.getSala().getNome().equals(novaPalestra.getSala().getNome())) {
                LocalDateTime inicioExistente = existente.getDataHoraInicio();
                long minutosExistente = (long) (existente.getDuracaoHoras() * 60);
                LocalDateTime fimExistente = inicioExistente.plusMinutes(minutosExistente);
                if (inicioNova.isBefore(fimExistente) && fimNova.isAfter(inicioExistente)) {
                    throw new ConflitoHorarioException("A Sala '" + novaPalestra.getSala().getNome() +
                            "' já está em uso pela palestra: " + existente.getTitulo());
                }
            }

            if (existente.getPalestrante().getEmail().equals(novaPalestra.getPalestrante().getEmail())) {
                LocalDateTime inicioExistente = existente.getDataHoraInicio();
                long minutosExistente = (long) (existente.getDuracaoHoras() * 60);
                LocalDateTime fimExistente = inicioExistente.plusMinutes(minutosExistente);
                if (inicioNova.isBefore(fimExistente) && fimNova.isAfter(inicioExistente)) {
                    throw new ConflitoHorarioException("O palestrante '" + novaPalestra.getPalestrante().getNome() +
                            "' já está ocupado neste horário.");
                }
            }
        }
    }

    @Override
    public void cadastrar(String titulo, String descricao, Evento evento,
                          LocalDateTime dataHoraInicio, float duracaoHoras,
                          Sala sala, Palestrante palestrante)
            throws PalestraJaExisteException, CampoVazioException, DataInvalidaException, ConflitoHorarioException {

        if (titulo == null || titulo.trim().isEmpty()) throw new CampoVazioException("Título");
        if (evento == null) throw new IllegalArgumentException("Evento nulo");
        if (dataHoraInicio == null) throw new CampoVazioException("Data/Hora");
        if (sala == null) throw new IllegalArgumentException("Sala nula");
        if (palestrante == null) throw new IllegalArgumentException("Palestrante nulo");
        if (duracaoHoras <= 0) throw new DataInvalidaException("Duração", "Deve ser positiva.");

        validarDataDentroDoEvento(dataHoraInicio, evento);

        Palestra novaPalestra = new Palestra(titulo, descricao, evento, dataHoraInicio, duracaoHoras, sala, palestrante);

        evento.adicionarAtividade(novaPalestra);
        try { SistemaSGA.getInstance().getControladorEvento().atualizar(evento); } catch (Exception e) {}

        validarConflitos(novaPalestra);
        this.repositorioPalestra.salvar(novaPalestra);
        
        try {
            new Thread(() -> {
                ServicoEmail.enviarEmailConfirmacaoPalestrante(
                        palestrante.getEmail(),
                        palestrante.getNome(),
                        titulo,
                        dataHoraInicio
                );
            }).start();
        } catch (Exception e) {
            System.err.println("Falha ao enviar e-mail para o palestrante: " + e.getMessage());
        }

    }

    @Override
    public void atualizar(Palestra palestra)
            throws PalestraNaoEncontradaException, CampoVazioException, DataInvalidaException, ConflitoHorarioException {

        if (palestra == null) throw new IllegalArgumentException("Palestra nula");

        validarDataDentroDoEvento(palestra.getDataHoraInicio(), palestra.getEvento());
        validarConflitos(palestra);

        this.repositorioPalestra.atualizar(palestra);

        try {
            List<Inscricao> inscritos = SistemaSGA.getInstance()
                    .getControladorInscricao()
                    .listarPorPalestra(palestra);

            System.out.println("Enviando notificação para " + inscritos.size() + " inscritos.");

            for (Inscricao inscricao : inscritos) {
                SistemaSGA.getInstance().getControladorNotificacao().enviarNotificacao(
                        inscricao.getParticipante(),
                        " Alteração: " + palestra.getTitulo(),
                        "Houve uma mudança de horário ou sala nesta palestra. Verifique os novos detalhes."
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remover(String titulo) throws PalestraNaoEncontradaException, CampoVazioException {
        if (titulo == null || titulo.trim().isEmpty()) throw new CampoVazioException("Título");

        Palestra palestra = this.repositorioPalestra.buscarPorTitulo(titulo);

        List<Inscricao> inscritos = SistemaSGA.getInstance()
                .getControladorInscricao()
                .listarPorPalestra(palestra);

        if (!inscritos.isEmpty()) {
            for (Inscricao inscricao : inscritos) {
                SistemaSGA.getInstance().getControladorNotificacao().enviarNotificacao(
                        inscricao.getParticipante(),
                        "❌ Cancelado: " + palestra.getTitulo(),
                        "A palestra '" + palestra.getTitulo() + "' foi cancelada. Sua inscrição foi removida."
                );
                try {
                    SistemaSGA.getInstance().getControladorInscricao().cancelarInscricao(inscricao);
                } catch (Exception e) {}
            }
        }

        this.repositorioPalestra.deletar(titulo);
    }

    @Override public Palestra buscar(String titulo) throws PalestraNaoEncontradaException, CampoVazioException {
        if (titulo == null || titulo.trim().isEmpty()) throw new CampoVazioException("Título");
        return this.repositorioPalestra.buscarPorTitulo(titulo);
    }

    @Override public List<Palestra> listarTodos() { return this.repositorioPalestra.listarTodas(); }
    @Override public List<Palestra> listar() { return listarTodos(); }
    @Override public List<Palestra> listarPorEvento(Evento e) { return this.repositorioPalestra.listarPorEvento(e); }
}
