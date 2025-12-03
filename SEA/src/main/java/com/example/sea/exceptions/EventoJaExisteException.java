package com.example.sea.exceptions;


public class EventoJaExisteException extends Exception {

    public EventoJaExisteException(String nomeDoEvento) {
        super("Já existe um evento cadastrado com o nome: " + nomeDoEvento);
    }
}