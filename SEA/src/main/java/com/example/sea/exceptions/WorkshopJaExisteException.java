package com.example.sea.exceptions;

public class WorkshopJaExisteException extends Exception {
    public WorkshopJaExisteException(String titulo) {
        super("Já existe um workshop cadastrado com o título: " + titulo);
    }
}