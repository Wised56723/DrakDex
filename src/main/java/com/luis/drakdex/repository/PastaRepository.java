package com.luis.drakdex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luis.drakdex.model.Pasta;
import com.luis.drakdex.model.enums.CategoriaPasta;

public interface PastaRepository extends JpaRepository<Pasta, Long> {
    
    // Buscar pastas raízes filtrando pelo TIPO (CRIATURA ou ITEM)
    List<Pasta> findByUsuarioIdAndPastaPaiIsNullAndCategoria(Long usuarioId, CategoriaPasta categoria);
    
    // Buscar pastas públicas raízes filtrando pelo TIPO
    List<Pasta> findByUsuarioIdAndPastaPaiIsNullAndPublicaTrueAndCategoria(Long usuarioId, CategoriaPasta categoria);

    // Listar TODAS as públicas raízes por categoria (para o feed geral)
    List<Pasta> findByPublicaTrueAndPastaPaiIsNullAndCategoria(CategoriaPasta categoria);
}