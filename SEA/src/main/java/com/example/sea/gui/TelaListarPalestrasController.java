package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Palestra;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TelaListarPalestrasController {

    @FXML private TableView<Palestra> tabelaPalestras;
    @FXML private TableColumn<Palestra, String> colNome;
    @FXML private TableColumn<Palestra, String> colPalestrante;
    @FXML private TableColumn<Palestra, String> colSala;
    @FXML private TableColumn<Palestra, String> colDescricao;
    @FXML private TableColumn<Palestra, String> colDuracaoHoras;

    @FXML
    public void initialize() {
        configurarColunas();
        atualizarTabela();
    }

    private void configurarColunas() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colPalestrante.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPalestrante().getNome()));
        colSala.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSala().getNome()));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colDuracaoHoras.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getDuracaoHoras())));
    }

    private void atualizarTabela() {
        tabelaPalestras.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorPalestra().listarTodos()
        ));
    }

    @FXML
    private void editar() {
        Palestra palestraSelecionada = tabelaPalestras.getSelectionModel().getSelectedItem();

        if (palestraSelecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma palestra para editar.");
            return;
        }

        ScreenManager.getInstance().carregarTelaEdicao(
                "TelaCadastroPalestra.fxml",
                "Editar Palestra",
                palestraSelecionada
        );
    }

    @FXML
    private void novaPalestra() {
        ScreenManager.getInstance().carregarTela("TelaCadastroPalestra.fxml", "Nova Palestra");
    }

    @FXML
    private void excluir() {
        Palestra palestraSelecionada = tabelaPalestras.getSelectionModel().getSelectedItem();

        if (palestraSelecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma palestra para excluir.");
            return;
        }

        try {
            SistemaSGA.getInstance().getControladorPalestra().remover(palestraSelecionada.getTitulo());
            atualizarTabela();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Palestra removida.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao excluir: " + e.getMessage());
        }
    }

    @FXML
    private void voltar() {
        ScreenManager.getInstance().carregarTela("TelaPrincipal.fxml", "Menu Principal");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
