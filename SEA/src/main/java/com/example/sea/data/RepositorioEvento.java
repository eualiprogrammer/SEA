package com.example.sea.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.EOFException;

import com.example.sea.model.Evento;
import com.example.sea.exceptions.EventoJaExisteException;
import com.example.sea.exceptions.EventoNaoEncontradoException;

import java.util.ArrayList;
import java.util.List;

public class RepositorioEvento implements IRepositorioEvento {

    private List<Evento> eventos;
    
    private static final String NOME_ARQUIVO = "RepoEventos.dat";

    public RepositorioEvento() {
        this.eventos = new ArrayList<>();
        this.carregarDados();
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            System.out.println("Ficheiro 'RepoEventos.dat' não encontrado. A começar com lista vazia.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.eventos = (ArrayList<Evento>) ois.readObject();
        } catch (EOFException e) {

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados dos eventos: " + e.getMessage());
            this.eventos = new ArrayList<>();
        }
    }

    private void salvarDados() {
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this.eventos);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados dos eventos: " + e.getMessage());
        }
    }
    
    @Override
    public void salvar(Evento evento) throws EventoJaExisteException {
        if (evento == null) return;
        try {
            buscarPorNome(evento.getNome());
            throw new EventoJaExisteException(evento.getNome());
        } catch (EventoNaoEncontradoException e) {
            this.eventos.add(evento);
            this.salvarDados();
            System.out.println("Evento salvo com sucesso: " + evento.getNome());
        }
    }

    @Override
    public Evento buscarPorNome(String nome) throws EventoNaoEncontradoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new EventoNaoEncontradoException("Nome nulo ou vazio");
        }
        for (Evento e : this.eventos) {
            if (e.getNome().equalsIgnoreCase(nome)) {
                return e;
            }
        }
        throw new EventoNaoEncontradoException(nome);
    }

    @Override
    public List<Evento> listarTodos() {
        return new ArrayList<>(this.eventos);
    }

    @Override
    public void atualizar(Evento evento) throws EventoNaoEncontradoException {
        if (evento == null) {
            throw new EventoNaoEncontradoException("Evento nulo");
        }
        Evento existente = this.buscarPorNome(evento.getNome());
        existente.setDescricao(evento.getDescricao());
        existente.setDataInicio(evento.getDataInicio());
        existente.setDataFim(evento.getDataFim());
        this.salvarDados();
        System.out.println("Evento atualizado: " + evento.getNome());
    }

    @Override
    public void deletar(String nome) throws EventoNaoEncontradoException {
        Evento paraRemover = this.buscarPorNome(nome);
        this.eventos.remove(paraRemover);
        this.salvarDados();
        System.out.println("Evento removido: " + nome);
    }
}
