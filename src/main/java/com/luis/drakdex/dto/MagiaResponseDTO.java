package com.luis.drakdex.dto;

public record MagiaResponseDTO(
    Long id,
    String nome,
    String escola,
    String tempoExecucao,
    String alcance,
    String duracao,
    String componentes,
    String custo,
    String sistema,
    String descricao,
    String donoVulgo // Para mostrar quem criou (se for pasta pública)
) {}