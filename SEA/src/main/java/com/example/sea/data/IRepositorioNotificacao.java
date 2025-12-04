package com.example.sea.data;

import com.example.sea.model.Notificacao;
import com.example.sea.model.Participante;
import java.util.List;

public interface IRepositorioNotificacao {
    void salvar(Notificacao notificacao);
    List<Notificacao> listarPorParticipante(Participante participante);
    void atualizar(Notificacao notificacao);
}