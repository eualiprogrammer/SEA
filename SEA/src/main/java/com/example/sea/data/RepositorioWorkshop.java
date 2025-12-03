package com.example.sea.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.example.sea.model.Workshop;
import com.example.sea.exceptions.WorkshopJaExisteException;
import com.example.sea.exceptions.WorkshopNaoEncontradoException;

public class RepositorioWorkshop implements IRepositorioWorkshop {
    private List<Workshop> workshops;
    private static final String NOME_ARQUIVO = "RepoWorkshops.dat";
    public RepositorioWorkshop() {
        this.workshops = new ArrayList<>();
        this.carregarDados();
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) return;
        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.workshops = (ArrayList<Workshop>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Erro ao carregar workshops: " + e.getMessage());
            this.workshops = new ArrayList<>();
        }
    }

    private void salvarDados() {
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this.workshops);
        } catch (IOException e) {
            System.err.println("Erro ao salvar workshops: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Workshop workshop) throws WorkshopJaExisteException {
        if (workshop == null) return;
        try {
            buscarPorTitulo(workshop.getTitulo());
            throw new WorkshopJaExisteException(workshop.getTitulo());
        } catch (WorkshopNaoEncontradoException e) {
            this.workshops.add(workshop);
            this.salvarDados();
            System.out.println("Workshop salvo: " + workshop.getTitulo());
        }
    }

    @Override
    public Workshop buscarPorTitulo(String titulo) throws WorkshopNaoEncontradoException {
        if (titulo == null) throw new WorkshopNaoEncontradoException("null");
        for (Workshop w : this.workshops) {
            if (w.getTitulo().equalsIgnoreCase(titulo)) return w;
        }
        throw new WorkshopNaoEncontradoException(titulo);
    }

    @Override
    public List<Workshop> listarTodos() {
        return new ArrayList<>(this.workshops);
    }

    @Override
    public void atualizar(Workshop workshop) throws WorkshopNaoEncontradoException {
        if (workshop == null) throw new WorkshopNaoEncontradoException("null");
        Workshop existente = buscarPorTitulo(workshop.getTitulo());
        existente.setDescricao(workshop.getDescricao());
        this.salvarDados();
        System.out.println("Workshop atualizado: " + workshop.getTitulo());
    }

    @Override
    public void deletar(String titulo) throws WorkshopNaoEncontradoException {
        Workshop w = buscarPorTitulo(titulo);
        this.workshops.remove(w);
        this.salvarDados();
        System.out.println("Workshop removido: " + titulo);
    }
}