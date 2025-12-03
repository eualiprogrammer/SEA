package com.example.sea.exceptions;

public class SalaJaExisteException extends Exception {

    public SalaJaExisteException(String nomeDaSala) {
        super("Já existe uma sala cadastrada com o nome: " + nomeDaSala);
    }
}