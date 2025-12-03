package com.example.sea.business;

import com.example.sea.model.Evento;
import com.example.sea.model.Participante;

public class SessaoUsuario {

    private static SessaoUsuario instance;
    private Participante participanteLogado;

    private SessaoUsuario() {}

    public static SessaoUsuario getInstance() {
        if (instance == null) {
            instance = new SessaoUsuario();
        }
        return instance;
    }

    public void login(Participante participante) {
        this.participanteLogado = participante;
    }

    public void logout() {
        this.participanteLogado = null;
    }

    public Participante getParticipanteLogado() {
        return participanteLogado;
    }

    public boolean isParticipante() {
        return participanteLogado != null;
    }

    private Evento eventoSelecionado; // <--- NOVO

    public void setEventoSelecionado(Evento evento) {
        this.eventoSelecionado = evento;
    }

    public Evento getEventoSelecionado() {
        return eventoSelecionado;
    }
}