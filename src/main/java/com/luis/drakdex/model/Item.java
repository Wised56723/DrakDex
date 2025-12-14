package com.luis.drakdex.model;

import com.luis.drakdex.model.enums.Raridade;
import com.luis.drakdex.model.enums.TipoItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "itens")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoItem tipo;

    @Enumerated(EnumType.STRING)
    private Raridade raridade; // Opcional (pode ser null)

    // Atributos numéricos básicos
    private Double peso; // Em Kg
    private String preco; // String para permitir "10 PO", "5 PP"

    // Atributos de Combate (Opcionais)
    private String dano;   // Ex: "1d8 + 2"
    private String defesa; // Ex: "+2 AC" ou "16"
    private String propriedades; // Ex: "Furtivo, Duas Mãos"

    // Vínculo com o Usuário (Dono do Item)
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}