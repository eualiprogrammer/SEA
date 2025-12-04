package com.example.sea.business;

import com.example.sea.data.*;
import com.example.sea.model.*;
import com.example.sea.exceptions.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.example.sea.util.ServicoEmail;

public class ControladorInscricao implements IControladorInscricao {

    private IRepositorioInscricao repositorioInscricao;

    public ControladorInscricao() {
        this.repositorioInscricao = new RepositorioInscricao();
    }

    private void validarConflitoHorario(Participante participante, Palestra novaPalestra) throws ConflitoHorarioException {
        LocalDateTime inicioNova = novaPalestra.getDataHoraInicio();
        long minutosDuracao = (long) (novaPalestra.getDuracaoHoras() * 60);
        LocalDateTime fimNova = inicioNova.plusMinutes(minutosDuracao);

        List<Inscricao> inscricoesExistentes = this.repositorioInscricao.listarPorParticipante(participante);

        for (Inscricao inscricao : inscricoesExistentes) {
            if (inscricao.getAtividade() instanceof Palestra) {
                Palestra palestraExistente = (Palestra) inscricao.getAtividade();
                LocalDateTime inicioExistente = palestraExistente.getDataHoraInicio();
                long minutosExistente = (long) (palestraExistente.getDuracaoHoras() * 60);
                LocalDateTime fimExistente = inicioExistente.plusMinutes(minutosExistente);

                if (inicioNova.isBefore(fimExistente) && fimNova.isAfter(inicioExistente)) {
                    throw new ConflitoHorarioException(palestraExistente.getTitulo(), novaPalestra.getTitulo());
                }
            }
        }
    }

    @Override
    public void inscrever(Participante participante, Atividade atividade) throws Exception {
        if (participante == null) throw new ParticipanteNaoEncontradoException("Participante nulo");
        if (atividade == null) throw new IllegalArgumentException("Atividade nula");

        if (atividade instanceof Palestra) {
            Palestra palestra = (Palestra) atividade;
            int numeroInscritos = this.repositorioInscricao.listarPorPalestra(palestra).size();
            int capacidadeSala = palestra.getSala().getCapacidade();

            if (numeroInscritos >= capacidadeSala) {
                throw new LotacaoExcedidaException(palestra.getTitulo());
            }
            this.validarConflitoHorario(participante, palestra);
        }


        if (atividade instanceof Workshop) {
            Workshop workshop = (Workshop) atividade;
            for (Palestra palestraDoPacote : workshop.getPalestrasDoWorkshop()) {
                try {
                    this.inscrever(participante, palestraDoPacote);
                } catch (InscricaoJaExisteException e) {
                    System.out.println("Já inscrito na palestra: " + palestraDoPacote.getTitulo());
                }
            }
        }

        Inscricao novaInscricao = new Inscricao(participante, atividade);
        this.repositorioInscricao.salvar(novaInscricao);

        try {
            String titulo = "Inscrição Confirmada";
            String msg = "Sua vaga na atividade '" + atividade.getTitulo() + "' está garantida.";

            SistemaSGA.getInstance().getControladorNotificacao()
                    .enviarNotificacao(participante, titulo, msg);

        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação: " + e.getMessage());
        }

        try {
            new Thread(() -> {
                String nomeAtividade = atividade.getTitulo();

                ServicoEmail.enviarEmailConfirmacao(
                        participante.getEmail(),
                        participante.getNome(),
                        nomeAtividade
                );
            }).start();
        } catch (Exception e) {
            System.err.println("Falha ao tentar enviar e-mail: " + e.getMessage());
        }
    }

    @Override
    public void inscrever(Participante p, Palestra pl) throws Exception {
        this.inscrever(p, (Atividade) pl);
    }

    @Override
    public void cancelarInscricao(Inscricao inscricao) throws InscricaoNaoEncontradaException {
        if (inscricao == null) throw new InscricaoNaoEncontradaException();
        this.repositorioInscricao.deletar(inscricao);
    }

    @Override
    public void marcarPresenca(Inscricao inscricao) throws InscricaoNaoEncontradaException {
        if (inscricao == null) throw new InscricaoNaoEncontradaException();

        inscricao.confirmarPresenca();
        this.repositorioInscricao.atualizar(inscricao);

        try {
            Certificado novoCertificado = new Certificado(inscricao);
            SistemaSGA.getInstance().getControladorCertificado().cadastrar(novoCertificado);

            inscricao.setCertificado(novoCertificado);
            this.repositorioInscricao.atualizar(inscricao);

            SistemaSGA.getInstance().getControladorNotificacao().enviarNotificacao(
                    inscricao.getParticipante(),
                    "🎓 Certificado Disponível",
                    "Sua presença em '" + inscricao.getAtividade().getTitulo() + "' foi confirmada! O certificado já está disponível."
            );

            System.out.println("Presença confirmada e Certificado gerado!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Certificado gerarCertificado(Inscricao inscricao) throws Exception {
        if (inscricao == null) throw new InscricaoNaoEncontradaException();
        if (!inscricao.isPresenca()) {
            throw new CertificadoSemPresencaException(inscricao.getParticipante().getNome(), inscricao.getAtividade().getTitulo());
        }

        Certificado novoCertificado = new Certificado(inscricao);
        SistemaSGA.getInstance().getControladorCertificado().cadastrar(novoCertificado);

        return novoCertificado;
    }

    @Override public List<Inscricao> listarPorParticipante(Participante p) { return this.repositorioInscricao.listarPorParticipante(p); }
    @Override public List<Inscricao> listarPorPalestra(Palestra p) { return this.repositorioInscricao.listarPorPalestra(p); }
    @Override public List<Inscricao> listarPorAtividade(Atividade a) { return this.repositorioInscricao.listarPorAtividade(a); }
    @Override public List<Inscricao> listarTodos() { return this.repositorioInscricao.listarTodas(); }
    @Override public void atualizar(Inscricao i) throws Exception { this.repositorioInscricao.atualizar(i); }
    @Override public void cadastrar(Inscricao i) throws Exception { this.repositorioInscricao.salvar(i); }
}
