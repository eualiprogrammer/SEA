package com.example.sea.model;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Palestrante implements Serializable{
    @Serial
    private static final long serialVersionUID = 6L;
    private String nome;
    private String areaEspecializacao;
    private String telefone; 
    private String email;
    private List<Palestra> palestras; 
    
    public Palestrante(String nome, String areaEspecializacao, String telefone, String email) {
        this.nome = nome;
        this.areaEspecializacao = areaEspecializacao;
        this.telefone = telefone;
        this.email = email;
        this.palestras = new ArrayList<>();
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAreaEspecializacao() {
        return areaEspecializacao;
    }

    public void setAreaEspecializacao(String areaEspecializacao) {
        this.areaEspecializacao = areaEspecializacao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Palestra> getPalestras() {
        return this.palestras;
    }

    public void adicionarPalestra(Palestra palestra) {
        if (palestra != null && !this.palestras.contains(palestra)) {
            this.palestras.add(palestra);
        }
    }

    @Override
    public String toString() {
        return this.nome;
    }
}