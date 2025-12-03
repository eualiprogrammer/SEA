package com.example.sea.exceptions;


public class ParticipanteNaoEncontradoException extends Exception {

    public ParticipanteNaoEncontradoException(String identificador) {
        super("Nenhum participante foi encontrado com o identificador: " + identificador);
    }
}