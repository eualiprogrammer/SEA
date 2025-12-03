package com.example.sea.exceptions;

public class PalestraJaExisteException extends Exception {
    public PalestraJaExisteException(String titulo) {
        super("Já existe uma palestra cadastrada com o título: " + titulo);
    }
}