package br.furb.webservice.service;

import br.furb.webservice.entity.Participacao;
import br.furb.webservice.entity.Usuario;
import br.furb.webservice.entity.Partida;
import br.furb.webservice.repository.ParticipacaoRepository;
import br.furb.webservice.repository.UsuarioRepository;
import br.furb.webservice.repository.PartidaRepository;
import br.furb.webservice.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipacaoService {

    @Autowired
    private ParticipacaoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PartidaRepository partidaRepository;

    public List<Participacao> listar() {
        return repository.findAll();
    }

    public Participacao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Participação não encontrada"));
    }

    public Participacao salvar(Participacao p) {

        if (p.getUsuario() == null || p.getUsuario().getId() == null) {
            throw new BancoDeDadosException("Usuário é obrigatório");
        }

        if (p.getPartida() == null || p.getPartida().getId() == null) {
            throw new BancoDeDadosException("Partida é obrigatória");
        }

        Usuario usuario = usuarioRepository.findById(p.getUsuario().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        Partida partida = partidaRepository.findById(p.getPartida().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Partida não encontrada"));

        p.setUsuario(usuario);
        p.setPartida(partida);

        return repository.save(p);
    }

    public Participacao atualizar(Long id, Participacao p) {
        Participacao existente = buscarPorId(id);

        if (p.getConfirmado() != null)
            existente.setConfirmado(p.getConfirmado());

        if (p.getPagou() != null)
            existente.setPagou(p.getPagou());

        if (p.getTime() != null)
            existente.setTime(p.getTime());

        return repository.save(existente);
    }

    public void deletar(Long id) {
        buscarPorId(id);

        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BancoDeDadosException("Erro ao deletar participação");
        }
    }

    public Participacao confirmarPresenca(Long id) {
        Participacao p = buscarPorId(id);
        p.setConfirmado(true);
        return repository.save(p);
    }

    public Participacao marcarPagamento(Long id) {
        Participacao p = buscarPorId(id);
        p.setPagou(true);
        return repository.save(p);
    }

    public Participacao definirTime(Long id, String time) {
        Participacao p = buscarPorId(id);

        if (!time.equalsIgnoreCase("A") && !time.equalsIgnoreCase("B")) {
            throw new BancoDeDadosException("Time deve ser A ou B");
        }

        p.setTime(time.toUpperCase());
        return repository.save(p);
    }
}
