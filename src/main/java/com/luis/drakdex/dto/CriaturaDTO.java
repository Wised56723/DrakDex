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

    // --- NOVO CAMPO (Apenas para Leitura) ---
    // Não precisamos receber isso no POST (o token já diz quem é),
    // mas vamos enviar isso no GET.
    private String criadorVulgo; 
}