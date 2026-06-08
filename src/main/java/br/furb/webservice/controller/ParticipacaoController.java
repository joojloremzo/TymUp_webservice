package br.furb.webservice.controller;

import br.furb.webservice.entity.Participacao;
import br.furb.webservice.service.ParticipacaoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participacoes")
public class ParticipacaoController {

    @Autowired
    private ParticipacaoService service;

    @GetMapping
    public ResponseEntity<List<Participacao>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participacao> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Participacao> salvar(@RequestBody Participacao p) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.salvar(p));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participacao> atualizar(@PathVariable Long id,
                                                  @RequestBody Participacao p) {
        return ResponseEntity.ok(service.atualizar(id, p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Participacao> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(service.confirmarPresenca(id));
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<Participacao> pagar(@PathVariable Long id) {
        return ResponseEntity.ok(service.marcarPagamento(id));
    }

    @PutMapping("/{id}/time")
    public ResponseEntity<Participacao> definirTime(
            @PathVariable Long id,
            @RequestBody String time) {

        return ResponseEntity.ok(service.definirTime(id, time));
    }
}