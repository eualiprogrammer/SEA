package com.example.sea.data;

import com.example.sea.model.Palestrante;
import com.example.sea.exceptions.PalestranteJaExisteException;
import com.example.sea.exceptions.PalestranteNaoEncontradoException;
import java.util.List;

public interface IRepositorioPalestrante {

    void salvar(Palestrante palestrante) throws PalestranteJaExisteException;

    Palestrante buscarPorIdentificador(String identificador) throws PalestranteNaoEncontradoException;

    List<Palestrante> listarTodos();

    void atualizar(Palestrante palestrante) throws PalestranteNaoEncontradoException;

    void deletar(String identificador) throws PalestranteNaoEncontradoException;
}
