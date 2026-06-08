package br.furb.webservice.service;

import br.furb.webservice.entity.Partida;
import br.furb.webservice.repository.PartidaRepository;

import br.furb.webservice.entity.Partida;
import br.furb.webservice.entity.Usuario;
import br.furb.webservice.repository.PartidaRepository;
import br.furb.webservice.repository.UsuarioRepository;
import br.furb.webservice.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartidaService {

    @Autowired
    private PartidaRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Partida> listar() {
        return repository.findAll();
    }

    public Partida buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Partida não encontrada"));
    }

    public Partida salvar(Partida partida) {

        if (partida.getData() == null || partida.getHora() == null) {
            throw new BancoDeDadosException("Data e hora são obrigatórias");
        }

        if (partida.getOrganizador() == null || partida.getOrganizador().getId() == null) {
            throw new BancoDeDadosException("Organizador é obrigatório");
        }

        // validar se o usuário existe
        Usuario organizador = usuarioRepository.findById(partida.getOrganizador().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Organizador não encontrado"));

        partida.setOrganizador(organizador);

        return repository.save(partida);
    }

    public Partida atualizar(Long id, Partida partida) {
        Partida existente = buscarPorId(id);

        if (partida.getData() != null)
            existente.setData(partida.getData());

        if (partida.getHora() != null)
            existente.setHora(partida.getHora());

        if (partida.getValor() != null)
            existente.setValor(partida.getValor());

        if (partida.getOrganizador() != null) {
            Usuario organizador = usuarioRepository.findById(partida.getOrganizador().getId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Organizador não encontrado"));

            existente.setOrganizador(organizador);
        }

        return repository.save(existente);
    }

    public void deletar(Long id) {
        buscarPorId(id);

        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BancoDeDadosException("Erro de integridade: partida possui participantes");
        }
    }
}
