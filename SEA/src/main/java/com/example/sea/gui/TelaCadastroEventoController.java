package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Evento;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class TelaCadastroEventoController {

    @FXML private TextField txtNome;
    @FXML private DatePicker dpDataInicio;
    @FXML private DatePicker dpDataFim;
    @FXML private TextArea txtDescricao;

    private Evento eventoEmEdicao;

    public void setEvento(Evento evento) {
        this.eventoEmEdicao = evento;
        if (evento != null) {
            txtNome.setText(evento.getNome());
            dpDataInicio.setValue(evento.getDataInicio());
            dpDataFim.setValue(evento.getDataFim());
            txtDescricao.setText(evento.getDescricao());
        }
    }

    @FXML
    private void salvar() {
        try {
            String nome = txtNome.getText();
            String descricao = txtDescricao.getText();

            if (dpDataInicio.getValue() == null || dpDataFim.getValue() == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione as datas.");
                return;
            }

            LocalDate dataInicio = dpDataInicio.getValue();
            LocalDate dataFim = dpDataFim.getValue();

            if (eventoEmEdicao != null) {
                eventoEmEdicao.setNome(nome);
                eventoEmEdicao.setDataInicio(dataInicio);
                eventoEmEdicao.setDataFim(dataFim);
                eventoEmEdicao.setDescricao(descricao);

                SistemaSGA.getInstance().getControladorEvento().atualizar(eventoEmEdicao);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Evento atualizado!");

            } else {
                Evento novoEvento = new Evento(nome, dataInicio, dataFim, descricao);

                SistemaSGA.getInstance().getControladorEvento().cadastrar(novoEvento);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Evento cadastrado!");
            }
            cancelar();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        ScreenManager.getInstance().carregarTela("TelaListarEventos.fxml", "Eventos");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
