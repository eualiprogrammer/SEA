package com.example.sea.model;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Atividade implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private String titulo;
    private String descricao;
    private Evento evento;

    private List<Inscricao> inscricoes;

    public Atividade(String titulo, String descricao, Evento evento) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.evento = evento;
        this.inscricoes = new ArrayList<>();
    }

    public List<Inscricao> getInscricoes() {
        return this.inscricoes;
    }

    public void adicionarInscricao(Inscricao inscricao) {
        if (inscricao != null && !this.inscricoes.contains(inscricao)) {
            this.inscricoes.add(inscricao);
        }
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    @Override
    public String toString() { return this.titulo; }
}