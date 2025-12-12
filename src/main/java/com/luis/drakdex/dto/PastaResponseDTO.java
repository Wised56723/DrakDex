package com.luis.drakdex.dto;

import java.util.List;

public record PastaResponseDTO(
    Long id,
    String nome,
    boolean publica,
    Long pastaPaiId,
    List<PastaResponseDTO> subPastas,
    int quantidadeCriaturas,
    List<CriaturaDTO> criaturas, // <--- NOVO: Lista real das criaturas
    String donoVulgo // <--- NOVO: Para saber de quem é a pasta pública
) {}