package com.luis.drakdex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luis.drakdex.dto.ItemRequestDTO;
import com.luis.drakdex.dto.ItemResponseDTO;
import com.luis.drakdex.model.Usuario;
import com.luis.drakdex.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/itens")
public class ItemController {

    @Autowired
    private ItemService service;

    // Método auxiliar para pegar o usuário logado (evita repetir código)
    private Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping
    public ResponseEntity<ItemResponseDTO> criar(@Valid @RequestBody ItemRequestDTO dados) {
        var itemCriado = service.criar(dados, getUsuarioLogado());
        return ResponseEntity.ok(itemCriado);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> listarMeusItens() {
        return ResponseEntity.ok(service.listarMeusItens(getUsuarioLogado()));
    }

    // --- NOVOS ENDPOINTS ---

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id, getUsuarioLogado()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ItemRequestDTO dados) {
        return ResponseEntity.ok(service.atualizar(id, dados, getUsuarioLogado()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id, getUsuarioLogado());
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}