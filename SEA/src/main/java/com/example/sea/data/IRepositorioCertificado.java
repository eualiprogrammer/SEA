package com.example.sea.data;

import com.example.sea.model.Certificado;
import com.example.sea.model.Participante;
import com.example.sea.exceptions.CertificadoNaoEncontradoException;
import java.util.List;

public interface IRepositorioCertificado {

    void salvar(Certificado certificado);

    Certificado buscarPorCodigo(String codigoValidacao) throws CertificadoNaoEncontradoException;

    List<Certificado> listarTodos();

    List<Certificado> listarPorParticipante(Participante participante);

    void deletar(String codigoValidacao) throws CertificadoNaoEncontradoException;
}
