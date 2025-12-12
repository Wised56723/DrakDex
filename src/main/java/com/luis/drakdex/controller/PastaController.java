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

import com.luis.drakdex.dto.PastaRequestDTO;
import com.luis.drakdex.dto.PastaResponseDTO;
import com.luis.drakdex.model.Usuario;
import com.luis.drakdex.service.PastaService;

@RestController
@RequestMapping("/api/pastas")
public class PastaController {

    @Autowired
    private PastaService service;

    @PostMapping
    public ResponseEntity<PastaResponseDTO> criar(@RequestBody PastaRequestDTO dados) {
        // Pega o usuário logado
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        var pastaCriada = service.criarPasta(dados, usuario);
        return ResponseEntity.ok(pastaCriada);
    }

    @GetMapping("/meus-bestiarios")
    public ResponseEntity<List<PastaResponseDTO>> listarMeus() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(service.listarMinhasPastasRaiz(usuario));
    }
}