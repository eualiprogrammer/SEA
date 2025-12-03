package com.example.sea.exceptions;


public class PalestranteJaExisteException extends Exception {
    public PalestranteJaExisteException(String email) {
        super("Já existe um palestrante cadastrado com o email: " + email);
    }
}