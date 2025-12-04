package com.example.sea.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class ServicoEmail {

    private static final String REMETENTE = "sistemsacademicoseventos@gmail.com";
    private static final String SENHA = "syoh eeci osmr jina";

    private static Session criarSessao() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMETENTE, SENHA);
            }
        });
    }

    public static void enviarEmailConfirmacao(String destinatario, String nomeParticipante, String nomePalestra) {
        enviarEmail(destinatario, "Confirmação de Inscrição",
                "Olá, " + nomeParticipante + "!\n\n"
                        + "Sua inscrição na palestra \"" + nomePalestra + "\" foi confirmada com sucesso.\n\n"
                        + "Atenciosamente,\nEquipe SEA.");
    }

    public static void enviarEmailConfirmacaoPalestrante(String destinatario, String nomePalestrante, String tituloPalestra, LocalDateTime dataHora) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        String dataFormatada = dataHora.format(formatter);

        String assunto = "Cadastro de Palestra Confirmado - SEA";
        String corpo = "Olá, " + nomePalestrante + ".\n\n"
                + "Você foi cadastrado como palestrante da atividade: \"" + tituloPalestra + "\".\n"
                + "Data e Horário: " + dataFormatada + "\n\n"
                + "Atenciosamente,\nEquipe SEA.";

        enviarEmail(destinatario, assunto, corpo);
    }

    private static void enviarEmail(String destinatario, String assunto, String corpo) {
        try {
            Message message = new MimeMessage(criarSessao());
            message.setFrom(new InternetAddress(REMETENTE));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);
            message.setText(corpo);
            Transport.send(message);
            System.out.println(">>> E-mail enviado para: " + destinatario);
        } catch (MessagingException e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}
