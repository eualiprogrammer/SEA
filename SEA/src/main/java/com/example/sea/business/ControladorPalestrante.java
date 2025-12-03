package com.example.sea.business;

import com.example.sea.data.IRepositorioPalestrante;
import com.example.sea.data.RepositorioPalestrante;
import com.example.sea.model.Palestrante;
import com.example.sea.exceptions.PalestranteJaExisteException;
import com.example.sea.exceptions.PalestranteNaoEncontradoException;
import com.example.sea.exceptions.CampoVazioException; 
import java.util.List;

public class ControladorPalestrante implements IControladorPalestrante {

    private IRepositorioPalestrante repositorioPalestrante;

    public ControladorPalestrante() {
        this.repositorioPalestrante = new RepositorioPalestrante();
    }

    @Override
    public void cadastrar(Palestrante palestrante) throws PalestranteJaExisteException, CampoVazioException {
        if (palestrante == null) {
            throw new IllegalArgumentException("O palestrante não pode ser nulo.");
        }
        if (palestrante.getNome() == null || palestrante.getNome().trim().isEmpty()) {
            throw new CampoVazioException("Nome"); 
        }
        if (palestrante.getEmail() == null || palestrante.getEmail().trim().isEmpty()) {
            throw new CampoVazioException("Email");
        }
        
        this.repositorioPalestrante.salvar(palestrante);
    }

    @Override
    public Palestrante buscar(String email) throws PalestranteNaoEncontradoException, CampoVazioException {
        if (email == null || email.trim().isEmpty()) {
            throw new CampoVazioException("Email");
        }
        
        return this.repositorioPalestrante.buscarPorIdentificador(email);
    }

    @Override
    public List<Palestrante> listar() {
        return this.repositorioPalestrante.listarTodos();
    }

    @Override
    public void atualizar(Palestrante palestrante) throws PalestranteNaoEncontradoException, CampoVazioException {
        if (palestrante == null) {
            throw new IllegalArgumentException("O palestrante não pode ser nulo.");
        }
        if (palestrante.getEmail() == null || palestrante.getEmail().trim().isEmpty()) {
            throw new CampoVazioException("Email");
        }
       

        this.repositorioPalestrante.atualizar(palestrante);
    }

    @Override
    public void remover(String email) throws PalestranteNaoEncontradoException, CampoVazioException {
        if (email == null || email.trim().isEmpty()) {
            throw new CampoVazioException("Email");
        }

        this.repositorioPalestrante.deletar(email);
    }
}