package com.example.sea.business;

import com.example.sea.data.IRepositorioCertificado;
import com.example.sea.data.RepositorioCertificado;
import com.example.sea.exceptions.CampoVazioException;
import com.example.sea.exceptions.CertificadoNaoEncontradoException;
import com.example.sea.model.Certificado;
import com.example.sea.model.Participante;

import java.util.List;

public class ControladorCertificado implements IControladorCertificado {

    private IRepositorioCertificado repositorioCertificado;

    public ControladorCertificado() {
        this.repositorioCertificado = new RepositorioCertificado();
    }

    @Override
    public Certificado buscar(String codigoValidacao) throws CertificadoNaoEncontradoException, CampoVazioException {
        if (codigoValidacao == null || codigoValidacao.trim().isEmpty()) {
            throw new CampoVazioException("Código de Validação");
        }
        return this.repositorioCertificado.buscarPorCodigo(codigoValidacao);
    }

    @Override
    public List<Certificado> listarTodos() {
        return this.repositorioCertificado.listarTodos();
    }

    @Override
    public List<Certificado> listarPorParticipante(Participante participante) {
        if (participante == null) {
            throw new IllegalArgumentException("O participante não pode ser nulo para buscar o histórico.");
        }
        return this.repositorioCertificado.listarPorParticipante(participante);
    }

    @Override
    public void remover(String codigoValidacao) throws CertificadoNaoEncontradoException, CampoVazioException {
        if (codigoValidacao == null || codigoValidacao.trim().isEmpty()) {
            throw new CampoVazioException("Código de Validação");
        }
        this.repositorioCertificado.deletar(codigoValidacao);
    }

    @Override
    public void cadastrar(Certificado certificado) throws Exception {
        if (certificado == null) throw new IllegalArgumentException("Certificado nulo");
        this.repositorioCertificado.salvar(certificado);
    }
}
