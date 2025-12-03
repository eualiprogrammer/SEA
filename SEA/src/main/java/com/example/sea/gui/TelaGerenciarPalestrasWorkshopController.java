package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Palestra;
import com.example.sea.model.Workshop;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class TelaGerenciarPalestrasWorkshopController {

    @FXML private Label lblTituloWorkshop;
    @FXML private TableView<Palestra> tabelaPalestrasIncluidas;
    @FXML private TableColumn<Palestra, String> colTitulo;
    @FXML private TableColumn<Palestra, String> colPalestrante;

    @FXML private ChoiceBox<Palestra> cbPalestrasDisponiveis;

    private Workshop workshopAtual;

    public void setWorkshop(Workshop workshop) {
        this.workshopAtual = workshop;
        if (workshop != null) {
            lblTituloWorkshop.setText(workshop.getTitulo() + " (" + workshop.getEvento().getNome() + ")");
            atualizarTudo();
        }
    }

    @FXML
    public void initialize() {
        configurarTabela();
    }

    private void configurarTabela() {
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colPalestrante.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPalestrante().getNome()));
    }

    private void atualizarTudo() {
        if (workshopAtual == null) return;

        tabelaPalestrasIncluidas.setItems(FXCollections.observableArrayList(
                workshopAtual.getPalestrasDoWorkshop()
        ));

        List<Palestra> todas = SistemaSGA.getInstance().getControladorPalestra().listarTodos();
        List<Palestra> disponiveis = new ArrayList<>();

        for (Palestra p : todas) {
            boolean jaTem = workshopAtual.getPalestrasDoWorkshop().contains(p);

            boolean mesmoEvento = p.getEvento().getNome().equals(workshopAtual.getEvento().getNome());

            if (!jaTem && mesmoEvento) {
                disponiveis.add(p);
            }
        }

        cbPalestrasDisponiveis.setItems(FXCollections.observableArrayList(disponiveis));
    }

    @FXML
    private void adicionarPalestra() {
        Palestra selecionada = cbPalestrasDisponiveis.getValue();
        if (selecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione uma palestra para adicionar.");
            return;
        }

        try {
            SistemaSGA.getInstance().getControladorWorkshop()
                    .adicionarPalestraAoWorkshop(workshopAtual.getTitulo(), selecionada);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Palestra adicionada com sucesso!");
            atualizarTudo();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao adicionar: " + e.getMessage());
        }
    }

    @FXML
    private void removerPalestra() {
        Palestra selecionada = tabelaPalestrasIncluidas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione uma palestra da lista para remover.");
            return;
        }

        try {
            SistemaSGA.getInstance().getControladorWorkshop()
                    .removerPalestraDoWorkshop(workshopAtual.getTitulo(), selecionada);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Palestra removida do workshop.");
            atualizarTudo();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao remover: " + e.getMessage());
        }
    }

    @FXML
    private void voltar() {
        ScreenManager.getInstance().carregarTela("TelaListarWorkshops.fxml", "Gestão de Workshops");
    }

    private void mostrarAlerta(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}