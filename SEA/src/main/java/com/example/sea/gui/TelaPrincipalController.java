package com.example.sea.gui;

import com.example.sea.business.SessaoUsuario;
import javafx.fxml.FXML;

public class TelaPrincipalController {

    @FXML
    private void irParaPalestrantes() {
        ScreenManager.getInstance().carregarTela("TelaListarPalestrantes.fxml", "Gerenciar Palestrantes");
    }

    @FXML
    private void irParaSalas() {
        ScreenManager.getInstance().carregarTela("TelaListarSalas.fxml", "Gerenciar Salas");
    }

    @FXML
    private void irParaEventos() {
        ScreenManager.getInstance().carregarTela("TelaListarEventos.fxml", "Gerenciar Eventos");
    }

    @FXML
    private void irParaPalestras() {
        ScreenManager.getInstance().carregarTela("TelaListarPalestras.fxml", "Gerenciar Palestras");
    }

    @FXML
    private void irParaInscricoes() {
        ScreenManager.getInstance().carregarTela("TelaListarInscricoes.fxml", "Gerenciar Inscrições");
    }

    @FXML
    private void irParaPresencas() {
        ScreenManager.getInstance().carregarTela("admin_presencas_list.fxml", "Gerenciar Presenças");
    }

    @FXML
    private void irParaWorkshops() {
        ScreenManager.getInstance().carregarTela("TelaListarWorkshops.fxml", "Gerenciar Workshops");
    }

    @FXML
    private void sair() {
        SessaoUsuario.getInstance().logout();
        ScreenManager.getInstance().carregarTela("TelaLogin.fxml", "Login");
    }
}