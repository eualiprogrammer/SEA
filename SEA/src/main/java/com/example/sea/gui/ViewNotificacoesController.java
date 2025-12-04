package com.example.sea.gui;

import com.example.sea.business.SessaoUsuario;
import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Notificacao;
import com.example.sea.model.Participante;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewNotificacoesController {

    @FXML private VBox containerNotificacoes;

    @FXML
    public void initialize() {
        carregarNotificacoes();
    }

    private void carregarNotificacoes() {
        containerNotificacoes.getChildren().clear();

        Participante logado = SessaoUsuario.getInstance().getParticipanteLogado();
        if (logado == null) {
            ScreenManager.getInstance().carregarTela("TelaLogin.fxml", "Login");
            return;
        }

        List<Notificacao> notificacoes = SistemaSGA.getInstance()
                .getControladorNotificacao()
                .listarMinhasNotificacoes(logado);

        if (notificacoes.isEmpty()) {
            Label emptyLabel = new Label("Você não tem novas notificações.");
            emptyLabel.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px;");
            containerNotificacoes.getChildren().add(emptyLabel);
            return;
        }

        notificacoes.sort((n1, n2) -> n2.getDataHora().compareTo(n1.getDataHora()));

        for (Notificacao n : notificacoes) {
            containerNotificacoes.getChildren().add(criarCardNotificacao(n));
        }
    }

    private VBox criarCardNotificacao(Notificacao notif) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");

        String estiloBase = notif.isLida()
                ? "-fx-background-color: #151720; -fx-padding: 15; -fx-background-radius: 10; -fx-opacity: 0.6; -fx-border-color: #333; -fx-border-width: 1;"
                : "-fx-background-color: #1E2130; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: rgba(255,255,255,0.1); -fx-border-width: 1;";

        card.setStyle(estiloBase);

        // Título
        Label lblTitulo = new Label((notif.isLida() ? "" : "🔔 ") + notif.getTitulo());
        lblTitulo.setStyle("-fx-text-fill: #D946EF; -fx-font-size: 16px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(217, 70, 239, 0.3), 10, 0, 0, 0);");

        // Data
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Label lblData = new Label(notif.getDataHora().format(fmt));
        lblData.setStyle("-fx-text-fill: #64748B; -fx-font-size: 11px;");

        // Mensagem
        Label lblMensagem = new Label(notif.getMensagem());
        lblMensagem.setStyle("-fx-text-fill: #E2E8F0; -fx-font-size: 13px;");
        lblMensagem.setWrapText(true);

        // Botão
        Button btnLida = new Button("Marcar como lida");
        btnLida.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-border-color: #94A3B8; -fx-border-radius: 20; -fx-font-size: 10px; -fx-cursor: hand;");

        if (notif.isLida()) {
            btnLida.setDisable(true);
            btnLida.setText("Lida ✓");
        }

        btnLida.setOnAction(e -> {
            SistemaSGA.getInstance().getControladorNotificacao().marcarComoLida(notif);
            card.setStyle("-fx-background-color: #151720; -fx-padding: 15; -fx-background-radius: 10; -fx-opacity: 0.6;");
            btnLida.setDisable(true);
            btnLida.setText("Lida ✓");
            lblTitulo.setText(notif.getTitulo());
        });

        HBox topo = new HBox(10, lblTitulo, lblData);
        HBox.setHgrow(lblTitulo, Priority.ALWAYS);
        lblData.setStyle("-fx-alignment: CENTER-RIGHT;");

        card.getChildren().addAll(topo, lblMensagem, btnLida);
        return card;
    }

    @FXML
    private void voltar() {
        ScreenManager.getInstance().carregarTela("view_eventos.fxml", "Área do Aluno");
    }
}