package com.example.sea.exceptions;

public class PalestranteNaoEncontradoException extends Exception {
    public PalestranteNaoEncontradoException(String identificador) {
        super("Nenhum palestrante foi encontrado com o identificador: " + identificador);
    }
}