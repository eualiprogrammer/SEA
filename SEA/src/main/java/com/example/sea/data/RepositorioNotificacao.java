package com.example.sea.data;

import com.example.sea.model.Notificacao;
import com.example.sea.model.Participante;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioNotificacao {
    private List<Notificacao> notificacoes;
    private static final String NOME_ARQUIVO = "RepoNotificacoes.dat";

    public RepositorioNotificacao() {
        this.notificacoes = new ArrayList<>();
        carregarDados();
    }

    public void salvar(Notificacao notificacao) {
        this.notificacoes.add(notificacao);
        salvarDados();
    }

    public void atualizar() {
        salvarDados();
    }

    public List<Notificacao> listarPorParticipante(Participante participante) {
        return this.notificacoes.stream()
                .filter(n -> n.getDestinatario().getCpf().equals(participante.getCpf()))
                .collect(Collectors.toList());
    }

    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            this.notificacoes = (ArrayList<Notificacao>) ois.readObject();
        } catch (Exception e) {
            this.notificacoes = new ArrayList<>();
        }
    }

    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO))) {
            oos.writeObject(this.notificacoes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}