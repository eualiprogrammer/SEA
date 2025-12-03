package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Sala;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TelaListarSalasController {

    @FXML private TableView<Sala> tabelaSalas;
    @FXML private TableColumn<Sala, String> colNome;
    @FXML private TableColumn<Sala, Integer> colCapacidade;
    @FXML private TableColumn<Sala, Integer> colProjetores;
    @FXML private TableColumn<Sala, Integer> colCaixasDeSom;
    @FXML private TableColumn<Sala, String> colStatus;

    @FXML
    public void initialize() {
        configurarColunas();
        atualizarTabela();
    }

    private void configurarColunas() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidade"));
        colProjetores.setCellValueFactory(new PropertyValueFactory<>("numeroProjetores"));
        colCaixasDeSom.setCellValueFactory(new PropertyValueFactory<>("numeroCaixasSom"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void atualizarTabela() {
        tabelaSalas.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorSala().listar()
        ));
    }

    @FXML
    private void novaSala() {
        ScreenManager.getInstance().carregarTela("TelaCadastroSala.fxml", "Nova Sala");
    }

    @FXML
    private void excluir() {
        Sala selecionada = tabelaSalas.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            mostrarAlerta(AlertType.WARNING, "Atenção", "Selecione uma sala na tabela para excluir.");
            return;
        }

        try {
            SistemaSGA.getInstance().getControladorSala().remover(selecionada.getNome());

            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Sala removida com sucesso!");
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

    @FXML
    private void editar() {
        Sala salaSelecionada = tabelaSalas.getSelectionModel().getSelectedItem();

        if (salaSelecionada == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atenção");
            alert.setHeaderText(null);
            alert.setContentText("Selecione uma sala para editar.");
            alert.showAndWait();
            return;
        }

        ScreenManager.getInstance().carregarTelaEdicao(
                "TelaCadastroSala.fxml",
                "Editar Sala",
                salaSelecionada
        );
    }
}
