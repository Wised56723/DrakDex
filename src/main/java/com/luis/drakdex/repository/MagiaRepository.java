package com.luis.drakdex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luis.drakdex.model.Magia;

public interface MagiaRepository extends JpaRepository<Magia, Long> {
    // Busca todas as magias dentro de uma pasta específica
    List<Magia> findByPastaId(Long pastaId);
}