package com.luis.drakdex.dto;

import jakarta.validation.constraints.NotBlank;

public record MagiaRequestDTO(
    @NotBlank String nome,
    String escola,
    String tempoExecucao,
    String alcance,
    String duracao,
    String componentes,
    String custo,
    String sistema,
    String descricao,
    Long pastaId // Opcional no Update, Obrigatório no Create
) {}