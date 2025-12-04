package com.example.sea.business;

public class SistemaSGA {

    private static SistemaSGA instance;

    private IControladorSala controladorSala;
    private IControladorPalestrante controladorPalestrante;
    private IControladorParticipante controladorParticipante;
    private IControladorEvento controladorEvento;
    private IControladorPalestra controladorPalestra;
    private IControladorWorkshop controladorWorkshop;
    private IControladorInscricao controladorInscricao;
    private IControladorCertificado controladorCertificado;

    private IControladorNotificacao controladorNotificacao;

    private SistemaSGA() {
        this.controladorSala = new ControladorSala();
        this.controladorPalestrante = new ControladorPalestrante();
        this.controladorParticipante = new ControladorParticipante();
        this.controladorEvento = new ControladorEvento();
        this.controladorPalestra = new ControladorPalestra();
        this.controladorWorkshop = new ControladorWorkshop();
        this.controladorInscricao = new ControladorInscricao();
        this.controladorCertificado = new ControladorCertificado();
        
        this.controladorNotificacao = new ControladorNotificacao();
    }

    public static SistemaSGA getInstance() {
        if (instance == null) {
            instance = new SistemaSGA();
        }
        return instance;
    }

    // Getters
    public IControladorSala getControladorSala() { return controladorSala; }
    public IControladorPalestrante getControladorPalestrante() { return controladorPalestrante; }
    public IControladorParticipante getControladorParticipante() { return controladorParticipante; }
    public IControladorEvento getControladorEvento() { return controladorEvento; }
    public IControladorPalestra getControladorPalestra() { return controladorPalestra; }
    public IControladorWorkshop getControladorWorkshop() { return controladorWorkshop; }
    public IControladorInscricao getControladorInscricao() { return controladorInscricao; }
    public IControladorCertificado getControladorCertificado() { return controladorCertificado; }
    public IControladorNotificacao getControladorNotificacao() { return controladorNotificacao; }
}