package com.luis.drakdex.controller;

import com.luis.drakdex.service.DndApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external")
@RequiredArgsConstructor
public class ExternalController {

    private final DndApiService dndApiService;

    // Lista todos os monstros disponíveis na API do D&D
    // Retorna algo como: [{ "index": "aboleth", "name": "Aboleth", "url": "/api/monsters/aboleth" }, ...]
    @GetMapping("/monsters")
    public ResponseEntity<Object> listarMonstrosExternos() {
        return ResponseEntity.ok(dndApiService.getMonsters());
    }

    // Busca os detalhes de um monstro específico pelo índice (ex: "aboleth")
    @GetMapping("/monsters/{index}")
    public ResponseEntity<Object> buscarDetalhesMonstro(@PathVariable String index) {
        return ResponseEntity.ok(dndApiService.getMonsterByIndex(index));
    }
}