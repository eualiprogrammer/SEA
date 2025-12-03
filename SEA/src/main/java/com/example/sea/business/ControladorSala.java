package com.example.sea.business;

import com.example.sea.data.IRepositorioSala;
import com.example.sea.data.RepositorioSala;
import com.example.sea.model.Sala;
import com.example.sea.exceptions.CampoVazioException;
import com.example.sea.exceptions.SalaJaExisteException;
import com.example.sea.exceptions.SalaNaoEncontradaException;

import java.util.List;

public class ControladorSala implements IControladorSala {

    private IRepositorioSala repositorioSala;

    public ControladorSala() {
        this.repositorioSala = new RepositorioSala();
    }

    @Override
    public void cadastrar(Sala sala) throws SalaJaExisteException, CampoVazioException, Exception {
        if (sala == null) {
            throw new IllegalArgumentException("O objeto 'Sala' não pode ser nulo.");
        }
        if (sala.getNome() == null || sala.getNome().trim().isEmpty()) {
            throw new CampoVazioException("Nome");
        }
        if (sala.getCapacidade() <= 0) {
            throw new Exception("A capacidade da sala deve ser maior que zero.");
        }
        this.repositorioSala.salvar(sala);
    }

    @Override
    public Sala buscar(String nome) throws SalaNaoEncontradaException, CampoVazioException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new CampoVazioException("Nome");
        }
        return this.repositorioSala.buscarPorNome(nome);
    }

    @Override
    public List<Sala> listar() {
        return this.repositorioSala.listarTodas();
    }

    @Override
    public void atualizar(Sala sala) throws SalaNaoEncontradaException, CampoVazioException, Exception {
        if (sala == null) {
            throw new IllegalArgumentException("O objeto 'Sala' não pode ser nulo.");
        }
        if (sala.getNome() == null || sala.getNome().trim().isEmpty()) {
            throw new CampoVazioException("Nome");
        }
        if (sala.getCapacidade() <= 0) {
            throw new Exception("A capacidade da sala deve ser maior que zero.");
        }
        this.repositorioSala.atualizar(sala);
    }

    @Override
    public void remover(String nome) throws SalaNaoEncontradaException, CampoVazioException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new CampoVazioException("Nome");
        }
        this.repositorioSala.deletar(nome);
    }
}
