package com.example.sea.exceptions;


public class PalestraNaoEncontradaException extends Exception {
    
    public PalestraNaoEncontradaException(String identificador) {
        super("Nenhuma palestra foi encontrada com o identificador: " + identificador);
    }
}