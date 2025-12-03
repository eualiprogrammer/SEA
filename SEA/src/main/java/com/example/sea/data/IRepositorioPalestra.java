package com.example.sea.data;

import com.example.sea.model.Palestra;
import com.example.sea.model.Evento;
import com.example.sea.exceptions.PalestraJaExisteException;
import com.example.sea.exceptions.PalestraNaoEncontradaException;
import java.util.List;

public interface IRepositorioPalestra {

    void salvar(Palestra palestra) throws PalestraJaExisteException;

    Palestra buscarPorTitulo(String titulo) throws PalestraNaoEncontradaException;

    List<Palestra> listarTodas();
    
    List<Palestra> listarPorEvento(Evento evento);

    void atualizar(Palestra palestra) throws PalestraNaoEncontradaException;

    void deletar(String titulo) throws PalestraNaoEncontradaException;
}
