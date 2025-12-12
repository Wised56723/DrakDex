package com.luis.drakdex.model;

import jakarta.persistence.*;
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
}