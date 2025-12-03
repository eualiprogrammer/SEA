package com.example.sea.exceptions;


public class SalaNaoEncontradaException extends Exception {

    public SalaNaoEncontradaException(String nomeDaSala) {
        super("Nenhuma sala foi encontrada com o nome: " + nomeDaSala);
    }
}