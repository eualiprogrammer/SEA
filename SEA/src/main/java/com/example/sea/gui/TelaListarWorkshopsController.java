package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Workshop;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TelaListarWorkshopsController {

    @FXML private TableView<Workshop> tabelaWorkshops;
    @FXML private TableColumn<Workshop, String> colTitulo;
    @FXML private TableColumn<Workshop, String> colEvento;
    @FXML private TableColumn<Workshop, String> colDescricao;

    @FXML
    public void initialize() {
        configurarColunas();
        atualizarTabela();
    }

    private void configurarColunas() {
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        colEvento.setCellValueFactory(cellData -> {
            if (cellData.getValue().getEvento() != null) {
                return new SimpleStringProperty(cellData.getValue().getEvento().getNome());
            }
            return new SimpleStringProperty("-");
        });
    }

    private void atualizarTabela() {
        tabelaWorkshops.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorWorkshop().listar()
        ));
    }

    @FXML
    private void novoWorkshop() {
        ScreenManager.getInstance().carregarTela("TelaCadastroWorkshop.fxml", "Novo Workshop");
    }

    @FXML
    private void editar() {
        Workshop selecionado = tabelaWorkshops.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um workshop para editar.");
            return;
        }
        ScreenManager.getInstance().carregarTelaEdicao(
                "TelaCadastroWorkshop.fxml", "Editar Workshop", selecionado
        );
    }

    @FXML
    private void excluir() {
        Workshop selecionado = tabelaWorkshops.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um workshop para excluir.");
            return;
        }
        try {
            SistemaSGA.getInstance().getControladorWorkshop().remover(selecionado.getTitulo());
            atualizarTabela(); // Recarrega a lista visual
            mostrarAlerta(Alert.AlertType.INFORMATION, "Workshop removido com sucesso.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao excluir: " + e.getMessage());
        }
    }

    @FXML
    private void voltar() {
        ScreenManager.getInstance().carregarTela("TelaPrincipal.fxml", "Menu Principal");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String msg) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void gerenciarPalestras() {
        Workshop selecionado = tabelaWorkshops.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um workshop.");
            return;
        }
        ScreenManager.getInstance().carregarTelaEdicao(
                "admin_workshop_palestras.fxml",
                "Conteúdo do Workshop",
                selecionado
        );
    }
}