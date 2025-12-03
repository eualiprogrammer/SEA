package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Inscricao;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TelaListarInscricoesController {

    @FXML private TableView<Inscricao> tabelaInscricoes;

    @FXML private TableColumn<Inscricao, String> colParticipante;
    @FXML private TableColumn<Inscricao, String> colPalestra; // Nome da Atividade
    @FXML private TableColumn<Inscricao, String> colConfirmacao;
    @FXML private TableColumn<Inscricao, String> colDataHoraInscricao;

    @FXML
    public void initialize() {
        configurarColunas();
        atualizarTabela();
    }

    private void configurarColunas() {
        colParticipante.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getParticipante().getNome())
        );

        colPalestra.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAtividade().getTitulo())
        );

        colConfirmacao.setCellValueFactory(new PropertyValueFactory<>("statusConfirmacao"));

        colDataHoraInscricao.setCellValueFactory(cellData -> {
            LocalDateTime data = cellData.getValue().getDataHoraInscricao();
            if (data != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                return new SimpleStringProperty(data.format(formatter));
            }
            return new SimpleStringProperty("");
        });
    }

    private void atualizarTabela() {
        tabelaInscricoes.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorInscricao().listarTodos()
        ));
        tabelaInscricoes.refresh();
    }

    @FXML
    private void novaInscricao() {
        ScreenManager.getInstance().carregarTela("TelaCadastroInscricao.fxml", "Nova Inscrição");
    }

    @FXML
    private void editar() {
        Inscricao inscricaoSelecionada = tabelaInscricoes.getSelectionModel().getSelectedItem();

        if (inscricaoSelecionada == null) {
            mostrarAlerta(AlertType.WARNING, "Atenção", "Selecione uma inscrição para editar.");
            return;
        }

        ScreenManager.getInstance().carregarTelaEdicao(
                "TelaCadastroInscricao.fxml",
                "Editar Inscrição",
                inscricaoSelecionada
        );
    }

    @FXML
    private void excluir() {
        Inscricao inscricaoSelecionada = tabelaInscricoes.getSelectionModel().getSelectedItem();

        if (inscricaoSelecionada == null) {
            mostrarAlerta(AlertType.WARNING, "Atenção", "Selecione uma inscrição para cancelar.");
            return;
        }

        try {
            SistemaSGA.getInstance().getControladorInscricao().cancelarInscricao(inscricaoSelecionada);
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Inscrição cancelada com sucesso!");
            atualizarTabela();

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Erro", "Não foi possível cancelar: " + e.getMessage());
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