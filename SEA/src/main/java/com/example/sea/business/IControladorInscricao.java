package com.example.sea.business;

import com.example.sea.model.Inscricao;
import com.example.sea.model.Participante;
import com.example.sea.model.Palestra;
import com.example.sea.model.Atividade; // Importante
import com.example.sea.model.Certificado;
import java.util.List;

public interface IControladorInscricao {

    void inscrever(Participante participante, Atividade atividade) throws Exception;

    void inscrever(Participante participante, Palestra palestra) throws Exception;

    void cancelarInscricao(Inscricao inscricao) throws Exception;

    void marcarPresenca(Inscricao inscricao) throws Exception;

    List<Inscricao> listarPorParticipante(Participante participante);

    List<Inscricao> listarPorPalestra(Palestra palestra);

    List<Inscricao> listarTodos();

    Certificado gerarCertificado(Inscricao inscricao) throws Exception;

    void atualizar(Inscricao inscricao) throws Exception;

    void cadastrar(Inscricao inscricao) throws Exception;

    List<Inscricao> listarPorAtividade(Atividade atividade);
}