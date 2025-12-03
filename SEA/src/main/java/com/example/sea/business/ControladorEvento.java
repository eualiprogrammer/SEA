package com.example.sea.business;

import com.example.sea.data.IRepositorioEvento;
import com.example.sea.data.RepositorioEvento;
import com.example.sea.exceptions.CampoVazioException;
import com.example.sea.exceptions.DataInvalidaException;
import com.example.sea.exceptions.EventoJaExisteException;
import com.example.sea.exceptions.EventoNaoEncontradoException;
import com.example.sea.model.Evento;

import java.util.List;

public class ControladorEvento implements IControladorEvento {

    private IRepositorioEvento repositorioEvento;

    public ControladorEvento() {
        this.repositorioEvento = new RepositorioEvento();
    }

    private void validarEvento(Evento evento) throws CampoVazioException, DataInvalidaException {
        if (evento == null) {
            throw new IllegalArgumentException("O objeto 'Evento' não pode ser nulo.");
        }
        if (evento.getNome() == null || evento.getNome().trim().isEmpty()) {
            throw new CampoVazioException("Nome");
        }
        if (evento.getDataInicio() == null) {
            throw new CampoVazioException("Data de Início");
        }
        if (evento.getDataFim() == null) {
            throw new CampoVazioException("Data de Fim");
        }
        if (evento.getDataInicio().isAfter(evento.getDataFim())) {
            throw new DataInvalidaException("A data de início não pode ser posterior à data de fim.");
        }
    }

    @Override
    public void cadastrar(Evento evento) throws EventoJaExisteException, CampoVazioException, DataInvalidaException {
        this.validarEvento(evento);
        this.repositorioEvento.salvar(evento);
    }

    @Override
    public Evento buscar(String nome) throws EventoNaoEncontradoException, CampoVazioException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new CampoVazioException("Nome");
        }
        return this.repositorioEvento.buscarPorNome(nome);
    }

    @Override
    public List<Evento> listar() {
        return this.repositorioEvento.listarTodos();
    }

    @Override
    public void atualizar(Evento evento) throws EventoNaoEncontradoException, CampoVazioException, DataInvalidaException {
        this.validarEvento(evento);
        this.repositorioEvento.atualizar(evento);
    }

    @Override
    public void remover(String nome) throws EventoNaoEncontradoException, CampoVazioException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new CampoVazioException("Nome");
        }
        this.repositorioEvento.deletar(nome);
    }
}
