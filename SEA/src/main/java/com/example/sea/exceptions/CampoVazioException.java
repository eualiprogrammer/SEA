package com.example.sea.exceptions;

public class CampoVazioException extends Exception {
    
    public CampoVazioException(String nomeDoCampo) {
        super("O campo '" + nomeDoCampo + "' é obrigatório e não pode estar vazio.");
    }
}