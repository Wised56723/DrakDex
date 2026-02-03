package com.luis.drakdex.dto;

public record ItemResponseDTO(
    Long id,
    String nome,
    String descricao,
    String tipo,
    String raridade,
    Double peso,
    String preco,
    String dano,
    String defesa,
    String propriedades,
    String imagemUrl,
    String donoVulgo
) {}