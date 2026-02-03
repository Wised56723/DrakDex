package com.luis.drakdex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luis.drakdex.model.Npc;

public interface NpcRepository extends JpaRepository<Npc, Long> {
    List<Npc> findByPastaId(Long pastaId);
}