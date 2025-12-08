package com.luis.drakdex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luis.drakdex.dto.CriaturaDTO;
import com.luis.drakdex.exception.ResourceNotFoundException;
import com.luis.drakdex.model.Criatura;
import com.luis.drakdex.repository.CriaturaRepository;
import com.luis.drakdex.service.DndApiService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/criaturas")
public class CriaturaController {

    @Autowired
    private CriaturaRepository repository;
    
    @Autowired // Injeta o serviço novo
    private DndApiService dndApiService;


    // 1. LISTAR TODAS (COM PAGINAÇÃO)
    // Exemplo: GET /api/criaturas?page=0&size=5
    @GetMapping
    public ResponseEntity<Page<CriaturaDTO>> listarTodas(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        
        Page<CriaturaDTO> pagina = repository.findAll(pageable)
                .map(this::convertToDto);
        
        return ResponseEntity.ok(pagina);
    }

    // 2. BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<CriaturaDTO> buscarPorId(@PathVariable Long id) {
        Criatura criatura = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Criatura não encontrada para o ID :: " + id));
        return ResponseEntity.ok().body(convertToDto(criatura));
    }

    // 3. CRIAR NOVA
    @PostMapping
    @Transactional
    public CriaturaDTO criar(@Valid @RequestBody CriaturaDTO criaturaDTO) {
        Criatura criatura = convertToEntity(criaturaDTO);
        Criatura criaturaSalva = repository.save(criatura);
        return convertToDto(criaturaSalva);
    }

    // 4. ATUALIZAR
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<CriaturaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody CriaturaDTO criaturaDTO) {
        Criatura criatura = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Criatura não encontrada para o ID :: " + id));

        criatura.setNome(criaturaDTO.getNome());
        criatura.setTipo(criaturaDTO.getTipo());
        criatura.setNivel(criaturaDTO.getNivel());
        criatura.setDescricao(criaturaDTO.getDescricao());

        final Criatura atualizada = repository.save(criatura);
        return ResponseEntity.ok(convertToDto(atualizada));
    }

    // 5. DELETAR
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Criatura criatura = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Criatura não encontrada para o ID :: " + id));

        repository.delete(criatura);
        return ResponseEntity.ok().build();
    }

    // 6. BUSCAR POR NOME (Paginado)
    // AQUI ESTAVA O ERRO: Agora passamos 'pageable' para o repositório
    @GetMapping("/busca")
    public ResponseEntity<Page<CriaturaDTO>> buscarPorNome(
            @RequestParam String nome,
            @PageableDefault(size = 10) Pageable pageable) {
        
        // Correção: repository.findByNome...(nome, pageable)
        Page<CriaturaDTO> pagina = repository.findByNomeContainingIgnoreCase(nome, pageable)
                .map(this::convertToDto);
                
        return ResponseEntity.ok(pagina);
    }

    // 7. FILTRAR POR TIPO (Paginado)
    // AQUI ESTAVA O ERRO: Agora passamos 'pageable' para o repositório
    @GetMapping("/filtro")
    public ResponseEntity<Page<CriaturaDTO>> filtrarPorTipo(
            @RequestParam String tipo,
            @PageableDefault(size = 10) Pageable pageable) {
        
        // Correção: repository.findByTipo(tipo, pageable)
        Page<CriaturaDTO> pagina = repository.findByTipo(tipo, pageable)
                .map(this::convertToDto);
                
        return ResponseEntity.ok(pagina);
    }

    // Rota Especial: Consome dados externos do DnD API
    // GET /api/criaturas/integracao/dnd
    @GetMapping("/integracao/dnd")
    public ResponseEntity<Object> buscarDaApiDnd() {
        Object dadosExternos = dndApiService.buscarMonstrosExternos();
        return ResponseEntity.ok(dadosExternos);
    }

    // --- CONVERSORES ---

    private CriaturaDTO convertToDto(Criatura criatura) {
        CriaturaDTO dto = new CriaturaDTO();
        dto.setId(criatura.getId());
        dto.setNome(criatura.getNome());
        dto.setTipo(criatura.getTipo());
        dto.setNivel(criatura.getNivel());
        dto.setDescricao(criatura.getDescricao());
        return dto;
    }

    private Criatura convertToEntity(CriaturaDTO criaturaDTO) {
        Criatura criatura = new Criatura();
        criatura.setNome(criaturaDTO.getNome());
        criatura.setTipo(criaturaDTO.getTipo());
        criatura.setNivel(criaturaDTO.getNivel());
        criatura.setDescricao(criaturaDTO.getDescricao());
        return criatura;
    }
}