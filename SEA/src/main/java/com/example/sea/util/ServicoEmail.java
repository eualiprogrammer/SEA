package com.example.sea.util; // Ou o pacote que preferir

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class ServicoEmail {

    private static final String REMETENTE = "sistemsacademicoseventos@gmail.com";
    private static final String SENHA = "syoh eeci osmr jina";

    public static void enviarEmailConfirmacao(String destinatario, String nomeParticipante, String nomePalestra) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMETENTE, SENHA);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(REMETENTE));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Confirmação de Inscrição - SEA");

            String msg = "Olá, " + nomeParticipante + "!\n\n"
                    + "Sua inscrição na atividade '" + nomePalestra + "' foi confirmada com sucesso.\n"
                    + "Nos vemos lá!\n\n"
                    + "Atenciosamente,\nEquipe SEA.";

            message.setText(msg);

            Transport.send(message);
            System.out.println("E-mail de confirmação enviado para: " + destinatario);

        } catch (MessagingException e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}