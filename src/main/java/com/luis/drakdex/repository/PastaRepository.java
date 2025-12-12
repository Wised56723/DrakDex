package com.luis.drakdex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luis.drakdex.model.Pasta;

public interface PastaRepository extends JpaRepository<Pasta, Long> {
    
    // Buscar apenas as pastas RAIZ (sem pai) de um usuário específico
    // Isso serve para carregar a árvore inicial "Meus Bestiários"
    List<Pasta> findByUsuarioIdAndPastaPaiIsNull(Long usuarioId);
    
    // Buscar pastas públicas raízes (para ver perfis de outros)
    List<Pasta> findByUsuarioIdAndPastaPaiIsNullAndPublicaTrue(Long usuarioId);
}