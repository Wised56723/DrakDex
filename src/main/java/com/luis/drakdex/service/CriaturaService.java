package com.luis.drakdex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luis.drakdex.dto.CriaturaDTO;
import com.luis.drakdex.dto.CriaturaRequestDTO;
import com.luis.drakdex.model.Criatura;
import com.luis.drakdex.model.Pasta;
import com.luis.drakdex.model.Usuario;
import com.luis.drakdex.repository.CriaturaRepository;
import com.luis.drakdex.repository.PastaRepository;

@Service
public class CriaturaService {

    @Autowired
    private CriaturaRepository repository;

    @Autowired
    private PastaRepository pastaRepository;

    @Transactional
    public CriaturaDTO criarCriatura(CriaturaRequestDTO dados, Usuario usuario) {
        Criatura criatura = new Criatura();
        criatura.setNome(dados.nome());
        criatura.setDescricao(dados.descricao());
        criatura.setNivel(dados.nivel());
        criatura.setTipo(dados.tipo());
        criatura.setUsuario(usuario);
        
        // 👇 SALVAR A URL DA IMAGEM
        criatura.setImagemUrl(dados.imagemUrl());

        if (dados.pastaId() != null) {
            Pasta pasta = pastaRepository.findById(dados.pastaId())
                    .orElseThrow(() -> new RuntimeException("Pasta não encontrada"));
            criatura.setPasta(pasta);
        }

        repository.save(criatura);
        return converterParaDTO(criatura);
    }

    private CriaturaDTO converterParaDTO(Criatura criatura) {
        CriaturaDTO dto = new CriaturaDTO();
        dto.setId(criatura.getId());
        dto.setNome(criatura.getNome());
        dto.setDescricao(criatura.getDescricao());
        dto.setNivel(criatura.getNivel());
        dto.setTipo(criatura.getTipo());
        
        // 👇 DEFINIR A URL DA IMAGEM NO DTO
        dto.setImagemUrl(criatura.getImagemUrl());

        if (criatura.getUsuario() != null) {
            dto.setCriadorVulgo(criatura.getUsuario().getVulgo());
        }
        return dto;
    }
}