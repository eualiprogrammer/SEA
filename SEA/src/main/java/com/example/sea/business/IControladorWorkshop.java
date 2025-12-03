package com.example.sea.business;

import com.example.sea.model.Workshop;
import com.example.sea.model.Evento;
import com.example.sea.model.Palestra;
import com.example.sea.exceptions.*;
import java.util.List;

public interface IControladorWorkshop {

    void cadastrar(String titulo, String descricao, Evento evento)
        throws WorkshopJaExisteException, CampoVazioException;

    Workshop buscar(String titulo) throws WorkshopNaoEncontradoException, CampoVazioException;

    List<Workshop> listar();

    void atualizar(Workshop workshop) throws WorkshopNaoEncontradoException, CampoVazioException;

    void remover(String titulo) throws WorkshopNaoEncontradoException, CampoVazioException;

    void adicionarPalestraAoWorkshop(String tituloWorkshop, Palestra palestra)
        throws WorkshopNaoEncontradoException, CampoVazioException;

    void removerPalestraDoWorkshop(String tituloWorkshop, Palestra palestra)
            throws WorkshopNaoEncontradoException, CampoVazioException;
}