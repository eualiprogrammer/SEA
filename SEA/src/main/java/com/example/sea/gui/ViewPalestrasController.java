package com.example.sea.gui;

import com.example.sea.business.SessaoUsuario;
import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Evento;
import com.example.sea.model.Palestra;
import com.example.sea.model.Participante;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewPalestrasController {

    @FXML
    private VBox containerPalestras;

    @FXML
    public void initialize() {
        carregarPalestras();
    }

    private void carregarPalestras() {
        containerPalestras.getChildren().clear();

        Evento eventoFiltro = SessaoUsuario.getInstance().getEventoSelecionado();

        List<Palestra> todasPalestras = SistemaSGA.getInstance().getControladorPalestra().listarTodos();

        boolean encontrouAlguma = false;

        for (Palestra palestra : todasPalestras) {
            if (eventoFiltro != null && palestra.getEvento().getNome().equals(eventoFiltro.getNome())) {
                containerPalestras.getChildren().add(criarCardPalestra(palestra));
                encontrouAlguma = true;
            }
        }

        if (!encontrouAlguma) {
            containerPalestras.getChildren().add(new Label("Nenhuma palestra encontrada para este evento."));
        }
    }

    private VBox criarCardPalestra(Palestra palestra) {
        //Configuração Visual do Card
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-color: #1E2130; -fx-padding: 15; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0); -fx-border-color: rgba(255,255,255,0.1); -fx-border-width: 1; -fx-border-radius: 10;");

        //Título da Palestra
        Label lblTitulo = new Label(palestra.getTitulo());
        lblTitulo.setStyle("-fx-text-fill: #8B5CF6; -fx-font-size: 20px; -fx-font-weight: bold;"); // Roxo Neon

        //Montagem das Informações Detalhadas
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        String dataStr = palestra.getDataHoraInicio() != null ? palestra.getDataHoraInicio().format(fmt) : "A definir";

        String nomeEvento = palestra.getEvento() != null ? palestra.getEvento().getNome() : "Evento não informado";
        String nomeSala = palestra.getSala() != null ? palestra.getSala().getNome() : "Sala não definida";
        int capacidade = palestra.getSala() != null ? palestra.getSala().getCapacidade() : 0;
        String nomePalestrante = palestra.getPalestrante() != null ? palestra.getPalestrante().getNome() : "A definir";
        String emailPalestrante = palestra.getPalestrante() != null ? palestra.getPalestrante().getEmail() : "";

        StringBuilder info = new StringBuilder();
        info.append("📅 Evento: ").append(nomeEvento).append("\n");
        info.append("🎤 Palestrante: ").append(nomePalestrante).append(" (").append(emailPalestrante).append(")\n");
        info.append("📍 Local: ").append(nomeSala).append(" (Capacidade: ").append(capacidade).append(" pessoas)\n");
        info.append("⏰ Data: ").append(dataStr).append("  |  ⏳ Duração: ").append(palestra.getDuracaoHoras()).append("h\n");
        info.append("\n📝 Sobre a atividade:\n").append(palestra.getDescricao());

        Label lblInfo = new Label(info.toString());
        lblInfo.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 13px;");
        lblInfo.setWrapText(true);

        //Botão de Ação
        Button btnInscrever = new Button("Inscrever-se");
        btnInscrever.getStyleClass().add("btn-acao");
        btnInscrever.setOnAction(e -> realizarInscricao(palestra, btnInscrever));

        //Layout
        VBox conteudoTexto = new VBox(5, lblTitulo, lblInfo);
        HBox.setHgrow(conteudoTexto, Priority.ALWAYS);

        HBox linhaInferior = new HBox(btnInscrever);
        linhaInferior.setAlignment(Pos.BOTTOM_RIGHT);

        card.getChildren().addAll(conteudoTexto, linhaInferior);

        return card;
    }

    private void realizarInscricao(Palestra palestra, Button btn) {
        Participante participante = SessaoUsuario.getInstance().getParticipanteLogado();

        if (participante == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Sessão expirada. Faça login novamente.");
            ScreenManager.getInstance().carregarTela("TelaLogin.fxml", "Login");
            return;
        }

        try {
            SistemaSGA.getInstance().getControladorInscricao().inscrever(participante, palestra);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso!", "Você está inscrito em: " + palestra.getTitulo());
            btn.setText("Inscrito ✓");
            btn.setDisable(true);
            btn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Não foi possível inscrever: " + e.getMessage());
        }
    }

    @FXML
    private void voltar() {
        ScreenManager.getInstance().carregarTela("view_eventos.fxml", "Eventos Disponíveis");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }


}