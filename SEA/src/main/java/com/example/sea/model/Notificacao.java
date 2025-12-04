package com.example.sea.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Notificacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private String titulo;
    private String mensagem;
    private LocalDateTime dataHora;
    private Participante destinatario;
    private boolean lida;

    public Notificacao(String titulo, String mensagem, Participante destinatario) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.destinatario = destinatario;
        this.dataHora = LocalDateTime.now();
        this.lida = false;
    }

    public String getTitulo() { return titulo; }
    public String getMensagem() { return mensagem; }
    public LocalDateTime getDataHora() { return dataHora; }
    public Participante getDestinatario() { return destinatario; }
    public boolean isLida() { return lida; }
    public void marcarComoLida() { this.lida = true; }
}