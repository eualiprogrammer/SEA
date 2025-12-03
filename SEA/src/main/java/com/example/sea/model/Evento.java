package com.example.sea.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Evento implements Serializable{
    private static final long serialVersionUID = 3L;
    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String descricao;
    private List<Atividade> atividades; 

    public Evento(String nome, LocalDate dataInicio, LocalDate dataFim, String descricao) {
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.descricao = descricao;
        
        this.atividades = new ArrayList<>();
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Atividade> getAtividades() {
        return this.atividades;
    }
    
    public void adicionarAtividade(Atividade atividade) {
        if (atividade != null && !this.atividades.contains(atividade)) {
            this.atividades.add(atividade);
            atividade.setEvento(this); 
        }
    }

    @Override
    public String toString() {
        return this.nome;
    }
}