package com.example.sea.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Certificado implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;
    private Inscricao inscricao;
    private String codigoValidacao;
    private LocalDate dataEmissao;
    private float cargaHorariaRegistrada;

    public Certificado(Inscricao inscricao) {
        if (inscricao == null) {
            throw new IllegalArgumentException("A inscrição não pode ser nula.");
        }

        if (!inscricao.isPresenca()) {
            throw new RuntimeException("Não é possível gerar certificado: Participante '" +
                    inscricao.getParticipante().getNome() + "' está marcado como ausente.");
        }

        this.inscricao = inscricao;
        this.dataEmissao = LocalDate.now();
        this.codigoValidacao = UUID.randomUUID().toString();

        Atividade atividade = inscricao.getAtividade();

        if (atividade instanceof Palestra) {
            this.cargaHorariaRegistrada = ((Palestra) atividade).getDuracaoHoras();
        } else if (atividade instanceof Workshop) {
            this.cargaHorariaRegistrada = ((Workshop) atividade).getDuracaoHoras();
        } else {
            this.cargaHorariaRegistrada = 0;
        }

        inscricao.setCertificado(this);
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public String getCodigoValidacao() {
        return codigoValidacao;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public float getCargaHorariaRegistrada() {
        return cargaHorariaRegistrada;
    }

    public String getNomeParticipante() {
        return this.inscricao.getParticipante().getNome();
    }

    public String getTituloAtividade() {
        return this.inscricao.getAtividade().getTitulo();
    }
}