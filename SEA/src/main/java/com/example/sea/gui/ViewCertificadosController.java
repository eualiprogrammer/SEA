package com.example.sea.gui;

import com.example.sea.business.SessaoUsuario;
import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Certificado;
import com.example.sea.model.Participante;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewCertificadosController {

    @FXML
    private VBox containerCertificados;

    @FXML
    public void initialize() {
        carregarCertificados();
    }

    private void carregarCertificados() {
        containerCertificados.getChildren().clear();

        Participante logado = SessaoUsuario.getInstance().getParticipanteLogado();
        if (logado == null) {
            ScreenManager.getInstance().carregarTela("TelaLogin.fxml", "Login");
            return;
        }

        List<Certificado> certificados = SistemaSGA.getInstance()
                .getControladorCertificado()
                .listarPorParticipante(logado);

        if (certificados.isEmpty()) {
            Label emptyLabel = new Label("Nenhum certificado disponível ainda.");
            emptyLabel.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px;");
            containerCertificados.getChildren().add(emptyLabel);
            return;
        }

        for (Certificado cert : certificados) {
            containerCertificados.getChildren().add(criarCardCertificado(cert));
        }
    }

    private VBox criarCardCertificado(Certificado cert) {
        //Configuração Visual
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-color: #1E2130; -fx-padding: 20; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 15, 0, 0, 5); -fx-border-color: rgba(255,255,255,0.05); -fx-border-width: 1;");

        //Dados
        String nomeAtividade = cert.getInscricao().getAtividade().getTitulo();
        String codigo = cert.getCodigoValidacao();

        //Data
        String dataEvento = "Data: A definir";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (cert.getInscricao().getAtividade() instanceof com.example.sea.model.Palestra) {
            com.example.sea.model.Palestra p = (com.example.sea.model.Palestra) cert.getInscricao().getAtividade();
            if (p.getDataHoraInicio() != null) {
                dataEvento = "Data: " + p.getDataHoraInicio().format(fmt);
            }
        } else {
            dataEvento = "Workshop/Atividade Prática";
        }

        //Título
        Label lblTitulo = new Label("🎓 " + nomeAtividade);
        lblTitulo.setStyle("-fx-text-fill: #D946EF; -fx-font-size: 20px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(217, 70, 239, 0.3), 10, 0, 0, 0);");

        // Detalhes
        String detalhesTexto = dataEvento + "\n🔑 Código de Validação: " + codigo;
        Label lblDetalhes = new Label(detalhesTexto);
        lblDetalhes.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 13px; -fx-font-family: 'Consolas', monospace;");
        lblDetalhes.setWrapText(true);

        //Botão Gerar PDF
        Button btnPdf = new Button("📥 Baixar PDF");
        btnPdf.getStyleClass().add("btn-acao");
        btnPdf.setStyle("-fx-background-color: linear-gradient(to right, #8B5CF6, #D946EF); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 30;");

        btnPdf.setOnAction(e -> salvarPdf(cert));

        VBox infoBox = new VBox(5, lblTitulo, lblDetalhes);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        HBox linhaBtn = new HBox(btnPdf);
        linhaBtn.setAlignment(Pos.CENTER_RIGHT);

        HBox linhaPrincipal = new HBox(10);
        linhaPrincipal.setAlignment(Pos.CENTER_LEFT);
        linhaPrincipal.getChildren().addAll(infoBox, linhaBtn);

        card.getChildren().add(linhaPrincipal);
        return card;
    }

    private void salvarPdf(Certificado cert) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Certificado");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Certificado_" + cert.getCodigoValidacao() + ".pdf");
        File file = fileChooser.showSaveDialog(containerCertificados.getScene().getWindow());

        if (file != null) {
            gerarArquivoPdf(cert, file);
        }
    }

    private void gerarArquivoPdf(Certificado cert, File destino) {
        try {
            PdfWriter writer = new PdfWriter(destino);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            //DEFINIÇÃO DE CORES
            Color corFundo = new DeviceRgb(15, 17, 26);
            Color corDourado = new DeviceRgb(250, 204, 21);
            Color corRoxo = new DeviceRgb(139, 92, 246);
            Color corTexto = new DeviceRgb(226, 232, 240);

            //FONTE
            PdfFont fontTimes = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);

            //FUNDO E BORDA
            PdfPage page = pdf.addNewPage();
            PdfCanvas canvas = new PdfCanvas(page);

            canvas.saveState();
            canvas.setFillColor(corFundo);
            canvas.rectangle(0, 0, pdf.getDefaultPageSize().getWidth(), pdf.getDefaultPageSize().getHeight());
            canvas.fill();
            canvas.restoreState();

            canvas.saveState();
            canvas.setStrokeColor(corDourado);
            canvas.setLineWidth(3);
            canvas.rectangle(20, 20, pdf.getDefaultPageSize().getWidth() - 40, pdf.getDefaultPageSize().getHeight() - 40);
            canvas.stroke();
            canvas.restoreState();

            //CONTEÚDO

            document.add(new Paragraph("\n\n"));

            // Título
            document.add(new Paragraph("CERTIFICADO")
                    .setFont(fontTimes)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(40)
                    .setBold()
                    .setFontColor(corDourado));

            document.add(new Paragraph("DE PARTICIPAÇÃO")
                    .setFont(fontTimes)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18)
                    .setBold()
                    .setCharacterSpacing(5)
                    .setFontColor(corRoxo));

            document.add(new Paragraph("\n\n\n"));

            // Texto
            String nomeAtividade = cert.getInscricao().getAtividade().getTitulo().toUpperCase();
            String complementoTexto = "";

            if (cert.getInscricao().getAtividade() instanceof com.example.sea.model.Palestra) {
                com.example.sea.model.Palestra p = (com.example.sea.model.Palestra) cert.getInscricao().getAtividade();
                if (p.getDataHoraInicio() != null) {
                    complementoTexto = " realizada em " + p.getDataHoraInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } else {
                    complementoTexto = " realizada em data a definir";
                }
            } else {
                complementoTexto = " concluída com êxito";
            }

            String nomeParticipante = cert.getInscricao().getParticipante().getNome().toUpperCase();

            String texto = "Certificamos que " + nomeParticipante +
                    " participou da atividade " + nomeAtividade +
                    complementoTexto + ".";

            document.add(new Paragraph(texto)
                    .setFont(fontTimes)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.JUSTIFIED)
                    .setFontColor(corTexto)
                    .setMarginLeft(60)
                    .setMarginRight(60));

            document.add(new Paragraph("\n\n\n\n"));

            // Rodapé
            document.add(new Paragraph("Autenticidade do Documento")
                    .setFont(fontTimes)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(corDourado));

            document.add(new Paragraph(cert.getCodigoValidacao())
                    .setFont(fontTimes)
                    .setFontSize(12)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(corTexto));

            document.close();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "PDF gerado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Falha ao gerar PDF: " + e.getMessage());
        }
    }

    @FXML
    private void voltar() {
        ScreenManager.getInstance().carregarTela("view_eventos.fxml", "Área do Aluno");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}