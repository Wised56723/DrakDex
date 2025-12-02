package com.luis.drakdex.controller;

import com.luis.drakdex.model.Criatura;
import com.luis.drakdex.repository.CriaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/criaturas")
public class CriaturaController {

    @Autowired
    private CriaturaRepository repository;

    // 1. LISTAR TODAS (GET)
    // Rota: http://localhost:8080/api/criaturas
    @GetMapping
    public List<Criatura> listarTodas() {
        return repository.findAll();
    }

    // 2. BUSCAR POR ID (GET)
    // Rota: http://localhost:8080/api/criaturas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Criatura> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(record -> ResponseEntity.ok().body(record)) // Se achar, retorna 200 OK e o objeto
                .orElse(ResponseEntity.notFound().build());      // Se não achar, retorna 404 Not Found
    }

    // 3. CRIAR NOVA (POST)
    // Rota: http://localhost:8080/api/criaturas
    @PostMapping
    public Criatura criar(@RequestBody Criatura criatura) {
        return repository.save(criatura);
    }

    // 4. ATUALIZAR (PUT) - O NOVO MÉTODO
    // Rota: http://localhost:8080/api/criaturas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Criatura> atualizar(@PathVariable Long id, @RequestBody Criatura detalhesAtualizados) {
        return repository.findById(id)
                .map(record -> {
                    // Atualiza os dados do objeto encontrado com os novos dados
                    record.setNome(detalhesAtualizados.getNome());
                    record.setTipo(detalhesAtualizados.getTipo());
                    record.setNivel(detalhesAtualizados.getNivel());
                    record.setDescricao(detalhesAtualizados.getDescricao());
                    
                    // Salva a versão atualizada no banco
                    Criatura atualizado = repository.save(record);
                    return ResponseEntity.ok().body(atualizado);
                }).orElse(ResponseEntity.notFound().build());
    }

    // 5. DELETAR (DELETE)
    // Rota: http://localhost:8080/api/criaturas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(record -> {
                    repository.deleteById(id);
                    return ResponseEntity.ok().build(); // Retorna 200 OK (sucesso)
                }).orElse(ResponseEntity.notFound().build()); // Retorna 404 se o ID não existir
    }
}