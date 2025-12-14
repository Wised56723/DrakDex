package com.luis.drakdex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luis.drakdex.dto.PastaRequestDTO;
import com.luis.drakdex.dto.PastaResponseDTO;
import com.luis.drakdex.model.Usuario;
import com.luis.drakdex.model.enums.CategoriaPasta;
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
    public ResponseEntity<List<PastaResponseDTO>> listarMeus(
            @RequestParam(defaultValue = "CRIATURA") CategoriaPasta tipo) {
        
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Agora passamos o tipo (ITEM ou CRIATURA) para o serviço filtrar
        return ResponseEntity.ok(service.listarMinhasPastasRaiz(usuario, tipo));
    }

    @GetMapping("/publicas")
    public ResponseEntity<List<PastaResponseDTO>> listarPublicas(
            @RequestParam(defaultValue = "CRIATURA") CategoriaPasta tipo) {
        
        // Também filtramos as públicas pelo tipo
        return ResponseEntity.ok(service.listarPublicas(tipo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PastaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}