package com.luis.drakdex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luis.drakdex.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // Buscar itens de um usuário específico
    List<Item> findByUsuarioId(Long usuarioId);
}