package com.example.sea.data;

import com.example.sea.model.Workshop;
import com.example.sea.exceptions.WorkshopJaExisteException;
import com.example.sea.exceptions.WorkshopNaoEncontradoException;
import java.util.List;

public interface IRepositorioWorkshop {
    void salvar(Workshop workshop) throws WorkshopJaExisteException;
    Workshop buscarPorTitulo(String titulo) throws WorkshopNaoEncontradoException;
    List<Workshop> listarTodos();
    void atualizar(Workshop workshop) throws WorkshopNaoEncontradoException;
    void deletar(String titulo) throws WorkshopNaoEncontradoException;
}