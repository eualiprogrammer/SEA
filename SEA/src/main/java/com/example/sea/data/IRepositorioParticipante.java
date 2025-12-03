package com.example.sea.data;

import com.example.sea.model.Participante;
import com.example.sea.exceptions.ParticipanteJaExisteException;
import com.example.sea.exceptions.ParticipanteNaoEncontradoException;
import java.util.List;

public interface IRepositorioParticipante {

    void salvar(Participante participante) throws ParticipanteJaExisteException;

    Participante buscarPorIdentificador(String cpf) throws ParticipanteNaoEncontradoException;

    List<Participante> listarTodos();

    void atualizar(Participante participante) throws ParticipanteNaoEncontradoException;

    void deletar(String cpf) throws ParticipanteNaoEncontradoException;
}
