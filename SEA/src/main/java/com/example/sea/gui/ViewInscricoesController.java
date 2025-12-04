package com.example.sea.gui;

import com.example.sea.business.SessaoUsuario;
import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Inscricao;
import com.example.sea.model.Palestra;
import com.example.sea.model.Participante;
import com.example.sea.model.Workshop;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewInscricoesController {

    @FXML
    private VBox containerInscricoes;

    @FXML
    public void initialize() {
        carregarMinhasInscricoes();
    }

    private void carregarMinhasInscricoes() {
        containerInscricoes.getChildren().clear();

        Participante logado = SessaoUsuario.getInstance().getParticipanteLogado();
        if (logado == null) {
            ScreenManager.getInstance().carregarTela("TelaLogin.fxml", "Login");
            return;
        }

        List<Inscricao> minhasInscricoes = SistemaSGA.getInstance()
                .getControladorInscricao()
                .listarPorParticipante(logado);

        if (minhasInscricoes.isEmpty()) {
            Label emptyLabel = new Label("Você ainda não se inscreveu em nenhuma atividade.");
            emptyLabel.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px;");
            containerInscricoes.getChildren().add(emptyLabel);
            return;
        }

        for (Inscricao inscricao : minhasInscricoes) {
            containerInscricoes.getChildren().add(criarCardInscricao(inscricao));
        }
    }

    private VBox criarCardInscricao(Inscricao inscricao) {
        //Configuração Visual
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-color: #1E2130; -fx-padding: 15; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0); -fx-border-color: rgba(255,255,255,0.05); -fx-border-width: 1;");

        //Dados
        String nomeAtividade = inscricao.getAtividade().getTitulo();

        String nomeEvento = "-";
        if (inscricao.getAtividade().getEvento() != null) {
            nomeEvento = inscricao.getAtividade().getEvento().getNome();
        }

        //Data e Local
        String dataHora = "Data a definir";
        String local = "Local a definir";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

        if (inscricao.getAtividade() instanceof Palestra) {
            Palestra p = (Palestra) inscricao.getAtividade();
            if (p.getDataHoraInicio() != null) dataHora = p.getDataHoraInicio().format(fmt);
            if (p.getSala() != null) local = p.getSala().getNome();
        }
        else if (inscricao.getAtividade() instanceof Workshop) {
            dataHora = "Ver detalhes do evento";
            local = "Múltiplos locais";
        }

        // Título
        Label lblTitulo = new Label(nomeAtividade);
        lblTitulo.setStyle("-fx-text-fill: #8B5CF6; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Detalhes
        String detalhes = String.format("📅 %s\n📍 %s\n🎉 %s", dataHora, local, nomeEvento);
        Label lblDetalhes = new Label(detalhes);
        lblDetalhes.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 13px;");
        lblDetalhes.setWrapText(true);

        // Status
        Label lblStatus = new Label(inscricao.isPresenca() ? "✅ Presença Confirmada" : "⏳ Aguardando Realização");
        lblStatus.setStyle(inscricao.isPresenca() ?
                "-fx-text-fill: #2ecc71; -fx-font-weight: bold;" :
                "-fx-text-fill: #f1c40f; -fx-font-style: italic;");

        //Botão Cancelar
        Button btnCancelar = new Button("Cancelar Inscrição");
        btnCancelar.getStyleClass().add("btn-perigo");

        if (inscricao.isPresenca()) {
            btnCancelar.setDisable(true);
            btnCancelar.setText("Concluído");
            btnCancelar.setStyle("-fx-opacity: 0.5;");
        } else {
            btnCancelar.setOnAction(e -> cancelarInscricao(inscricao));
        }

        //Layout
        VBox infoBox = new VBox(5, lblTitulo, lblDetalhes, lblStatus);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        HBox linhaBotoes = new HBox(btnCancelar);
        linhaBotoes.setAlignment(Pos.CENTER_RIGHT);

        card.getChildren().addAll(infoBox, linhaBotoes);

        return card;
    }

    private void cancelarInscricao(Inscricao inscricao) {
        try {
            SistemaSGA.getInstance().getControladorInscricao().cancelarInscricao(inscricao);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Cancelado", "Sua inscrição foi cancelada.");
            carregarMinhasInscricoes();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível cancelar: " + e.getMessage());
        }
    }

    @FXML
    private void voltar() {
        ScreenManager.getInstance().carregarTela("view_eventos.fxml", "Área do Aluno");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}