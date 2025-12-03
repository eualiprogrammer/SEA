package com.example.sea;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MainTeste {
    public static void main(String[] args) {

        try {
            Sala sala1 = new Sala("Auditório Principal", 50, 2, 4);
            SistemaSGA.getInstance().getControladorSala().cadastrar(sala1);
            System.out.println("Sala cadastrada: " + sala1.getNome());

            Palestrante palestrante1 = new Palestrante("Dr. João Silva", "Inteligência Artificial", "9999-8888", "joao@email.com");
            SistemaSGA.getInstance().getControladorPalestrante().cadastrar(palestrante1);
            System.out.println("Palestrante cadastrado: " + palestrante1.getNome());

            Participante aluno = new Participante("Maria Estudante", "maria@email.com", "UFPE", "123.456.789-00", "senha123");
            SistemaSGA.getInstance().getControladorParticipante().cadastrar(aluno);
            System.out.println("Aluno cadastrado: " + aluno.getNome());

            LocalDate hoje = LocalDate.now();
            LocalDate fimEvento = hoje.plusDays(5);
            Evento evento = new Evento("Semana de Tecnologia", hoje, fimEvento, "Evento acadêmico anual");
            SistemaSGA.getInstance().getControladorEvento().cadastrar(evento);
            System.out.println("Evento cadastrado: " + evento.getNome());


            System.out.println("\n>> 2. Testando Fluxo de Palestra...");
            LocalDateTime dataPalestra = LocalDate.now().atTime(14, 0);

            SistemaSGA.getInstance().getControladorPalestra().cadastrar(
                    "Java para Iniciantes", "Aprenda os fundamentos", evento,
                    dataPalestra, 2.0f, sala1, palestrante1
            );

            Palestra palestraJava = SistemaSGA.getInstance().getControladorPalestra().buscar("Java para Iniciantes");
            System.out.println("Palestra criada.");

            SistemaSGA.getInstance().getControladorInscricao().inscrever(aluno, palestraJava);
            System.out.println("Inscrição realizada.");

            Inscricao inscricaoFeita = SistemaSGA.getInstance().getControladorInscricao().listarPorParticipante(aluno).get(0);
            SistemaSGA.getInstance().getControladorInscricao().marcarPresenca(inscricaoFeita);
            System.out.println("Presença confirmada e Certificado gerado!");


            SistemaSGA.getInstance().getControladorWorkshop().cadastrar("Workshop Fullstack", "Web Completo", evento);
            Workshop workshop = SistemaSGA.getInstance().getControladorWorkshop().buscar("Workshop Fullstack");
            System.out.println("Workshop criado.");

            LocalDateTime dataW1 = LocalDate.now().plusDays(1).atTime(9, 0);
            LocalDateTime dataW2 = LocalDate.now().plusDays(1).atTime(14, 0);

            SistemaSGA.getInstance().getControladorPalestra().cadastrar("Aula 1: Frontend", "HTML/CSS", evento, dataW1, 4.0f, sala1, palestrante1);
            SistemaSGA.getInstance().getControladorPalestra().cadastrar("Aula 2: Backend", "Spring Boot", evento, dataW2, 4.0f, sala1, palestrante1);

            Palestra p1 = SistemaSGA.getInstance().getControladorPalestra().buscar("Aula 1: Frontend");
            Palestra p2 = SistemaSGA.getInstance().getControladorPalestra().buscar("Aula 2: Backend");

            SistemaSGA.getInstance().getControladorWorkshop().adicionarPalestraAoWorkshop(workshop.getTitulo(), p1);
            SistemaSGA.getInstance().getControladorWorkshop().adicionarPalestraAoWorkshop(workshop.getTitulo(), p2);

            Participante aluno2 = new Participante("João Dev", "joao@dev.com", "IFPE", "999.999.999-99", "123");
            SistemaSGA.getInstance().getControladorParticipante().cadastrar(aluno2);

            SistemaSGA.getInstance().getControladorInscricao().inscrever(aluno2, workshop);
            System.out.println("Inscrição no Workshop realizada.");



            try {
                System.out.print("Palestra data inválida... ");
                LocalDateTime dataFutura = LocalDate.of(2050, 1, 1).atTime(10, 0);
                SistemaSGA.getInstance().getControladorPalestra().cadastrar(
                        "Palestra do Futuro", "Teste", evento, dataFutura, 1.0f, sala1, palestrante1
                );
                System.out.println("FALHA: Data inválida permitida!");
            } catch (Exception e) {
                System.out.println("SUCESSO: Bloqueado (" + e.getMessage() + ")");
            }

            try {
                System.out.print("Conflito de sala... ");
                SistemaSGA.getInstance().getControladorPalestra().cadastrar(
                        "Palestra Conflitante", "Teste", evento, dataPalestra, 1.0f, sala1, palestrante1
                );
                System.out.println("Conflito permitido!");
            } catch (Exception e) {
                System.out.println("SUCESSO: Bloqueado (" + e.getMessage() + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\nERRO INESPERADO: " + e.getMessage());
        }

        System.exit(0);
    }
}