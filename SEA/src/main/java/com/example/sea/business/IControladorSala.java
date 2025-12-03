package com.example.sea.business;

import com.example.sea.model.Sala;
import com.example.sea.exceptions.SalaJaExisteException;
import com.example.sea.exceptions.SalaNaoEncontradaException;
import com.example.sea.exceptions.CampoVazioException;
import java.util.List;

public interface IControladorSala {

    void cadastrar(Sala sala) throws SalaJaExisteException, CampoVazioException, Exception;

    Sala buscar(String nome) throws SalaNaoEncontradaException, CampoVazioException;

    List<Sala> listar();

    void atualizar(Sala sala) throws SalaNaoEncontradaException, CampoVazioException, Exception;

    void remover(String nome) throws SalaNaoEncontradaException, CampoVazioException;
}