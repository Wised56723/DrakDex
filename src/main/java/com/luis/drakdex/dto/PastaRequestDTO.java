package com.luis.drakdex.dto;

import jakarta.validation.constraints.NotBlank;

// O usuário envia o nome, se é pública e (opcionalmente) o ID do pai
public record PastaRequestDTO(
    @NotBlank String nome,
    boolean publica,
    Long pastaPaiId 
) {}