package com.example.sea.business;
import com.example.sea.model.Participante;
import com.example.sea.exceptions.ParticipanteJaExisteException;
import com.example.sea.exceptions.ParticipanteNaoEncontradoException;
import com.example.sea.exceptions.CampoVazioException;
import java.util.List;
 
public interface IControladorParticipante {

    void cadastrar(Participante participante) throws ParticipanteJaExisteException, CampoVazioException;

    Participante buscar(String cpf) throws ParticipanteNaoEncontradoException, CampoVazioException;

    List<Participante> listar();

    void atualizar(Participante participante) throws ParticipanteNaoEncontradoException, CampoVazioException;

    void remover(String cpf) throws ParticipanteNaoEncontradoException, CampoVazioException;

    List<Participante> listarTodos();
}