package com.luis.drakdex.model;

import java.util.ArrayList;
import java.util.List;

import com.luis.drakdex.model.enums.TipoFicha;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "npcs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Npc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoFicha tipoFicha; // DND5E ou LIVRE

    // --- CAMPOS DE LORE (Comuns a ambos) ---
    @Column(columnDefinition = "TEXT")
    private String aparencia;
    
    @Column(columnDefinition = "TEXT")
    private String personalidade;
    
    @Column(columnDefinition = "TEXT")
    private String historia;

    // --- CAMPOS ESPECÍFICOS DE D&D 5e (Nullable) ---
    // Só serão preenchidos se tipoFicha == DND5E
    private Integer nivelDesafio; // CR ou Nível
    private Integer classeArmadura;
    private Integer pontosVida;
    
    // Atributos
    private Integer forca;
    private Integer destreza;
    private Integer constituicao;
    private Integer inteligencia;
    private Integer sabedoria;
    private Integer carisma;

    // --- CAMPO PARA MODO LIVRE ---
    @Column(columnDefinition = "TEXT")
    private String regrasCustomizadas; // Texto livre para stats de outros sistemas

    // --- RELACIONAMENTOS ---

    @ManyToOne
    @JoinColumn(name = "pasta_id", nullable = false)
    private Pasta pasta;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // NOVO: NPC pode ter vários itens (Loot / Equipamento)
    // Cria uma tabela intermediária "npc_itens"
    @ManyToMany
    @JoinTable(
        name = "npc_itens",
        joinColumns = @JoinColumn(name = "npc_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> equipamento = new ArrayList<>();

    // NOVO: NPC pode conhecer várias magias (Grimório)
    // Cria uma tabela intermediária "npc_magias"
    @ManyToMany
    @JoinTable(
        name = "npc_magias",
        joinColumns = @JoinColumn(name = "npc_id"),
        inverseJoinColumns = @JoinColumn(name = "magia_id")
    )
    private List<Magia> magiasConhecidas = new ArrayList<>();
}