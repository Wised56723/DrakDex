package com.luis.drakdex.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luis.drakdex.dto.CriaturaDTO;
import com.luis.drakdex.exception.ResourceNotFoundException;
import com.luis.drakdex.model.Criatura;
import com.luis.drakdex.repository.CriaturaRepository;

@RestController
@RequestMapping("/api/criaturas")
public class CriaturaController {

    @Autowired
    private CriaturaRepository repository;

    // 1. LISTAR TODAS (Retorna Lista de DTOs)
    @GetMapping
    public List<CriaturaDTO> listarTodas() {
        return repository.findAll().stream()
                .map(this::convertToDto) // Converte cada entidade da lista em DTO
                .collect(Collectors.toList());
    }

    // 2. BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<CriaturaDTO> buscarPorId(@PathVariable Long id) {
        Criatura criatura = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Criatura não encontrada para o ID :: " + id));
        return ResponseEntity.ok().body(convertToDto(criatura));
    }

    // 3. CRIAR NOVA (Recebe DTO -> Salva Entidade -> Retorna DTO)
    @PostMapping
    public CriaturaDTO criar(@RequestBody CriaturaDTO criaturaDTO) {
        // Converte DTO para Entidade para salvar no banco
        Criatura criatura = convertToEntity(criaturaDTO);
        
        Criatura criaturaSalva = repository.save(criatura);
        
        // Retorna o DTO da criatura salva
        return convertToDto(criaturaSalva);
    }

    // 4. ATUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<CriaturaDTO> atualizar(@PathVariable Long id, @RequestBody CriaturaDTO criaturaDTO) {
        Criatura criatura = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Criatura não encontrada para o ID :: " + id));

        // Atualiza os dados da entidade com base no DTO recebido
        criatura.setNome(criaturaDTO.getNome());
        criatura.setTipo(criaturaDTO.getTipo());
        criatura.setNivel(criaturaDTO.getNivel());
        criatura.setDescricao(criaturaDTO.getDescricao());

        final Criatura atualizada = repository.save(criatura);
        return ResponseEntity.ok(convertToDto(atualizada));
    }

    // 5. DELETAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Criatura criatura = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Criatura não encontrada para o ID :: " + id));

        repository.delete(criatura);
        return ResponseEntity.ok().build();
    }

    // ==========================================
    // MÉTODOS AUXILIARES DE CONVERSÃO (MAPPER)
    // ==========================================
    
    // Converte Entidade (Banco) -> DTO (Visualização)
    private CriaturaDTO convertToDto(Criatura criatura) {
        CriaturaDTO dto = new CriaturaDTO();
        dto.setId(criatura.getId());
        dto.setNome(criatura.getNome());
        dto.setTipo(criatura.getTipo());
        dto.setNivel(criatura.getNivel());
        dto.setDescricao(criatura.getDescricao());
        return dto;
    }

    // Converte DTO (Input) -> Entidade (Banco)
    private Criatura convertToEntity(CriaturaDTO criaturaDTO) {
        Criatura criatura = new Criatura();
        // Não setamos o ID aqui porque o banco gera automaticamente no create
        criatura.setNome(criaturaDTO.getNome());
        criatura.setTipo(criaturaDTO.getTipo());
        criatura.setNivel(criaturaDTO.getNivel());
        criatura.setDescricao(criaturaDTO.getDescricao());
        return criatura;
    }
}