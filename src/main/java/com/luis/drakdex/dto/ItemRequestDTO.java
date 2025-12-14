package com.luis.drakdex.dto;

import com.luis.drakdex.model.enums.Raridade;
import com.luis.drakdex.model.enums.TipoItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemRequestDTO(
    @NotBlank String nome,
    String descricao,
    @NotNull TipoItem tipo,
    Raridade raridade, // Opcional
    Double peso,
    String preco,
    String dano,
    String defesa,
    String propriedades
) {}