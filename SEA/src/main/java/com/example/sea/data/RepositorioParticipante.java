package com.example.sea.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.EOFException;

import com.example.sea.model.Participante;
import com.example.sea.exceptions.ParticipanteJaExisteException;
import com.example.sea.exceptions.ParticipanteNaoEncontradoException;

import java.util.ArrayList;
import java.util.List;

public class RepositorioParticipante implements IRepositorioParticipante {
    private List<Participante> participantes;
    private static final String NOME_ARQUIVO = "RepoParticipantes.dat";

    public RepositorioParticipante() {
        this.participantes = new ArrayList<>();
        this.carregarDados();
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            System.out.println("Ficheiro 'RepoParticipantes.dat' não encontrado. A começar com lista vazia.");
            return;
        }
        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.participantes = (ArrayList<Participante>) ois.readObject();
        } catch (EOFException e) {
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados dos participantes: " + e.getMessage());
            this.participantes = new ArrayList<>();
        }
    }

    private void salvarDados() {
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this.participantes);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados dos participantes: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Participante participante) throws ParticipanteJaExisteException {
        if (participante == null) return;
        try {
            buscarPorIdentificador(participante.getCpf());
            throw new ParticipanteJaExisteException(participante.getCpf());
        } catch (ParticipanteNaoEncontradoException e) {
            this.participantes.add(participante);
            this.salvarDados();
            System.out.println("Participante salvo com sucesso: " + participante.getNome());
        }
    }

    @Override
    public Participante buscarPorIdentificador(String cpf) throws ParticipanteNaoEncontradoException {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new ParticipanteNaoEncontradoException("CPF nulo ou vazio");
        }
        for (Participante p : this.participantes) {
            if (p.getCpf().equals(cpf)) {
                return p;
            }
        }
        throw new ParticipanteNaoEncontradoException(cpf);
    }

    @Override
    public List<Participante> listarTodos() {
        return new ArrayList<>(this.participantes);
    }

    @Override
    public void atualizar(Participante participante) throws ParticipanteNaoEncontradoException {
        if (participante == null) {
            throw new ParticipanteNaoEncontradoException("Participante nulo");
        }
        Participante existente = this.buscarPorIdentificador(participante.getCpf());
        existente.setNome(participante.getNome());
        existente.setEmail(participante.getEmail());
        existente.setInstituicao(participante.getInstituicao());
        this.salvarDados();
        System.out.println("Participante atualizado: " + participante.getNome());
    }

    @Override
    public void deletar(String cpf) throws ParticipanteNaoEncontradoException {
        Participante paraRemover = this.buscarPorIdentificador(cpf);
        this.participantes.remove(paraRemover);
        this.salvarDados();
        System.out.println("Participante removido: " + cpf);
    }
}
