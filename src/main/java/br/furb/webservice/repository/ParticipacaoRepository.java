package br.furb.webservice.repository;

import br.furb.webservice.entity.Participacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipacaoRepository extends JpaRepository<Participacao, Long> {
}