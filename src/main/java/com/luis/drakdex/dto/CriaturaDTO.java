package com.luis.drakdex.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CriaturaDTO {
    private Long id;
    
    @NotBlank
    private String nome;
    
    @NotBlank
    private String tipo;
    
    private Integer nivel;
    private String descricao;
    
    private String criadorVulgo; // Apenas leitura (saída)
    
    // --- NOVO CAMPO ---
    private Long pastaId; // O ID da pasta onde vamos salvar (entrada)
}