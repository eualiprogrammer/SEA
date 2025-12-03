package com.example.sea.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.example.sea.model.Inscricao;
import com.example.sea.model.Atividade; // Importante
import com.example.sea.model.Palestra;
import com.example.sea.model.Participante;
import com.example.sea.exceptions.InscricaoJaExisteException;
import com.example.sea.exceptions.InscricaoNaoEncontradaException;

public class RepositorioInscricao implements IRepositorioInscricao {

    private List<Inscricao> inscricoes;
    private static final String NOME_ARQUIVO = "RepoInscricoes.dat";

    public RepositorioInscricao() {
        this.inscricoes = new ArrayList<>();
        this.carregarDados();
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) return;

        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.inscricoes = (ArrayList<Inscricao>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Erro ao carregar inscrições: " + e.getMessage());
            this.inscricoes = new ArrayList<>();
        }
    }

    private void salvarDados() {
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this.inscricoes);
        } catch (IOException e) {
            System.err.println("Erro ao salvar inscrições: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Inscricao inscricao) throws InscricaoJaExisteException {
        if (inscricao == null) return;

        try {
            buscar(inscricao.getParticipante(), inscricao.getAtividade());
            throw new InscricaoJaExisteException(
                    inscricao.getParticipante().getNome(),
                    inscricao.getAtividade().getTitulo()
            );
        } catch (InscricaoNaoEncontradaException e) {
            this.inscricoes.add(inscricao);
            this.salvarDados();
        }
    }

    @Override
    public Inscricao buscar(Participante participante, Atividade atividade) throws InscricaoNaoEncontradaException {
        if (participante == null || atividade == null) {
            throw new InscricaoNaoEncontradaException();
        }
        for (Inscricao i : this.inscricoes) {
            boolean mesmoParticipante = i.getParticipante().getCpf().equals(participante.getCpf());

            boolean mesmaAtividade = i.getAtividade().getTitulo().equals(atividade.getTitulo());

            if (mesmoParticipante && mesmaAtividade) {
                return i;
            }
        }
        throw new InscricaoNaoEncontradaException();
    }

    @Override
    public List<Inscricao> listarTodas() {
        return new ArrayList<>(this.inscricoes);
    }

    @Override
    public List<Inscricao> listarPorPalestra(Palestra palestra) {
        List<Inscricao> listaFiltrada = new ArrayList<>();
        if (palestra == null) return listaFiltrada;

        for (Inscricao i : this.inscricoes) {
            // Verifica se é palestra E se tem o mesmo título
            if (i.getAtividade() instanceof Palestra &&
                    i.getAtividade().getTitulo().equals(palestra.getTitulo())) {
                listaFiltrada.add(i);
            }
        }
        return listaFiltrada;
    }

    @Override
    public List<Inscricao> listarPorParticipante(Participante participante) {
        List<Inscricao> listaFiltrada = new ArrayList<>();
        if (participante == null) return listaFiltrada;

        for (Inscricao i : this.inscricoes) {
            if (i.getParticipante().getCpf().equals(participante.getCpf())) {
                listaFiltrada.add(i);
            }
        }
        return listaFiltrada;
    }

    @Override
    public void atualizar(Inscricao inscricao) throws InscricaoNaoEncontradaException {
        if (inscricao == null) throw new InscricaoNaoEncontradaException();

        Inscricao existente = this.buscar(inscricao.getParticipante(), inscricao.getAtividade());

        existente.setPresenca(inscricao.isPresenca());
        existente.setStatusConfirmacao(inscricao.getStatusConfirmacao());

        if (inscricao.getCertificado() != null) {
            existente.setCertificado(inscricao.getCertificado());
        }

        this.salvarDados();
    }

    @Override
    public void deletar(Inscricao inscricao) throws InscricaoNaoEncontradaException {
        if (inscricao == null) throw new InscricaoNaoEncontradaException();

        Inscricao paraRemover = this.buscar(inscricao.getParticipante(), inscricao.getAtividade());

        this.inscricoes.remove(paraRemover);
        this.salvarDados();
        System.out.println("Inscrição removida.");
    }

    public List<Inscricao> listarPorAtividade(Atividade atividade) {
        List<Inscricao> listaFiltrada = new ArrayList<>();
        if (atividade == null) return listaFiltrada;

        for (Inscricao i : this.inscricoes) {
            if (i.getAtividade().getTitulo().equals(atividade.getTitulo())) {
                listaFiltrada.add(i);
            }
        }
        return listaFiltrada;
    }
}