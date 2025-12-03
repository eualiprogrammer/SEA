package com.example.sea.exceptions;

public class WorkshopNaoEncontradoException extends Exception {
    public WorkshopNaoEncontradoException(String titulo) {
        super("Nenhum workshop foi encontrado com o título: " + titulo);
    }
}