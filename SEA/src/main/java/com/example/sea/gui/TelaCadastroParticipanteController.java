package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Participante;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class TelaCadastroParticipanteController {

    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private TextField txtInstituicao;
    @FXML private TextField txtCpf;
    @FXML private PasswordField txtSenha;

    private Participante participanteEmEdicao;

    public void setParticipante(Participante participante) {
        this.participanteEmEdicao = participante;
        if (participante != null) {
            txtNome.setText(participante.getNome());
            txtEmail.setText(participante.getEmail());
            txtInstituicao.setText(participante.getInstituicao());
            txtCpf.setText(participante.getCpf());
            txtCpf.setDisable(true);
        }
    }

    @FXML
    private void salvar() {
        try {
            String nome = txtNome.getText();
            String email = txtEmail.getText();
            String instituicao = txtInstituicao.getText();
            String cpf = txtCpf.getText();
            String senha = txtSenha.getText();

            if (nome.isEmpty() || email.isEmpty() || cpf.isEmpty() || instituicao.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Preencha todos os campos.");
                return;
            }

            if (participanteEmEdicao != null) {
                participanteEmEdicao.setNome(nome);
                participanteEmEdicao.setEmail(email);
                participanteEmEdicao.setInstituicao(instituicao);

                if (!senha.isEmpty()) {
                    participanteEmEdicao.setSenha(senha);
                }

                SistemaSGA.getInstance().getControladorParticipante().atualizar(participanteEmEdicao);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Dados atualizados!");
                cancelar();

            } else {
                if (senha.isEmpty()) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Crie uma senha para seu acesso.");
                    return;
                }

                Participante novoParticipante = new Participante(nome, email, instituicao, cpf, senha);
                SistemaSGA.getInstance().getControladorParticipante().cadastrar(novoParticipante);

                mostrarAlerta(Alert.AlertType.INFORMATION, "Bem-vindo!", "Cadastro realizado! Faça login.");
                ScreenManager.getInstance().carregarTela("TelaLogin.fxml", "Login");
            }

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar: " + e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        if (participanteEmEdicao == null) {
            ScreenManager.getInstance().carregarTela("TelaLogin.fxml", "Login");
        } else {
            ScreenManager.getInstance().carregarTela("TelaPrincipal.fxml", "Menu Principal");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
