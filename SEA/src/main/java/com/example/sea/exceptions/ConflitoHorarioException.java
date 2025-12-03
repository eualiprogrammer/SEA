
package com.example.sea.exceptions;


public class ConflitoHorarioException extends Exception {

    
    public ConflitoHorarioException(String mensagem) {
        super(mensagem);
    }

    public ConflitoHorarioException(String palestraExistente, String novaPalestra) {
        super("Conflito de Horário: A inscrição na palestra '" + novaPalestra + 
              "' conflita com a palestra '" + palestraExistente + "' em que já está inscrito.");
    }
}