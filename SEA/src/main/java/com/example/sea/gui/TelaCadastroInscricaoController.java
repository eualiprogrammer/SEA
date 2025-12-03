package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Inscricao;
import com.example.sea.model.Palestra;
import com.example.sea.model.Participante;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;

public class TelaCadastroInscricaoController {

    @FXML
    private ChoiceBox<Participante> choiceParticipante;

    @FXML
    private ChoiceBox<Palestra> choicePalestra;

    // Variável para controlar se estamos editando
    private Inscricao inscricaoEmEdicao;

    @FXML
    public void initialize() {
        carregarDadosChoiceBox();
    }

    private void carregarDadosChoiceBox() {
        choiceParticipante.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorParticipante().listarTodos()
        ));

        choicePalestra.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorPalestra().listarTodos()
        ));
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricaoEmEdicao = inscricao;

        if (inscricao != null) {
            choiceParticipante.setValue(inscricao.getParticipante());

            if (inscricao.getAtividade() instanceof Palestra) {
                choicePalestra.setValue((Palestra) inscricao.getAtividade());
            }
        }
    }

    @FXML
    private void salvar() {
        try {
            Participante participante = choiceParticipante.getValue();
            Palestra palestra = choicePalestra.getValue();

            if (participante == null || palestra == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos Obrigatórios", "Selecione um participante e uma palestra.");
                return;
            }

            if (inscricaoEmEdicao != null) {
                inscricaoEmEdicao.setParticipante(participante);

                inscricaoEmEdicao.setAtividade(palestra);

                SistemaSGA.getInstance().getControladorInscricao().atualizar(inscricaoEmEdicao);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Inscrição atualizada com sucesso!");
            } else {
                Inscricao novaInscricao = new Inscricao(participante, palestra);

                SistemaSGA.getInstance().getControladorInscricao().cadastrar(novaInscricao);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Inscrição realizada com sucesso!");
            }

            voltar();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar: " + e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        voltar();
    }

    private void voltar() {
        ScreenManager.getInstance().carregarTela("TelaListarInscricoes.fxml", "Inscrições");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}