package com.luis.drakdex.model;

import com.luis.drakdex.model.enums.CategoriaPasta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pastas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pasta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private boolean publica;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaPasta categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "pasta_pai_id")
    private Pasta pastaPai;

    @OneToMany(mappedBy = "pastaPai", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pasta> subPastas = new ArrayList<>();

    // --- CONTEÚDOS ---

    @OneToMany(mappedBy = "pasta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Criatura> criaturas = new ArrayList<>();

    @OneToMany(mappedBy = "pasta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> itens = new ArrayList<>();

    // NOVOS CONTEÚDOS (SPRINT 3)
    @OneToMany(mappedBy = "pasta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Magia> magias = new ArrayList<>();

    @OneToMany(mappedBy = "pasta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Npc> npcs = new ArrayList<>();
}