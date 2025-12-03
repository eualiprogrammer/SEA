package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Palestrante;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TelaListarPalestrantesController {

    @FXML private TableView<Palestrante> tabelaPalestrantes;
    @FXML private TableColumn<Palestrante, String> colNome;
    @FXML private TableColumn<Palestrante, String> colEmail;
    @FXML private TableColumn<Palestrante, String> colTelefone;
    @FXML private TableColumn<Palestrante, String> colEspecializacao;

    @FXML
    public void initialize() {
        configurarColunas();
        atualizarTabela();
    }

    private void configurarColunas() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colEspecializacao.setCellValueFactory(new PropertyValueFactory<>("areaEspecializacao"));
    }

    private void atualizarTabela() {
        tabelaPalestrantes.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorPalestrante().listar()
        ));
    }

    @FXML
    private void editar() {
        Palestrante palestranteSelecionado = tabelaPalestrantes.getSelectionModel().getSelectedItem();

        if (palestranteSelecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Atenção", "Selecione um palestrante para editar.");
            return;
        }

        ScreenManager.getInstance().carregarTelaEdicao(
                "TelaCadastroPalestrante.fxml",
                "Editar Palestrante",
                palestranteSelecionado
        );
    }

    @FXML
    private void novoPalestrante() {
        ScreenManager.getInstance().carregarTela("TelaCadastroPalestrante.fxml", "Novo Palestrante");
    }

    @FXML
    private void excluir() {
        Palestrante selecionado = tabelaPalestrantes.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Atenção", "Selecione um palestrante para excluir.");
            return;
        }

        try {
            SistemaSGA.getInstance().getControladorPalestrante().remover(selecionado.getEmail());

            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Palestrante removido.");
            atualizarTabela();

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Erro", "Não foi possível excluir: " + e.getMessage());
        }
    }

    @FXML
    private void voltar() {
        ScreenManager.getInstance().carregarTela("TelaPrincipal.fxml", "Menu Principal");
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}