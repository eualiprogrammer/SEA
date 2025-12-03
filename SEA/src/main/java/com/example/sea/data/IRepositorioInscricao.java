package com.example.sea.data;

import com.example.sea.model.Inscricao;
import com.example.sea.model.Atividade; // <--- Importante
import com.example.sea.model.Participante;
import com.example.sea.model.Palestra;
import com.example.sea.exceptions.InscricaoJaExisteException;
import com.example.sea.exceptions.InscricaoNaoEncontradaException;
import java.util.List;

public interface IRepositorioInscricao {
    void salvar(Inscricao inscricao) throws InscricaoJaExisteException;


    Inscricao buscar(Participante participante, Atividade atividade) throws InscricaoNaoEncontradaException;

    List<Inscricao> listarTodas();
    List<Inscricao> listarPorPalestra(Palestra palestra);
    List<Inscricao> listarPorParticipante(Participante participante);
    void atualizar(Inscricao inscricao) throws InscricaoNaoEncontradaException;
    void deletar(Inscricao inscricao) throws InscricaoNaoEncontradaException;
    List<Inscricao> listarPorAtividade(Atividade atividade);
}