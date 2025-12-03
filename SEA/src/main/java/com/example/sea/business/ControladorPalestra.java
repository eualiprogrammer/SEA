package com.example.sea.business;

import com.example.sea.data.IRepositorioInscricao;
import com.example.sea.data.IRepositorioPalestra;
import com.example.sea.data.RepositorioInscricao;
import com.example.sea.data.RepositorioPalestra;
import com.example.sea.exceptions.*;
import com.example.sea.model.Evento;
import com.example.sea.model.Inscricao;
import com.example.sea.model.Palestra;
import com.example.sea.model.Palestrante;
import com.example.sea.model.Sala;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ControladorPalestra implements IControladorPalestra {

    private IRepositorioPalestra repositorioPalestra;
    private IRepositorioInscricao repositorioInscricao;

    public ControladorPalestra() {
        this.repositorioPalestra = new RepositorioPalestra();
        this.repositorioInscricao = new RepositorioInscricao();
    }

    private void validarDataDentroDoEvento(LocalDateTime dataPalestra, Evento evento) throws DataInvalidaException {
        if (evento == null || evento.getDataInicio() == null || evento.getDataFim() == null) {
            return;
        }

        LocalDateTime inicioEvento = evento.getDataInicio().atStartOfDay();
        LocalDateTime fimEvento = evento.getDataFim().atTime(23, 59, 59);

        if (dataPalestra.isBefore(inicioEvento) || dataPalestra.isAfter(fimEvento)) {
            throw new DataInvalidaException(
                    "Data da Palestra",
                    "A palestra deve ocorrer entre " + evento.getDataInicio() + " e " + evento.getDataFim()
            );
        }
    }

    private void validarConflitos(Palestra novaPalestra) throws ConflitoHorarioException {
        LocalDateTime inicioNova = novaPalestra.getDataHoraInicio();
        long minutosNova = (long) (novaPalestra.getDuracaoHoras() * 60);
        LocalDateTime fimNova = inicioNova.plusMinutes(minutosNova);

        for (Palestra existente : this.repositorioPalestra.listarTodas()) {
            if (existente.getTitulo().equals(novaPalestra.getTitulo())) continue;

            LocalDateTime inicioExistente = existente.getDataHoraInicio();
            long minutosExistente = (long) (existente.getDuracaoHoras() * 60);
            LocalDateTime fimExistente = inicioExistente.plusMinutes(minutosExistente);

            boolean conflitoHorario = inicioNova.isBefore(fimExistente) && fimNova.isAfter(inicioExistente);

            if (conflitoHorario) {
                if (existente.getSala().getNome().equals(novaPalestra.getSala().getNome())) {
                    throw new ConflitoHorarioException(
                            "A Sala " + novaPalestra.getSala().getNome() + " já está ocupada por: " + existente.getTitulo()
                    );
                }
                if (existente.getPalestrante().getEmail().equals(novaPalestra.getPalestrante().getEmail())) {
                    throw new ConflitoHorarioException(
                            "O palestrante " + novaPalestra.getPalestrante().getNome() + " já tem compromisso na palestra: " + existente.getTitulo()
                    );
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
        if (evento == null) throw new IllegalArgumentException("Selecione um Evento");
        if (sala == null) throw new IllegalArgumentException("Selecione uma Sala");
        if (palestrante == null) throw new IllegalArgumentException("Selecione um Palestrante");
        if (dataHoraInicio == null) throw new CampoVazioException("Data e Hora");
        if (duracaoHoras <= 0) throw new DataInvalidaException("Duração", "A duração deve ser positiva.");

        validarDataDentroDoEvento(dataHoraInicio, evento);

        Palestra novaPalestra = new Palestra(titulo, descricao, evento, dataHoraInicio, duracaoHoras, sala, palestrante);

        validarConflitos(novaPalestra);

        evento.adicionarAtividade(novaPalestra);

        try {
            SistemaSGA.getInstance().getControladorEvento().atualizar(evento);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.repositorioPalestra.salvar(novaPalestra);
    }

    @Override
    public void atualizar(Palestra palestra)
            throws PalestraNaoEncontradaException, CampoVazioException, DataInvalidaException, ConflitoHorarioException {

        if (palestra == null) throw new IllegalArgumentException("Palestra nula");

        validarDataDentroDoEvento(palestra.getDataHoraInicio(), palestra.getEvento());
        validarConflitos(palestra);

        this.repositorioPalestra.atualizar(palestra);
    }

    @Override
    public void remover(String titulo) throws PalestraNaoEncontradaException, CampoVazioException, PalestraComInscritosException {
        if (titulo == null || titulo.trim().isEmpty()) throw new CampoVazioException("Título");

        Palestra palestra = this.repositorioPalestra.buscarPorTitulo(titulo);

        List<Inscricao> inscritos = this.repositorioInscricao.listarPorPalestra(palestra);
        if (!inscritos.isEmpty()) {
            throw new PalestraComInscritosException(titulo, inscritos.size());
        }

        this.repositorioPalestra.deletar(titulo);
    }

    @Override
    public Palestra buscar(String titulo) throws PalestraNaoEncontradaException, CampoVazioException {
        if (titulo == null || titulo.trim().isEmpty()) throw new CampoVazioException("Título");
        return this.repositorioPalestra.buscarPorTitulo(titulo);
    }

    @Override
    public List<Palestra> listarTodos() {
        return this.repositorioPalestra.listarTodas();
    }

    @Override
    public List<Palestra> listar() {
        return this.listarTodos();
    }

    @Override
    public List<Palestra> listarPorEvento(Evento evento) {
        if (evento == null) return new ArrayList<>();
        return this.repositorioPalestra.listarPorEvento(evento);
    }
}