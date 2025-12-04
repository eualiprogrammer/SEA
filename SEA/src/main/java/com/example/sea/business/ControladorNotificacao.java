package com.example.sea.business;

import com.example.sea.data.RepositorioNotificacao;
import com.example.sea.model.Notificacao;
import com.example.sea.model.Participante;
import java.util.List;

public class ControladorNotificacao implements IControladorNotificacao {

    private RepositorioNotificacao repositorio;

    public ControladorNotificacao() {
        this.repositorio = new RepositorioNotificacao();
    }

    @Override
    public void enviarNotificacao(Participante destinatario, String titulo, String mensagem) {
        Notificacao nova = new Notificacao(titulo, mensagem, destinatario);
        repositorio.salvar(nova);

        new Thread(() -> {
            try {
                System.out.println("Simulando envio de e-mail para: " + destinatario.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public List<Notificacao> listarMinhasNotificacoes(Participante participante) {
        return repositorio.listarPorParticipante(participante);
    }

    @Override
    public void marcarComoLida(Notificacao notificacao) {
        if (notificacao != null) {
            notificacao.marcarComoLida();
            repositorio.atualizar();
        }
    }
}