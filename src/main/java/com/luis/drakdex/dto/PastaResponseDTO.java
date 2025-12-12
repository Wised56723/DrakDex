package com.luis.drakdex.dto;

import java.util.List;

public record PastaResponseDTO(
    Long id,
    String nome,
    boolean publica,
    Long pastaPaiId,
    List<PastaResponseDTO> subPastas, // Recursivo para o Frontend montar a árvore
    int quantidadeCriaturas
) {}