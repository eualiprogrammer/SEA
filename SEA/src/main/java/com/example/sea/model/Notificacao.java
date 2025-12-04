package com.example.sea.model;

import java.time.LocalDateTime;

public class Notificacao {
    private String titulo;
    private String mensagem;
    private LocalDateTime data;
    private boolean lida;

    public Notificacao(String titulo, String mensagem) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.data = LocalDateTime.now();
        this.lida = false;
    }

    public String getTitulo() { return titulo; }
    public String getMensagem() { return mensagem; }
    public LocalDateTime getData() { return data; }
    public boolean isLida() { return lida; }
    public void setLida(boolean lida) { this.lida = lida; }
}