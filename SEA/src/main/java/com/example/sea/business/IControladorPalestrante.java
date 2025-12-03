package com.example.sea.business;

import com.example.sea.model.Palestrante;
import com.example.sea.exceptions.PalestranteJaExisteException;
import com.example.sea.exceptions.PalestranteNaoEncontradoException;
import com.example.sea.exceptions.CampoVazioException; 
import java.util.List;

public interface IControladorPalestrante {
    void cadastrar(Palestrante palestrante) throws PalestranteJaExisteException, CampoVazioException;
    
    Palestrante buscar(String email) throws PalestranteNaoEncontradoException, CampoVazioException;

    List<Palestrante> listar();
   
    void atualizar(Palestrante palestrante) throws PalestranteNaoEncontradoException, CampoVazioException;
  
    void remover(String email) throws PalestranteNaoEncontradoException, CampoVazioException;
}