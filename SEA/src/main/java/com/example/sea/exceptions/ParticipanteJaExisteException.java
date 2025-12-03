package com.example.sea.exceptions;


public class ParticipanteJaExisteException extends Exception {

    public ParticipanteJaExisteException(String identificador) {
        super("Já existe um participante cadastrado com o identificador (email/CPF): " + identificador);
    }
}