package com.example.sea.exceptions;


public class CertificadoNaoEncontradoException extends Exception {
    
    public CertificadoNaoEncontradoException(String codigo) {
        super("Certificado não encontrado com o código: " + codigo);
    }
}