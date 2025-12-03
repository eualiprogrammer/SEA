package com.example.sea.model;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Palestra extends Atividade{
    @Serial
    private static final long serialVersionUID = 5L;
    private LocalDateTime dataHoraInicio;
    private float duracaoHoras;
    private Sala sala; 
    private Palestrante palestrante; 
    private List<Inscricao> inscricoes; 

    public Palestra(String titulo, String descricao, Evento evento, 
                    LocalDateTime dataHoraInicio, float duracaoHoras, 
                    Sala sala, Palestrante palestrante) {
        
        super(titulo, descricao, evento); 
        this.dataHoraInicio = dataHoraInicio;
        this.duracaoHoras = duracaoHoras;
        this.sala = sala;
        this.palestrante = palestrante;
        this.inscricoes = new ArrayList<>();
        
        if (palestrante != null) {
            palestrante.adicionarPalestra(this);
        }
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public float getDuracaoHoras() {
        return duracaoHoras;
    }

    public void setDuracaoHoras(float duracaoHoras) {
        this.duracaoHoras = duracaoHoras;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public Palestrante getPalestrante() {
        return palestrante;
    }

    public void setPalestrante(Palestrante palestrante) {
        this.palestrante = palestrante;
        if (palestrante != null) {
            palestrante.adicionarPalestra(this);
        }
    }

    public List<Inscricao> getInscricoes() {
        return this.inscricoes;
    }

    public void adicionarInscricao(Inscricao inscricao) {
        if (inscricao != null && !this.inscricoes.contains(inscricao)) {
            this.inscricoes.add(inscricao);
        }
    }

    @Override
    public String toString() {
        return this.getTitulo();
    }
}