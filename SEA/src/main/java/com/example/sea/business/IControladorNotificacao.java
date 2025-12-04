package com.example.sea.business;

import com.example.sea.model.Notificacao;
import com.example.sea.model.Participante;
import java.util.List;

public interface IControladorNotificacao {
    void enviarNotificacao(Participante destinatario, String titulo, String mensagem);
    List<Notificacao> listarMinhasNotificacoes(Participante participante);
    void marcarComoLida(Notificacao notificacao);
}