package com.example.sea.gui;

import com.example.sea.business.SessaoUsuario;
import com.example.sea.business.SistemaSGA;
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

import java.util.List;

public class ViewWorkshopsController {

    @FXML
    private VBox containerWorkshops;

    @FXML
    public void initialize() {
        carregarWorkshops();
    }

    private void carregarWorkshops() {
        containerWorkshops.getChildren().clear();
        List<Workshop> workshops = SistemaSGA.getInstance().getControladorWorkshop().listar();

        if (workshops.isEmpty()) {
            Label emptyLabel = new Label("Nenhum workshop disponível no momento.");
            emptyLabel.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px;");
            containerWorkshops.getChildren().add(emptyLabel);
            return;
        }

        for (Workshop w : workshops) {
            containerWorkshops.getChildren().add(criarCardWorkshop(w));
        }
    }

    private VBox criarCardWorkshop(Workshop workshop) {
        // Visual
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-color: #1E2130; -fx-padding: 20; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 15, 0, 0, 5); -fx-border-color: rgba(255,255,255,0.05); -fx-border-width: 1;");

        // Título e Info
        Label lblTitulo = new Label(workshop.getTitulo());
        lblTitulo.setStyle("-fx-text-fill: #D946EF; -fx-font-size: 20px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(217, 70, 239, 0.3), 10, 0, 0, 0);");

        String nomeEvento = workshop.getEvento() != null ? workshop.getEvento().getNome() : "Evento Geral";
        String descricao = workshop.getDescricao();
        int qtdPalestras = workshop.getPalestrasDoWorkshop().size();

        Label lblInfo = new Label("🔗 Vinculado a: " + nomeEvento + "\n📚 Contém: " + qtdPalestras + " palestras\n📝 " + descricao);
        lblInfo.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 14px;");
        lblInfo.setWrapText(true);



        //Botão Ver Palestras
        Button btnVerPalestras = new Button("👁 Ver Palestras");
        btnVerPalestras.setStyle("-fx-background-color: transparent; -fx-text-fill: #D946EF; -fx-border-color: #D946EF; -fx-border-radius: 30; -fx-cursor: hand;");
        btnVerPalestras.setOnAction(e -> mostrarListaPalestras(workshop));

        //Botão Inscrever-se
        Button btnInscrever = new Button("Inscrever-se em Tudo");
        btnInscrever.getStyleClass().add("btn-acao");
        btnInscrever.setStyle("-fx-background-color: linear-gradient(to right, #8B5CF6, #D946EF); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 30;");
        btnInscrever.setOnAction(e -> inscreverNoWorkshop(workshop, btnInscrever));

        // Layout
        VBox conteudo = new VBox(5, lblTitulo, lblInfo);
        HBox.setHgrow(conteudo, Priority.ALWAYS);

        HBox linhaBotoes = new HBox(10, btnVerPalestras, btnInscrever);
        linhaBotoes.setAlignment(Pos.CENTER_RIGHT);

        // Organização
        HBox linhaPrincipal = new HBox(10);
        linhaPrincipal.setAlignment(Pos.CENTER_LEFT);
        linhaPrincipal.getChildren().addAll(conteudo);

        card.getChildren().addAll(linhaPrincipal, linhaBotoes);
        return card;
    }

    private void mostrarListaPalestras(Workshop workshop) {
        StringBuilder lista = new StringBuilder();
        if (workshop.getPalestrasDoWorkshop().isEmpty()) {
            lista.append("Nenhuma palestra vinculada ainda.");
        } else {
            for (Palestra p : workshop.getPalestrasDoWorkshop()) {
                lista.append("• ").append(p.getTitulo()).append("\n");
                if (p.getSala() != null) lista.append("   Local: ").append(p.getSala().getNome()).append("\n");
                lista.append("\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Conteúdo do Workshop");
        alert.setHeaderText("Palestras incluídas neste pacote:");
        alert.setContentText(lista.toString());
        alert.getDialogPane().setMinWidth(400);
        alert.showAndWait();
    }

    private void inscreverNoWorkshop(Workshop workshop, Button btn) {
        Participante participante = SessaoUsuario.getInstance().getParticipanteLogado();

        if (participante == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Faça login para continuar.");
            return;
        }

        try {
            SistemaSGA.getInstance().getControladorInscricao().inscrever(participante, workshop);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Inscrição realizada! Você foi inscrito automaticamente em todas as palestras deste workshop.");

            btn.setText("Inscrito ✓");
            btn.setDisable(true);
            btn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 30;");

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Erro ao inscrever: " + e.getMessage());
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