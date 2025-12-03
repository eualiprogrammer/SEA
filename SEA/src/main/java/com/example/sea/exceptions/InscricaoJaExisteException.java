package com.example.sea.exceptions;


public class InscricaoJaExisteException extends Exception {

    public InscricaoJaExisteException(String participante, String palestra) {
        super("O participante '" + participante + "' já está inscrito na palestra '" + palestra + "'.");
    }
}