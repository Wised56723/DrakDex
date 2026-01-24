package com.luis.drakdex.dto;

import jakarta.validation.constraints.NotBlank;

public record CriaturaRequestDTO(
    @NotBlank String nome,
    String descricao,
    Integer nivel,
    String tipo,
    Long pastaId,
    String imagemUrl // <--- NOVO
) {}