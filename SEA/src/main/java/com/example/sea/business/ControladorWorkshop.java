package com.example.sea.business;

import com.example.sea.data.IRepositorioEvento;
import com.example.sea.data.IRepositorioWorkshop;
import com.example.sea.data.RepositorioEvento;
import com.example.sea.data.RepositorioWorkshop;
import com.example.sea.model.Workshop;
import com.example.sea.model.Evento;
import com.example.sea.model.Palestra;
import com.example.sea.exceptions.*;
import java.util.List;

public class ControladorWorkshop implements IControladorWorkshop {

    private IRepositorioWorkshop repositorioWorkshop;
    private IRepositorioEvento repositorioEvento;

    public ControladorWorkshop() {
        this.repositorioWorkshop = new RepositorioWorkshop();
        this.repositorioEvento = new RepositorioEvento();
    }

    @Override
    public void cadastrar(String titulo, String descricao, Evento evento)
            throws WorkshopJaExisteException, CampoVazioException {

        if (titulo == null || titulo.trim().isEmpty()) throw new CampoVazioException("Título");
        if (evento == null) throw new IllegalArgumentException("O evento não pode ser nulo.");

        Workshop novoWorkshop = new Workshop(titulo, descricao, evento);
        evento.adicionarAtividade(novoWorkshop);
        try {
            SistemaSGA.getInstance().getControladorEvento().atualizar(evento);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.repositorioWorkshop.salvar(novoWorkshop);
    }

    @Override
    public Workshop buscar(String titulo) throws WorkshopNaoEncontradoException, CampoVazioException {
        if (titulo == null || titulo.trim().isEmpty()) throw new CampoVazioException("Título");
        return this.repositorioWorkshop.buscarPorTitulo(titulo);
    }

    @Override
    public List<Workshop> listar() {
        return this.repositorioWorkshop.listarTodos();
    }

    @Override
    public void atualizar(Workshop workshop) throws WorkshopNaoEncontradoException, CampoVazioException {
        if (workshop == null) throw new IllegalArgumentException("Workshop nulo");
        this.repositorioWorkshop.atualizar(workshop);
    }

    @Override
    public void remover(String titulo) throws WorkshopNaoEncontradoException, CampoVazioException {
        if (titulo == null || titulo.trim().isEmpty()) throw new CampoVazioException("Título");
        this.repositorioWorkshop.deletar(titulo);
    }

    @Override
    public void adicionarPalestraAoWorkshop(String tituloWorkshop, Palestra palestra)
            throws WorkshopNaoEncontradoException, CampoVazioException {
        Workshop workshop = this.buscar(tituloWorkshop);
        if (palestra != null) {
            if (!palestra.getEvento().getNome().equals(workshop.getEvento().getNome())) {
                throw new IllegalArgumentException("A palestra deve pertencer ao mesmo evento do workshop.");
            }

            workshop.adicionarPalestra(palestra);
            this.repositorioWorkshop.atualizar(workshop);
        }
    }

    @Override
    public void removerPalestraDoWorkshop(String tituloWorkshop, Palestra palestra)
            throws WorkshopNaoEncontradoException, CampoVazioException {
        Workshop workshop = this.buscar(tituloWorkshop);
        if (palestra != null) {
            workshop.removerPalestra(palestra);
            this.repositorioWorkshop.atualizar(workshop);
        }
    }
}