package com.luis.drakdex.model;

import java.util.ArrayList;
import java.util.List;

import com.luis.drakdex.model.enums.CategoriaPasta; // Importante para evitar loops infinitos nos logs

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

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

    // NOVO CAMPO: CATEGORIA
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaPasta categoria = CategoriaPasta.CRIATURA; // Padrão para não quebrar as antigas

    // NOVO: RELAÇÃO COM ITENS (Uma pasta pode ter vários itens)
    @OneToMany(mappedBy = "pasta")
    private List<Item> itens = new ArrayList<>();
}