package com.example.sea.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.example.sea.model.Certificado;
import com.example.sea.model.Participante;
import com.example.sea.exceptions.CertificadoNaoEncontradoException;

public class RepositorioCertificado implements IRepositorioCertificado {

    private List<Certificado> certificados;
    private static final String NOME_ARQUIVO = "RepoCertificados.dat";

    public RepositorioCertificado() {
        this.certificados = new ArrayList<>();
        this.carregarDados();
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) return;

        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.certificados = (ArrayList<Certificado>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Erro ao carregar certificados: " + e.getMessage());
            this.certificados = new ArrayList<>();
        }
    }

    private void salvarDados() {
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this.certificados);
        } catch (IOException e) {
            System.err.println("Erro ao salvar certificados: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Certificado certificado) {
        if (certificado != null) {
            this.certificados.add(certificado);
            this.salvarDados();
            System.out.println("Certificado salvo: " + certificado.getCodigoValidacao());
        }
    }

    @Override
    public Certificado buscarPorCodigo(String codigoValidacao) throws CertificadoNaoEncontradoException {
        if (codigoValidacao == null) throw new CertificadoNaoEncontradoException("null");
        
        for (Certificado c : this.certificados) {
            if (c.getCodigoValidacao().equals(codigoValidacao)) {
                return c;
            }
        }
        throw new CertificadoNaoEncontradoException(codigoValidacao);
    }

    @Override
    public List<Certificado> listarTodos() {
        return new ArrayList<>(this.certificados);
    }

    @Override
    public List<Certificado> listarPorParticipante(Participante participante) {
        List<Certificado> resultado = new ArrayList<>();
        if (participante == null) return resultado;

        for (Certificado c : this.certificados) {
            if (c.getInscricao().getParticipante().getCpf().equals(participante.getCpf())) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    @Override
    public void deletar(String codigoValidacao) throws CertificadoNaoEncontradoException {
        Certificado c = buscarPorCodigo(codigoValidacao);
        this.certificados.remove(c);
        this.salvarDados();
        System.out.println("Certificado removido: " + codigoValidacao);
    }
}