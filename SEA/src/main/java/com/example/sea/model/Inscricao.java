package com.example.sea.model;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Inscricao implements Serializable {
    @Serial
    private static final long serialVersionUID = 5L;

    private Participante participante;
    private Atividade atividade;

    private LocalDateTime dataHoraInscricao;
    private boolean presenca;
    private String statusConfirmacao;
    private Certificado certificado;

    public Inscricao(Participante participante, Atividade atividade) {
        if (participante == null || atividade == null) {
            throw new IllegalArgumentException("Participante e Atividade não podem ser nulos.");
        }
        this.participante = participante;
        this.atividade = atividade;
        this.dataHoraInscricao = LocalDateTime.now();
        this.presenca = false;
        this.statusConfirmacao = "Confirmada";

        participante.adicionarInscricao(this);
        atividade.adicionarInscricao(this);
    }

    public Certificado getCertificado() { return certificado; }
    public void setCertificado(Certificado certificado) { this.certificado = certificado; }

    public Participante getParticipante() { return participante; }
    public void setParticipante(Participante participante) { this.participante = participante; }

    public Atividade getAtividade() { return atividade; }
    public void setAtividade(Atividade atividade) { this.atividade = atividade; }

    // Método de conveniência para não quebrar código antigo (opcional)
    public Palestra getPalestra() {
        return (atividade instanceof Palestra) ? (Palestra) atividade : null;
    }

    public LocalDateTime getDataHoraInscricao() { return dataHoraInscricao; }
    public void setDataHoraInscricao(LocalDateTime dataHoraInscricao) { this.dataHoraInscricao = dataHoraInscricao; }

    public boolean isPresenca() { return presenca; }
    public void confirmarPresenca() { this.presenca = true; }
    public void marcarAusencia() { this.presenca = false; }
    public void setPresenca(boolean presenca) { this.presenca = presenca; }

    public String getStatusConfirmacao() { return statusConfirmacao; }
    public void setStatusConfirmacao(String statusConfirmacao) { this.statusConfirmacao = statusConfirmacao; }
}