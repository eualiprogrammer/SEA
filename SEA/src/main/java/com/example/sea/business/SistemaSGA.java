package com.example.sea.business;

public class SistemaSGA {
    
    private static SistemaSGA instance;
    
    private IControladorPalestrante controladorPalestrante;
    private IControladorSala controladorSala;
    private IControladorParticipante controladorParticipante;
    private IControladorEvento controladorEvento;
    private IControladorPalestra controladorPalestra;
    private IControladorWorkshop controladorWorkshop;
    private IControladorInscricao controladorInscricao;
    private IControladorCertificado controladorCertificado;
   
    private SistemaSGA() {
        System.out.println("Inicializando o Sistema SGA...");
        
        this.controladorPalestrante = new ControladorPalestrante();
        this.controladorSala = new ControladorSala();
        this.controladorParticipante = new ControladorParticipante();
        this.controladorEvento = new ControladorEvento();
        this.controladorPalestra = new ControladorPalestra();
        this.controladorWorkshop = new ControladorWorkshop();
        this.controladorInscricao = new ControladorInscricao();
        this.controladorCertificado = new ControladorCertificado();
    }

    public static SistemaSGA getInstance() {
        if (instance == null) {
            instance = new SistemaSGA();
        }
        return instance;
    }

    public IControladorPalestrante getControladorPalestrante() {
        return controladorPalestrante;
    }

    public IControladorSala getControladorSala() {
        return controladorSala;
    }

    public IControladorParticipante getControladorParticipante() {
        return controladorParticipante;
    }

    public IControladorEvento getControladorEvento() {
        return controladorEvento;
    }

    public IControladorPalestra getControladorPalestra() {
        return controladorPalestra;
    }

    public IControladorWorkshop getControladorWorkshop() {
        return controladorWorkshop;
    }

    public IControladorInscricao getControladorInscricao() {
        return controladorInscricao;
    }

    public IControladorCertificado getControladorCertificado() {
        return controladorCertificado;
    }
}