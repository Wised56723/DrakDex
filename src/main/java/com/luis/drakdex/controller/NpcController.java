package com.luis.drakdex.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luis.drakdex.dto.NpcRequestDTO;
import com.luis.drakdex.dto.NpcResponseDTO;
import com.luis.drakdex.model.Usuario;
import com.luis.drakdex.service.NpcService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/npcs")
@RequiredArgsConstructor
public class NpcController {

    private final NpcService service;

    @PostMapping
    public ResponseEntity<NpcResponseDTO> criar(@RequestBody @Valid NpcRequestDTO dados) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(service.criar(dados, usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NpcResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid NpcRequestDTO dados) {
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