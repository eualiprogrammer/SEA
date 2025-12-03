package com.example.sea.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.EOFException;

import com.example.sea.model.Evento;
import com.example.sea.model.Palestra;
import com.example.sea.exceptions.PalestraJaExisteException;
import com.example.sea.exceptions.PalestraNaoEncontradaException;

import java.util.ArrayList;
import java.util.List;

public class RepositorioPalestra implements IRepositorioPalestra {

    private List<Palestra> palestras;

    private static final String NOME_ARQUIVO = "RepoPalestras.dat";

    public RepositorioPalestra() {
        this.palestras = new ArrayList<>();
        this.carregarDados();
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            System.out.println("Ficheiro 'RepoPalestras.dat' não encontrado. A começar com lista vazia.");
            return; 
        }

        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.palestras = (ArrayList<Palestra>) ois.readObject();

        } catch (EOFException e) {
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados das palestras: " + e.getMessage());
            this.palestras = new ArrayList<>();
        }
    }

    private void salvarDados() {
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this.palestras);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados das palestras: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Palestra palestra) throws PalestraJaExisteException {
        if (palestra == null) return;
        try {
            buscarPorTitulo(palestra.getTitulo());
            throw new PalestraJaExisteException(palestra.getTitulo());
        } catch (PalestraNaoEncontradaException e) {
            this.palestras.add(palestra);
            this.salvarDados();
            System.out.println("Palestra salva com sucesso: " + palestra.getTitulo());
        }
    }

    @Override
    public Palestra buscarPorTitulo(String titulo) throws PalestraNaoEncontradaException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new PalestraNaoEncontradaException("Título nulo ou vazio");
        }
        for (Palestra p : this.palestras) {
            if (p.getTitulo().equalsIgnoreCase(titulo)) {
                return p;
            }
        }
        throw new PalestraNaoEncontradaException(titulo);
    }

    @Override
    public List<Palestra> listarTodas() {
        return new ArrayList<>(this.palestras);
    }

    @Override
    public List<Palestra> listarPorEvento(Evento evento) {
        List<Palestra> listaFiltrada = new ArrayList<>();
        if (evento == null) return listaFiltrada;

        for (Palestra p : this.palestras) {
            if (p.getEvento().getNome().equals(evento.getNome())) {
                listaFiltrada.add(p);
            }
        }
        return listaFiltrada;
    }

    @Override
    public void atualizar(Palestra palestra) throws PalestraNaoEncontradaException {
        if (palestra == null) {
            throw new PalestraNaoEncontradaException("Palestra nula");
        }
        Palestra existente = this.buscarPorTitulo(palestra.getTitulo());
        existente.setDescricao(palestra.getDescricao());
        existente.setDataHoraInicio(palestra.getDataHoraInicio());
        existente.setDuracaoHoras(palestra.getDuracaoHoras());
        existente.setSala(palestra.getSala());
        existente.setPalestrante(palestra.getPalestrante());
        
        this.salvarDados();
        System.out.println("Palestra atualizada: " + palestra.getTitulo());
    }

    @Override
    public void deletar(String titulo) throws PalestraNaoEncontradaException {
        Palestra paraRemover = this.buscarPorTitulo(titulo);
        this.palestras.remove(paraRemover);
        this.salvarDados();
        System.out.println("Palestra removida: " + titulo);
    }
}
