package com.luis.drakdex.dto;

import java.util.List;

public record PastaResponseDTO(
    Long id,
    String nome,
    boolean publica,
    Long pastaPaiId,
    String categoria, // String, não o Enum CategoriaPasta
    List<PastaResponseDTO> subPastas,
    List<CriaturaResponseDTO> criaturas,
    List<ItemResponseDTO> itens,
    List<MagiaResponseDTO> magias, 
    List<NpcResponseDTO> npcs,     
    String donoVulgo
) {}