package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Evento;
import com.example.sea.model.Workshop;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TelaCadastroWorkshopController {

    @FXML private TextField txtNome;
    @FXML private ChoiceBox<Evento> cbEvento;
    @FXML private TextArea txtDescricao;

    private Workshop workshopEmEdicao;

    @FXML
    public void initialize() {
        cbEvento.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorEvento().listar()
        ));
    }

    public void setWorkshop(Workshop workshop) {
        this.workshopEmEdicao = workshop;
        if (workshop != null) {
            txtNome.setText(workshop.getTitulo());
            txtDescricao.setText(workshop.getDescricao());
            cbEvento.setValue(workshop.getEvento());

            txtNome.setDisable(true);
        }
    }

    @FXML
    private void salvar() {
        try {
            String titulo = txtNome.getText();
            String descricao = txtDescricao.getText();
            Evento evento = cbEvento.getValue();

            if (titulo == null || titulo.trim().isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Atenção", "O título é obrigatório.");
                return;
            }
            if (evento == null) {
                mostrarAlerta(AlertType.WARNING, "Atenção", "Selecione um evento.");
                return;
            }

            if (workshopEmEdicao != null) {
                workshopEmEdicao.setDescricao(descricao);
                workshopEmEdicao.setEvento(evento);

                SistemaSGA.getInstance().getControladorWorkshop().atualizar(workshopEmEdicao);
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Workshop atualizado!");

            } else {
                SistemaSGA.getInstance().getControladorWorkshop().cadastrar(titulo, descricao, evento);

                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Workshop criado!");
            }

            cancelar();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        ScreenManager.getInstance().carregarTela("TelaListarWorkshops.fxml", "Workshops");
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}