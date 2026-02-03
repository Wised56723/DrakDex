package com.luis.drakdex.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "magias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Magia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    // Campos flexíveis (String) para aceitar D&D ou outros sistemas
    private String escola;         // Ex: "Evocação", "Magia Negra", "Piromancia"
    private String tempoExecucao;  // Ex: "1 Ação", "Instantâneo"
    private String alcance;        // Ex: "18 metros", "Toque"
    private String duracao;        // Ex: "1 minuto", "Instantâneo"
    private String componentes;    // Ex: "V, S, M"
    
    // Custo genérico: pode ser "Slot Nível 3" ou "10 Mana"
    private String custo; 

    // Sistema de origem para ajudar na organização (Ex: "D&D 5e", "Tormenta20")
    private String sistema;

    @Column(columnDefinition = "TEXT") // Permite textos longos
    private String descricao;

    // --- RELACIONAMENTOS ---

    @ManyToOne
    @JoinColumn(name = "pasta_id", nullable = false)
    private Pasta pasta;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}