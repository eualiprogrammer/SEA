package com.example.sea.gui;

import com.example.sea.business.SessaoUsuario;
import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Evento;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewEventosController {

    @FXML
    private VBox containerEventos;

    @FXML
    public void initialize() {
        carregarEventos();
    }

    private void carregarEventos() {
        containerEventos.getChildren().clear();
        List<Evento> eventos = SistemaSGA.getInstance().getControladorEvento().listar();

        if (eventos.isEmpty()) {
            Label emptyLabel = new Label("Nenhum evento disponível no momento.");
            emptyLabel.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px;");
            containerEventos.getChildren().add(emptyLabel);
            return;
        }

        for (Evento evento : eventos) {
            containerEventos.getChildren().add(criarCardEvento(evento));
        }
    }

    private VBox criarCardEvento(Evento evento) {
        //Configuração Visual
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-color: #1E2130; -fx-padding: 20; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 15, 0, 0, 5); -fx-border-color: rgba(255,255,255,0.05); -fx-border-width: 1; -fx-border-radius: 15;");

        //Título
        Label lblTitulo = new Label(evento.getNome());
        lblTitulo.setStyle("-fx-text-fill: #D946EF; -fx-font-size: 22px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(217, 70, 239, 0.2), 10, 0, 0, 0);"); // Rosa Neon

        // Informações
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String inicio = evento.getDataInicio() != null ? evento.getDataInicio().format(fmt) : "-";
        String fim = evento.getDataFim() != null ? evento.getDataFim().format(fmt) : "-";

        // Texto formatado
        String infoTexto = "🗓️ Período: " + inicio + " até " + fim + "\n\n" +
                "📝 " + evento.getDescricao();

        Label lblInfo = new Label(infoTexto);
        lblInfo.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 14px;");
        lblInfo.setWrapText(true);

        //Botão
        Button btnVerPalestras = new Button("Ver Palestras Disponíveis");
        btnVerPalestras.getStyleClass().add("btn-acao"); // Estilo Roxo/Rosa do CSS
        btnVerPalestras.setOnAction(e -> verPalestras(evento));

        //Layout
        VBox conteudo = new VBox(5, lblTitulo, lblInfo);
        HBox.setHgrow(conteudo, Priority.ALWAYS);

        HBox linhaBotoes = new HBox(btnVerPalestras);
        linhaBotoes.setAlignment(Pos.BOTTOM_RIGHT);

        //card
        card.getChildren().addAll(conteudo, linhaBotoes);
        return card;
    }

    private void verPalestras(Evento evento) {
        SessaoUsuario.getInstance().setEventoSelecionado(evento);
        ScreenManager.getInstance().carregarTela("view_palestras.fxml", "Palestras - " + evento.getNome());
    }

    @FXML
    private void verMinhasInscricoes() {
        ScreenManager.getInstance().carregarTela("view_inscricoes.fxml", "Minhas Inscrições");
    }

    @FXML
    private void verMeusCertificados() {
        ScreenManager.getInstance().carregarTela("view_certificados.fxml", "Meus Certificados");
    }

    @FXML
    private void sair() {
        SessaoUsuario.getInstance().logout();
        ScreenManager.getInstance().carregarTela("TelaLogin.fxml", "Login");
    }

    @FXML
    private void verWorkshops() {
        ScreenManager.getInstance().carregarTela("view_workshops.fxml", "Workshops Disponíveis");
    }


}