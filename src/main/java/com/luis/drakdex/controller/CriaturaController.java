package com.luis.drakdex.controller; // <--- Mudamos aqui!

// Criatura model moved into this file to avoid missing package error

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/criaturas")
public class CriaturaController {

    // Simple in-memory repository to avoid depending on a missing package
    private final List<Criatura> repository = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @GetMapping
    public List<Criatura> listarTodas() {
        return repository;
    }

    @PostMapping
    public Criatura criar(@RequestBody Criatura criatura) {
        criatura.setId(idGen.getAndIncrement());
        repository.add(criatura);
        return criatura;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Criatura> buscarPorId(@PathVariable Long id) {
        Optional<Criatura> opt = repository.stream().filter(c -> c.getId().equals(id)).findFirst();
        return opt
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Optional<Criatura> opt = repository.stream().filter(c -> c.getId().equals(id)).findFirst();
        if (opt.isPresent()) {
            repository.remove(opt.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

// Simple package-private model class to satisfy compilation; replace with proper entity later
class Criatura {
    private Long id;
    private String nome;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
}