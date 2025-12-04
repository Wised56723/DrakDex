package com.luis.drakdex.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luis.drakdex.model.Criatura;

@Repository
public interface CriaturaRepository extends JpaRepository<Criatura, Long> {

    // Agora retornam 'Page' em vez de 'List' e aceitam 'Pageable'
    Page<Criatura> findByTipo(String tipo, Pageable pageable);

    Page<Criatura> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}