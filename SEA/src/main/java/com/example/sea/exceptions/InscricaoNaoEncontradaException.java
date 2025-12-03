package com.example.sea.exceptions;

 
public class InscricaoNaoEncontradaException extends Exception {

    public InscricaoNaoEncontradaException() {
        super("A inscrição procurada não foi encontrada.");
    }
}