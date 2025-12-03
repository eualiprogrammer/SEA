package com.example.sea.data;

import com.example.sea.exceptions.SalaJaExisteException;
import com.example.sea.exceptions.SalaNaoEncontradaException;
import com.example.sea.model.Sala;

import java.util.List;

public interface IRepositorioSala {

    void salvar(Sala sala) throws SalaJaExisteException;

    Sala buscarPorNome(String nome) throws SalaNaoEncontradaException;

    List<Sala> listarTodas();

    void atualizar(Sala sala) throws SalaNaoEncontradaException;

    void deletar(String nome) throws SalaNaoEncontradaException;
}
