package com.example.sea.exceptions;

public class LotacaoExcedidaException extends Exception {

    public LotacaoExcedidaException(String nomePalestra) {
        super("Não foi possível realizar a inscrição: As vagas para a palestra '" + nomePalestra + "' estão esgotadas.");
    }
}