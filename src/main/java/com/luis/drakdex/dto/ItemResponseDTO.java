package com.luis.drakdex.dto;

import com.luis.drakdex.model.enums.Raridade;
import com.luis.drakdex.model.enums.TipoItem;

public record ItemResponseDTO(
    Long id,
    String nome,
    String descricao,
    TipoItem tipo,
    Raridade raridade,
    Double peso,
    String preco,
    String dano,
    String defesa,
    String propriedades,
    String donoVulgo
) {}