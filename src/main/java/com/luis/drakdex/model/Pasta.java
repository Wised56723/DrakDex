package com.luis.drakdex.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString; // Importante para evitar loops infinitos nos logs
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "pastas")
public class Pasta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private boolean publica; // true = visível para todos, false = só o dono

    // DONO DA PASTA
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // --- AUTO-RELACIONAMENTO (Árvore de Pastas) ---
    
    // Quem é o Pai? (Pode ser null se for Pasta Raiz)
    @ManyToOne
    @JoinColumn(name = "pasta_pai_id")
    @ToString.Exclude // Evita loop infinito ao imprimir
    private Pasta pastaPai;

    // Quem são os Filhos?
    @OneToMany(mappedBy = "pastaPai", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pasta> subPastas = new ArrayList<>();

    // --- CONTEÚDO ---
    
    // Criaturas dentro desta pasta
    @OneToMany(mappedBy = "pasta")
    private List<Criatura> criaturas = new ArrayList<>();
}