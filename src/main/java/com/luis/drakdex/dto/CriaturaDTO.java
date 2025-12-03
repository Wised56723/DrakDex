package com.luis.drakdex.dto;

import lombok.Data;

@Data
public class CriaturaDTO {
    // Note que não temos anotações como @Entity ou @Column aqui
    private Long id;
    private String nome;
    private String tipo;
    private Integer nivel;
    private String descricao;
}