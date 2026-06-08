package br.furb.webservice.service;

import br.furb.webservice.exception.*;
import br.furb.webservice.entity.Usuario;
import br.furb.webservice.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    public Usuario salvar(Usuario usuario) {

        if (usuario.getNome() == null || usuario.getEmail() == null) {
            throw new BancoDeDadosException("Nome e email são obrigatórios");
        }

        usuario.setSenha(
                passwordEncoder.encode(usuario.getSenha())
        );

        return repository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario usuario) {

        Usuario existente = buscarPorId(id);

        if (usuario.getNome() != null)
            existente.setNome(usuario.getNome());

        if (usuario.getEmail() != null)
            existente.setEmail(usuario.getEmail());

        if (usuario.getSenha() != null)
            existente.setSenha(
                    passwordEncoder.encode(usuario.getSenha())
            );

        if (usuario.getPosicaoPreferida() != null)
            existente.setPosicaoPreferida(usuario.getPosicaoPreferida());

        return repository.save(existente);
    }

    public void deletar(Long id) {

        buscarPorId(id);

        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BancoDeDadosException("Erro de integridade: usuário está em uso");
        }
    }
}