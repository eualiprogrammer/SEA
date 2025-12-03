package com.example.sea.data;

import com.example.sea.model.Evento;
import com.example.sea.exceptions.EventoJaExisteException;
import com.example.sea.exceptions.EventoNaoEncontradoException;
import java.util.List;

public interface IRepositorioEvento {

    void salvar(Evento evento) throws EventoJaExisteException;

    Evento buscarPorNome(String nome) throws EventoNaoEncontradoException;

    List<Evento> listarTodos();

    void atualizar(Evento evento) throws EventoNaoEncontradoException;

    void deletar(String nome) throws EventoNaoEncontradoException;
}
