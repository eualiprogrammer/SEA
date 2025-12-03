package com.example.sea.data;

import java.io.*;
import com.example.sea.model.Palestrante;
import com.example.sea.exceptions.PalestranteJaExisteException;
import com.example.sea.exceptions.PalestranteNaoEncontradoException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPalestrante implements IRepositorioPalestrante {
    private List<Palestrante> palestrantes;
    private static final String NOME_ARQUIVO = "RepoPalestrantes.dat";
    public RepositorioPalestrante() {
        this.palestrantes = new ArrayList<>();
        this.carregarDados();
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            return;
        }
        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.palestrantes = (ArrayList<Palestrante>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados dos palestrantes: " + e.getMessage());
            this.palestrantes = new ArrayList<>();
        }
    }

    private void salvarDados() {
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this.palestrantes);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados dos palestrantes: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Palestrante palestrante) throws PalestranteJaExisteException {
        if (palestrante == null) return;
        try {
            buscarPorIdentificador(palestrante.getEmail());
            throw new PalestranteJaExisteException(palestrante.getEmail());
        } catch (PalestranteNaoEncontradoException e) {
            this.palestrantes.add(palestrante);
            this.salvarDados();
            System.out.println("Palestrante salvo: " + palestrante.getNome());
        }
    }

    @Override
    public Palestrante buscarPorIdentificador(String identificador) throws PalestranteNaoEncontradoException {
        if (identificador == null) {
            throw new PalestranteNaoEncontradoException("null");
        }
        for (Palestrante p : this.palestrantes) {
            if (p.getEmail().equalsIgnoreCase(identificador)) {
                return p;
            }
        }
        throw new PalestranteNaoEncontradoException(identificador);
    }

    @Override
    public List<Palestrante> listarTodos() {
        return new ArrayList<>(this.palestrantes);
    }

    @Override
    public void atualizar(Palestrante palestrante) throws PalestranteNaoEncontradoException {
        if (palestrante == null) {
            throw new PalestranteNaoEncontradoException("null");
        }
        Palestrante existente = this.buscarPorIdentificador(palestrante.getEmail());
        existente.setNome(palestrante.getNome());
        existente.setTelefone(palestrante.getTelefone());
        existente.setAreaEspecializacao(palestrante.getAreaEspecializacao());
        this.salvarDados();
        System.out.println("Palestrante atualizado: " + palestrante.getNome());
    }

    @Override
    public void deletar(String identificador) throws PalestranteNaoEncontradoException {
        Palestrante paraRemover = this.buscarPorIdentificador(identificador);
        this.palestrantes.remove(paraRemover);
        this.salvarDados();
        System.out.println("Palestrante removido: " + identificador);
    }
}
