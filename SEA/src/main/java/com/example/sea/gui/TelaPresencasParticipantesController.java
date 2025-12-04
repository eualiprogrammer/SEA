package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Atividade;
import com.example.sea.model.Inscricao;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TelaPresencasParticipantesController {

    @FXML private Label lblTituloPalestra;
    @FXML private TableView<Inscricao> tabelaInscritos;
    @FXML private TableColumn<Inscricao, String> colParticipante;
    @FXML private TableColumn<Inscricao, String> colStatus;

    private Atividade atividadeAtual;

    public void setAtividade(Atividade atividade) {
        this.atividadeAtual = atividade;
        if (atividade != null) {
            lblTituloPalestra.setText(atividade.getTitulo());
            carregarInscritos();
        }
    }

    public void setPalestra(Atividade atividade) {
        setAtividade(atividade);
    }

    @FXML
    public void initialize() {
        configurarColunas();
    }

    private void configurarColunas() {
        colParticipante.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getParticipante().getNome()));

        colStatus.setCellValueFactory(cellData -> {
            boolean presente = cellData.getValue().isPresenca();
            return new SimpleStringProperty(presente ? "PRESENTE ✅" : "Ausente ❌");
        });
    }

    private void carregarInscritos() {
        if (atividadeAtual != null) {
            tabelaInscritos.setItems(FXCollections.observableArrayList(
                    SistemaSGA.getInstance().getControladorInscricao().listarPorAtividade(atividadeAtual)
            ));
        }
    }

    @FXML
    private void confirmarPresenca() {
        Inscricao inscricaoSelecionada = tabelaInscritos.getSelectionModel().getSelectedItem();

        if (inscricaoSelecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um aluno.");
            return;
        }

        try {
            SistemaSGA.getInstance().getControladorInscricao().marcarPresenca(inscricaoSelecionada);
            tabelaInscritos.refresh();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Presença confirmada!");

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void voltar() {
        ScreenManager.getInstance().carregarTela("admin_presencas_list.fxml", "Gerenciar Presenças");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}