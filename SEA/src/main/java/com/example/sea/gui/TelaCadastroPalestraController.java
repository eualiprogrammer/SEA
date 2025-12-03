package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Evento;
import com.example.sea.model.Palestra;
import com.example.sea.model.Palestrante;
import com.example.sea.model.Sala;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class TelaCadastroPalestraController {

    @FXML private TextField txtNome;
    @FXML private ChoiceBox<Evento> cbEvento;
    @FXML private ChoiceBox<Sala> cbSala;
    @FXML private ChoiceBox<Palestrante> cbPalestrante;
    @FXML private DatePicker dpDataInicio;
    @FXML private TextField txtDuracaoHoras;
    @FXML private TextArea txtDescricao;
    @FXML private TextField txtHora;

    private Palestra palestraEmEdicao;

    @FXML
    public void initialize() {
        carregarListas();
    }

    private void carregarListas() {
        cbEvento.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorEvento().listar()));
        cbSala.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorSala().listar()));
        cbPalestrante.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorPalestrante().listar()));
    }

    public void setPalestra(Palestra palestra) {
        this.palestraEmEdicao = palestra;
        if (palestra != null) {
            txtNome.setText(palestra.getTitulo());
            txtDescricao.setText(palestra.getDescricao());
            txtDuracaoHoras.setText(String.valueOf(palestra.getDuracaoHoras()));

            if (palestra.getDataHoraInicio() != null) {
                dpDataInicio.setValue(palestra.getDataHoraInicio().toLocalDate());
                txtHora.setText(palestra.getDataHoraInicio().toLocalTime().toString());
            }

            cbEvento.setValue(palestra.getEvento());
            cbSala.setValue(palestra.getSala());
            cbPalestrante.setValue(palestra.getPalestrante());

            txtNome.setDisable(true);
        }
    }

    @FXML
    private void salvar() {
        try {
            String nome = txtNome.getText();
            String descricao = txtDescricao.getText();
            String duracaoStr = txtDuracaoHoras.getText();
            String horaStr = txtHora.getText();
            LocalDate data = dpDataInicio.getValue();

            Evento evento = cbEvento.getValue();
            Sala sala = cbSala.getValue();
            Palestrante palestrante = cbPalestrante.getValue();

            if (nome == null || nome.trim().isEmpty()) throw new IllegalArgumentException("O nome da palestra é obrigatório.");
            if (data == null) throw new IllegalArgumentException("Selecione a data de início.");
            if (horaStr == null || horaStr.trim().isEmpty()) throw new IllegalArgumentException("Digite o horário (Ex: 14:00).");
            if (duracaoStr == null || duracaoStr.trim().isEmpty()) throw new IllegalArgumentException("Digite a duração em horas.");
            if (evento == null || sala == null || palestrante == null) throw new IllegalArgumentException("Preencha todos os campos de seleção (Evento, Sala, Palestrante).");

            float duracao = Float.parseFloat(duracaoStr);
            LocalTime hora = LocalTime.parse(horaStr);
            LocalDateTime dataHoraInicio = LocalDateTime.of(data, hora);

            if (palestraEmEdicao != null) {
                palestraEmEdicao.setDescricao(descricao);
                palestraEmEdicao.setDuracaoHoras(duracao);
                palestraEmEdicao.setDataHoraInicio(dataHoraInicio);
                palestraEmEdicao.setEvento(evento);
                palestraEmEdicao.setSala(sala);
                palestraEmEdicao.setPalestrante(palestrante);

                SistemaSGA.getInstance().getControladorPalestra().atualizar(palestraEmEdicao);
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Palestra atualizada com sucesso!");

            } else {
                SistemaSGA.getInstance().getControladorPalestra().cadastrar(
                        nome, descricao, evento, dataHoraInicio, duracao, sala, palestrante
                );
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Palestra cadastrada com sucesso!");
            }
            cancelar();

        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.ERROR, "Erro de Formato", "A duração deve ser um número (ex: 1.5 ou 2).");
        } catch (DateTimeParseException e) {
            mostrarAlerta(AlertType.ERROR, "Erro de Data/Hora", "Formato de hora inválido. Use HH:mm (ex: 13:30).");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Erro ao Salvar", e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        ScreenManager.getInstance().carregarTela("TelaListarPalestras.fxml", "Palestras");
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}