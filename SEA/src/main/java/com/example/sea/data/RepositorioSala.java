package com.example.sea.data;

import com.example.sea.exceptions.SalaJaExisteException;
import com.example.sea.exceptions.SalaNaoEncontradaException;
import com.example.sea.model.Sala;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioSala implements IRepositorioSala {
    private List<Sala> salas;
    private static final String NOME_ARQUIVO = "RepoSalas.dat";

    public RepositorioSala() {
        this.salas = new ArrayList<>();
        this.carregarDados();
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            System.out.println("Ficheiro 'RepoSalas.dat' não encontrado. A começar com lista vazia.");
            return;
        }
        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.salas = (ArrayList<Sala>) ois.readObject();
        } catch (EOFException e) {
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados das salas: " + e.getMessage());
            this.salas = new ArrayList<>();
        }
    }

    private void salvarDados() {
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this.salas);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados das salas: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Sala sala) throws SalaJaExisteException {
        if (sala == null) return;
        try {
            buscarPorNome(sala.getNome());
            throw new SalaJaExisteException(sala.getNome());
        } catch (SalaNaoEncontradaException e) {
            this.salas.add(sala);
            this.salvarDados();
            System.out.println("Sala salva com sucesso: " + sala.getNome());
        }
    }

    @Override
    public Sala buscarPorNome(String nome) throws SalaNaoEncontradaException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new SalaNaoEncontradaException("Nome nulo ou vazio");
        }
        for (Sala s : this.salas) {
            if (s.getNome().equalsIgnoreCase(nome)) {
                return s;
            }
        }
        throw new SalaNaoEncontradaException(nome);
    }

    @Override
    public List<Sala> listarTodas() {
        return new ArrayList<>(this.salas);
    }

    @Override
    public void atualizar(Sala sala) throws SalaNaoEncontradaException {
        if (sala == null) {
            throw new SalaNaoEncontradaException("Sala nula");
        }
        Sala existente = this.buscarPorNome(sala.getNome());
        existente.setCapacidade(sala.getCapacidade());
        existente.setNumeroProjetores(sala.getNumeroProjetores());
        existente.setNumeroCaixasSom(sala.getNumeroCaixasSom());
        existente.setStatus(sala.getStatus());
        this.salvarDados();
        System.out.println("Sala atualizada: " + sala.getNome());
    }

    @Override
    public void deletar(String nome) throws SalaNaoEncontradaException {
        Sala paraRemover = this.buscarPorNome(nome);
        this.salas.remove(paraRemover);
        this.salvarDados();
        System.out.println("Sala removida: " + nome);
    }
}
