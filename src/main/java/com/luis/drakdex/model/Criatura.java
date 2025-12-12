package com.luis.drakdex.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "criaturas")
public class Criatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String tipo;
    private Integer nivel;
    private String descricao;

    // --- NOVO RELACIONAMENTO ---
    @ManyToOne // Muitas criaturas -> Um Usuário
    @JoinColumn(name = "usuario_id", nullable = false) // Cria coluna 'usuario_id' no banco
    private Usuario usuario;

    // RELACIONAMENTO COM PASTA
    // Note: deixamos nullable = true por enquanto para não quebrar criaturas antigas
    @ManyToOne
    @JoinColumn(name = "pasta_id", nullable = true) 
    @JsonIgnore // O Frontend não precisa receber o objeto Pasta inteiro dentro da criatura
    private Pasta pasta;
}