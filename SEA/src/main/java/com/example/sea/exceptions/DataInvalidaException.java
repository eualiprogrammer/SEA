package com.example.sea.exceptions;

public class DataInvalidaException extends Exception {
    
    public DataInvalidaException(String mensagem) {
        super(mensagem);
    }

    public DataInvalidaException(String tipoData, String mensagem) {
        super("Erro na " + tipoData + ": " + mensagem);
    }
}