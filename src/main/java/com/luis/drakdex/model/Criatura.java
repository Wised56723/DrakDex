package com.luis.drakdex.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Criatura { // <--- TEM DE TER "public" AQUI

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String tipo;
    private Integer nivel;
    private String descricao;
}