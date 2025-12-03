package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Atividade; // <--- USAR ATIVIDADE (Genérico)
import com.example.sea.model.Palestra;
import com.example.sea.model.Workshop;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TelaListarPresencasController {

    @FXML private TableView<Atividade> tabelaPalestras;
    @FXML private TableColumn<Atividade, String> colNome;
    @FXML private TableColumn<Atividade, String> colPalestrante;
    @FXML private TableColumn<Atividade, String> colSala;
    @FXML private TableColumn<Atividade, String> colData;

    @FXML
    public void initialize() {
        configurarColunas();
        carregarAtividades();
    }

    private void configurarColunas() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        colPalestrante.setCellValueFactory(cellData -> {
            Atividade a = cellData.getValue();
            if (a instanceof Palestra) return new SimpleStringProperty(((Palestra) a).getPalestrante().getNome());
            return new SimpleStringProperty("-");
        });

        colSala.setCellValueFactory(cellData -> {
            Atividade a = cellData.getValue();
            if (a instanceof Palestra) return new SimpleStringProperty(((Palestra) a).getSala().getNome());
            return new SimpleStringProperty("Múltiplas/Online");
        });

        colData.setCellValueFactory(cellData -> {
            Atividade a = cellData.getValue();
            if (a instanceof Palestra) {
                Palestra p = (Palestra) a;
                if (p.getDataHoraInicio() != null) {
                    return new SimpleStringProperty(p.getDataHoraInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                }
            }
            return new SimpleStringProperty("Ver Detalhes");
        });
    }

    private void carregarAtividades() {
        List<Atividade> todas = new ArrayList<>();
        todas.addAll(SistemaSGA.getInstance().getControladorPalestra().listarTodos());
        todas.addAll(SistemaSGA.getInstance().getControladorWorkshop().listar());

        tabelaPalestras.setItems(FXCollections.observableArrayList(todas));
    }

    @FXML
    private void gerenciarParticipantes() {
        Atividade selecionada = tabelaPalestras.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma atividade.");
            return;
        }

        ScreenManager.getInstance().carregarTelaEdicao(
                "admin_presencas_participantes.fxml",
                "Lista de Presença - " + selecionada.getTitulo(),
                selecionada
        );
    }

    @FXML
    private void voltar() {
        ScreenManager.getInstance().carregarTela("TelaPrincipal.fxml", "Menu Principal");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}