package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Sala;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class TelaCadastroSalaController {

    @FXML private TextField txtNome;
    @FXML private TextField txtCapacidade;
    @FXML private TextField txtProjetores;
    @FXML private TextField txtCaixasDeSom;
    @FXML private ChoiceBox<String> cbStatus;

    private Sala salaEmEdicao;

    @FXML
    public void initialize() {
        cbStatus.setItems(FXCollections.observableArrayList(
                "Livre",
                "Ocupada",
                "Em Manutenção",
                "Indisponível"
        ));
        cbStatus.setValue("Livre");
    }

    public void setSala(Sala sala) {
        this.salaEmEdicao = sala;

        if (sala != null) {
            txtNome.setText(sala.getNome());
            txtCapacidade.setText(String.valueOf(sala.getCapacidade()));
            txtProjetores.setText(String.valueOf(sala.getNumeroProjetores()));
            txtCaixasDeSom.setText(String.valueOf(sala.getNumeroCaixasSom()));

            if (sala.getStatus() != null) {
                cbStatus.setValue(sala.getStatus());
            }
        }
    }

    @FXML
    private void salvar() {
        try {
            String nome = txtNome.getText();
            String capText = txtCapacidade.getText();
            String projText = txtProjetores.getText();
            String somText = txtCaixasDeSom.getText();

            String status = cbStatus.getValue();

            if (nome == null || nome.trim().isEmpty() || capText.isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Atenção", "Preencha pelo menos Nome e Capacidade.");
                return;
            }

            int capacidade = Integer.parseInt(capText);
            int numProjetores = projText.isEmpty() ? 0 : Integer.parseInt(projText);
            int numCaixasSom = somText.isEmpty() ? 0 : Integer.parseInt(somText);

            if (salaEmEdicao != null) {
                salaEmEdicao.setNome(nome);
                salaEmEdicao.setCapacidade(capacidade);
                salaEmEdicao.setNumeroProjetores(numProjetores);
                salaEmEdicao.setNumeroCaixasSom(numCaixasSom);

                salaEmEdicao.setStatus(status);

                SistemaSGA.getInstance().getControladorSala().atualizar(salaEmEdicao);
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Sala atualizada com sucesso!");
            } else {
                Sala novaSala = new Sala(nome, capacidade, numProjetores, numCaixasSom);

                novaSala.setStatus(status);

                SistemaSGA.getInstance().getControladorSala().cadastrar(novaSala);
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Sala cadastrada com sucesso!");
            }

            cancelar();

        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.ERROR, "Erro", "Capacidade, Projetores e Som devem ser números inteiros.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Erro ao Salvar", e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        ScreenManager.getInstance().carregarTela("TelaListarSalas.fxml", "Salas");
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}