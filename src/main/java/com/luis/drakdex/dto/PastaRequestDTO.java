package com.luis.drakdex.dto;

import com.luis.drakdex.model.enums.CategoriaPasta;

import jakarta.validation.constraints.NotBlank;

public record PastaRequestDTO(
    @NotBlank String nome,
    boolean publica,
    Long pastaPaiId,
    CategoriaPasta categoria // <--- NOVO (Opcional, se null assumimos CRIATURA)
) {}