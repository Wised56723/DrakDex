package com.luis.drakdex.dto;

import java.util.List;

import com.luis.drakdex.model.enums.TipoFicha;

public record NpcResponseDTO(
    Long id,
    String nome,
    TipoFicha tipoFicha,
    
    String aparencia,
    String personalidade,
    String historia,

    Integer nivelDesafio,
    Integer classeArmadura,
    Integer pontosVida,
    Integer forca,
    Integer destreza,
    Integer constituicao,
    Integer inteligencia,
    Integer sabedoria,
    Integer carisma,

    String regrasCustomizadas,

    // Aqui devolvemos os objetos completos para o Front mostrar os detalhes
    List<ItemResponseDTO> equipamentos,
    List<MagiaResponseDTO> magiasConhecidas,
    
    String donoVulgo
) {}