package com.luis.drakdex.dto;

public record CriaturaResponseDTO(
    Long id,
    String nome,
    String descricao,
    Integer nivel,
    String tipo,
    String imagemUrl,
    String criadorVulgo
) {}