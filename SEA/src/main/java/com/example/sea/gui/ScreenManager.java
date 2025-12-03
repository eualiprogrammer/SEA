package com.example.sea.gui;

import com.example.sea.model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ScreenManager {

    private static final int LARGURA = 1280;
    private static final int ALTURA = 720;
    private static ScreenManager instance;
    private Stage primaryStage;
    private ScreenManager() {}

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void carregarTela(String nomeArquivoFxml, String titulo) {
        try {
            String caminho = "/com/example/sea/" + nomeArquivoFxml;
            URL fxmlUrl = getClass().getResource(caminho);

            if (fxmlUrl == null) {
                System.err.println("ERRO CRÍTICO: Arquivo FXML não encontrado: " + caminho);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);

            URL cssUrl = getClass().getResource("/com/example/sea/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("AVISO: styles.css não encontrado. Carregando sem estilo.");
            }
            configurarPalco(scene, titulo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void carregarTelaEdicao(String nomeArquivoFxml, String titulo, Object objetoParaEditar) {
        try {
            String caminho = "/com/example/sea/" + nomeArquivoFxml;
            URL fxmlUrl = getClass().getResource(caminho);

            if (fxmlUrl == null) {
                System.err.println("ERRO CRÍTICO: Arquivo FXML de edição não encontrado: " + caminho);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Object controller = loader.getController();

            if (controller instanceof TelaCadastroInscricaoController && objetoParaEditar instanceof Inscricao) {
                ((TelaCadastroInscricaoController) controller).setInscricao((Inscricao) objetoParaEditar);
            }
            else if (controller instanceof TelaCadastroSalaController && objetoParaEditar instanceof Sala) {
                ((TelaCadastroSalaController) controller).setSala((Sala) objetoParaEditar);
            }
            else if (controller instanceof TelaCadastroEventoController && objetoParaEditar instanceof Evento) {
                ((TelaCadastroEventoController) controller).setEvento((Evento) objetoParaEditar);
            }
            else if (controller instanceof TelaCadastroPalestraController && objetoParaEditar instanceof Palestra) {
                ((TelaCadastroPalestraController) controller).setPalestra((Palestra) objetoParaEditar);
            }
            else if (controller instanceof TelaCadastroPalestranteController && objetoParaEditar instanceof Palestrante) {
                ((TelaCadastroPalestranteController) controller).setPalestrante((Palestrante) objetoParaEditar);
            }
            else if (controller instanceof TelaPresencasParticipantesController && objetoParaEditar instanceof Palestra) {
                ((TelaPresencasParticipantesController) controller).setPalestra((Palestra) objetoParaEditar);
            }
            else if (controller instanceof TelaCadastroWorkshopController && objetoParaEditar instanceof com.example.sea.model.Workshop) {
                ((TelaCadastroWorkshopController) controller).setWorkshop((com.example.sea.model.Workshop) objetoParaEditar);
            }
            else if (controller instanceof TelaGerenciarPalestrasWorkshopController && objetoParaEditar instanceof com.example.sea.model.Workshop) {
                ((TelaGerenciarPalestrasWorkshopController) controller).setWorkshop((com.example.sea.model.Workshop) objetoParaEditar);
            }
            else if (controller instanceof TelaPresencasParticipantesController && objetoParaEditar instanceof com.example.sea.model.Atividade) {
                ((TelaPresencasParticipantesController) controller).setAtividade((com.example.sea.model.Atividade) objetoParaEditar);
            }

            Scene scene = new Scene(root);

            URL cssUrl = getClass().getResource("/com/example/sea/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
            configurarPalco(scene, titulo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configurarPalco(Scene scene, String titulo) {
        if (primaryStage != null) {
            primaryStage.setScene(scene);
            primaryStage.setTitle("SGA - " + titulo);
            primaryStage.setWidth(LARGURA);
            primaryStage.setHeight(ALTURA);
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.show();
        }
    }
}