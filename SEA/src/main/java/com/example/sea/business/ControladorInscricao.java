package com.example.sea.business;

import com.example.sea.data.*;
import com.example.sea.model.*;
import com.example.sea.exceptions.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

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

            if (workshop.getPalestrasDoWorkshop().isEmpty()) {
            }

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
    }

    @Override
    public void inscrever(Participante participante, Palestra palestra) throws Exception {
        this.inscrever(participante, (Atividade) palestra);
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

    @Override
    public List<Inscricao> listarPorParticipante(Participante participante) {
        if (participante == null) return new ArrayList<>();
        return this.repositorioInscricao.listarPorParticipante(participante);
    }

    @Override
    public List<Inscricao> listarPorPalestra(Palestra palestra) {
        if (palestra == null) return new ArrayList<>();
        return this.repositorioInscricao.listarPorPalestra(palestra);
    }

    @Override
    public List<Inscricao> listarTodos() {
        return this.repositorioInscricao.listarTodas();
    }

    @Override
    public void atualizar(Inscricao inscricao) throws Exception {
        if (inscricao == null) throw new IllegalArgumentException("Inscrição inválida");
        this.repositorioInscricao.atualizar(inscricao);
    }

    @Override
    public void cadastrar(Inscricao inscricao) throws Exception {
        this.repositorioInscricao.salvar(inscricao);
    }

    @Override
    public List<Inscricao> listarPorAtividade(Atividade atividade) {
        return this.repositorioInscricao.listarPorAtividade(atividade);
    }
}