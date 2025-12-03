package com.example.sea.business;

import com.example.sea.exceptions.CampoVazioException;
import com.example.sea.exceptions.DataInvalidaException;
import com.example.sea.exceptions.EventoJaExisteException;
import com.example.sea.exceptions.EventoNaoEncontradoException;
import com.example.sea.model.Evento;

import java.util.List;

public interface IControladorEvento {
    void cadastrar(Evento evento) throws EventoJaExisteException, CampoVazioException, DataInvalidaException;

    Evento buscar(String nome) throws EventoNaoEncontradoException, CampoVazioException;

    List<Evento> listar();

    void atualizar(Evento evento) throws EventoNaoEncontradoException, CampoVazioException, DataInvalidaException;
   
    void remover(String nome) throws EventoNaoEncontradoException, CampoVazioException;
}