package com.example.sea.business;

import com.example.sea.model.*; 
import com.example.sea.exceptions.*; 
import java.time.LocalDateTime;
import java.util.List;

public interface IControladorPalestra {

  
    void cadastrar(String titulo, String descricao, Evento evento, 
                   LocalDateTime dataHoraInicio, float duracaoHoras, 
                   Sala sala, Palestrante palestrante) 
                   throws PalestraJaExisteException, CampoVazioException, DataInvalidaException, ConflitoHorarioException;

    Palestra buscar(String titulo) throws PalestraNaoEncontradaException, CampoVazioException;

    List<Palestra> listar();
    
    List<Palestra> listarPorEvento(Evento evento);
   
    void atualizar(Palestra palestra) 
        throws PalestraNaoEncontradaException, CampoVazioException, DataInvalidaException, ConflitoHorarioException;

    void remover(String titulo) throws PalestraNaoEncontradaException, CampoVazioException, PalestraComInscritosException;

    List<Palestra> listarTodos();
}