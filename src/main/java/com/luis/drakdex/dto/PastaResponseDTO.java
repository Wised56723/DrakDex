package com.luis.drakdex.dto;

import java.util.List;

import com.luis.drakdex.model.enums.CategoriaPasta;

public record PastaResponseDTO(
    Long id,
    String nome,
    boolean publica,
    Long pastaPaiId,
    List<PastaResponseDTO> subPastas,
    int quantidadeCriaturas,
    List<CriaturaDTO> criaturas,
    String donoVulgo,
    
    // NOVOS CAMPOS
    CategoriaPasta categoria,
    List<ItemResponseDTO> itens // <--- Lista de itens dentro da pasta
) {}