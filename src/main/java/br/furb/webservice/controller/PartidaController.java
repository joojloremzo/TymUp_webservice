package br.furb.webservice.controller;

import br.furb.webservice.entity.Partida;
import br.furb.webservice.service.PartidaService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partidas")
public class PartidaController {

    @Autowired
    private PartidaService service;

    @GetMapping
    public ResponseEntity<List<Partida>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partida> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Partida> salvar(
            @Valid @RequestBody Partida partida) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.salvar(partida));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partida> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Partida partida) {

        return ResponseEntity.ok(service.atualizar(id, partida));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
