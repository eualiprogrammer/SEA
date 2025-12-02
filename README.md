# Sistema de Gestão de Eventos Acadêmicos

## Descrição

Este sistema tem como objetivo gerenciar eventos acadêmicos, como seminários, workshops, palestras e cursos, incluindo cadastro de palestrantes, participantes, salas, horários e emissão de certificados. Ele deve permitir o planejamento completo do evento, registro de inscrições, controle de lotação e acompanhamento da participação dos inscritos.

O sistema deve organizar o cronograma do evento, associar palestras e workshops a salas disponíveis, controlar presença dos participantes, emitir certificados digitais e gerar relatórios detalhados sobre inscrições, participação e desempenho do evento.

## Requisitos Funcionais

### 1. Gerenciamento de Palestrantes

- **REQ01**: Permitir cadastro de palestrantes com nome, área de especialização e dados de contato.
- **REQ02**: Atualizar disponibilidade dos palestrantes e informações de contato.
- **REQ03**: Associar palestrantes a palestras ou workshops específicos.

### 2. Gerenciamento de Palestras e Workshops

- **REQ04**: Permitir cadastro de palestras e workshops com título, descrição, duração e horário.
- **REQ05**: Associar cada palestra a uma sala disponível.
- **REQ06**: Atualizar cronograma e horários em caso de alterações do evento.

### 3. Gerenciamento de Participantes

- **REQ07**: Permitir inscrição de participantes com nome, e-mail e instituição.
- **REQ08**: Confirmar inscrições e enviar e-mail de confirmação automaticamente.
- **REQ09**: Registrar presença dos participantes em cada palestra ou workshop.

### 4. Gerenciamento de Salas

- **REQ10**: Permitir cadastro de salas com capacidade e recursos disponíveis (projetor, som, assentos).
- **REQ11**: Bloquear inscrições ou lotar automaticamente quando a capacidade da sala for atingida.
- **REQ12**: Atualizar status da sala em tempo real (ocupada, livre, em manutenção).

### 5. Emissão de Certificados

- **REQ13**: Gerar certificados digitais para participantes presentes nas atividades.
- **REQ14**: Permitir emissão de certificados em PDF com dados personalizados (nome, atividade, carga horária).
- **REQ15**: Registrar histórico de certificados emitidos para cada participante.

### 6. Relatórios e Estatísticas

- **REQ16**: Gerar relatório de inscrições por evento, palestra ou workshop.
- **REQ17**: Gerar relatório de presença e participação dos inscritos.
- **REQ18**: Relatórios de palestrantes, salas utilizadas e atividades realizadas.
- **REQ19**: Exportação de relatórios em **PDF** e **CSV**.

### 7. Alertas e Notificações

- **REQ20**: Enviar e-mails de confirmação de inscrição e lembretes de participação.
- **REQ21**: Notificar participantes sobre alterações de horários ou cancelamento de atividades.
- **REQ22**: Alertar palestrantes sobre alterações em suas atividades.

### 8. Regras e Restrições

- **REQ23**: Não permitir inscrição em atividades com vagas esgotadas.
- **REQ24**: Não permitir atribuição de palestrante a horários conflitantes.
- **REQ25**: Bloquear exclusão de atividade após confirmação de presença de participantes.
- **REQ26**: Garantir que cada certificado seja emitido apenas para participantes confirmados.

## Possíveis APIs/Bibliotecas a Serem Usadas

- **JavaFX** – Interface gráfica para gestão de eventos, inscrições e salas.
- **JDBC / Hibernate** – Persistência de dados de palestrantes, participantes e eventos.
- **Java Mail API** – Envio de e-mails de confirmação, lembretes e notificações.
- **iText / JasperReports** – Geração de certificados e relatórios em PDF.
- **Apache POI** – Exportação de relatórios em CSV ou Excel.
- **JUnit / Mockito** – Testes de regras de negócio (inscrições, lotação e emissão de certificados).

#integrantes do grupo com nome completo:
* Silas Silva de Azevedo - nome6274@gmail.com
* Pedro Lucas Dias Penante Neves - pedropenanteneves@gmail.com
* Gabriel Barros de Morais - gabrielbmorais12@gmail.com
* Efraim Félix Jacinto de Alcântara - efraimjacinto8e5@gmail.com
* Alícia Maria de Souza Monteiro Gomes - alicia.msmg@gmail.com
