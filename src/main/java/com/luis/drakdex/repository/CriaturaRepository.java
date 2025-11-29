package com.luis.drakdex.repository; 

// Importante: Estamos a importar do pacote 'drakdex'
import com.luis.drakdex.model.Criatura; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriaturaRepository extends JpaRepository<Criatura, Long> {
}