package com.luis.drakdex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    public ResponseEntity<ItemResponseDTO> criar(@Valid @RequestBody ItemRequestDTO dados) {
        // Pega o usuário logado via Token JWT
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        var itemCriado = service.criar(dados, usuario);
        return ResponseEntity.ok(itemCriado);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> listarMeusItens() {
        // Pega o usuário logado
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        return ResponseEntity.ok(service.listarMeusItens(usuario));
    }
}