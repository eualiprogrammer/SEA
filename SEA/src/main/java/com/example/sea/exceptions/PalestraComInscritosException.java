package com.example.sea.exceptions;


public class PalestraComInscritosException extends Exception {
    
    public PalestraComInscritosException(String tituloPalestra, int numeroInscritos) {
        super("Não é possível remover a palestra '" + tituloPalestra + 
              "'. Ela já possui " + numeroInscritos + " participante(s) inscrito(s).");
    }
}