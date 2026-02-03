package com.luis.drakdex.controller;

import com.luis.drakdex.dto.MagiaRequestDTO;
import com.luis.drakdex.dto.MagiaResponseDTO;
import com.luis.drakdex.model.Usuario;
import com.luis.drakdex.service.MagiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/magias")
@RequiredArgsConstructor
public class MagiaController {

    private final MagiaService service;

    @PostMapping
    public ResponseEntity<MagiaResponseDTO> criar(@RequestBody @Valid MagiaRequestDTO dados) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(service.criar(dados, usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MagiaResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid MagiaRequestDTO dados) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(service.atualizar(id, dados, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        service.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}