package com.luis.drakdex.dto;

import java.util.List;

import com.luis.drakdex.model.enums.TipoFicha;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NpcRequestDTO(
    @NotBlank String nome,
    @NotNull TipoFicha tipoFicha, // DND5E ou LIVRE
    
    // Lore
    String aparencia,
    String personalidade,
    String historia,

    // D&D 5e (Opcionais)
    Integer nivelDesafio,
    Integer classeArmadura,
    Integer pontosVida,
    Integer forca,
    Integer destreza,
    Integer constituicao,
    Integer inteligencia,
    Integer sabedoria,
    Integer carisma,

    // Modo Livre
    String regrasCustomizadas,

    // Localização
    Long pastaId,

    // Relacionamentos (Recebemos apenas os IDs para vincular)
    List<Long> equipamentosIds,
    List<Long> magiasIds
) {}