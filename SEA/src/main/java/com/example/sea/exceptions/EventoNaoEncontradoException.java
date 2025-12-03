package com.example.sea.exceptions;

public class EventoNaoEncontradoException extends Exception {

    public EventoNaoEncontradoException(String nomeDoEvento) {
        super("Nenhum evento foi encontrado com o nome: " + nomeDoEvento);
    }
}