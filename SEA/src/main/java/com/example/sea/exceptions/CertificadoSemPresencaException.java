package com.example.sea.exceptions;


public class CertificadoSemPresencaException extends Exception {

    public CertificadoSemPresencaException(String nomeParticipante, String nomePalestra) {
        super("Não é possível gerar certificado: O participante '" + nomeParticipante + 
              "' não teve a presença confirmada na palestra '" + nomePalestra + "'.");
    }
}