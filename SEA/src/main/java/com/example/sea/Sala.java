package com.example.sea.model;
import java.io.Serializable;

public class Sala implements Serializable{
    private static final long serialVersionUID = 8L;
    private String nome;
    private int capacidade;
    private int numeroProjetores;
    private int numeroCaixasSom;
    private String status; 

    public Sala(String nome, int capacidade, int numeroProjetores, int numeroCaixasSom) {
        this.nome = nome;
        this.capacidade = capacidade;
        this.numeroProjetores = numeroProjetores;
        this.numeroCaixasSom = numeroCaixasSom;
        this.status = "Livre"; 
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public int getNumeroProjetores() {
        return numeroProjetores;
    }

    public void setNumeroProjetores(int numeroProjetores) {
        this.numeroProjetores = numeroProjetores;
    }

    public int getNumeroCaixasSom() {
        return numeroCaixasSom;
    }

    public void setNumeroCaixasSom(int numeroCaixasSom) {
        this.numeroCaixasSom = numeroCaixasSom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDisponivel() {
        return this.status.equalsIgnoreCase("Livre");
    }

    @Override
    public String toString() {
        return this.nome;
    }
}