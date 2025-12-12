package com.luis.drakdex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.luis.drakdex.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método usado pelo Spring Security para achar o usuário no Login
    UserDetails findByEmail(String email);
    
    // Para validar se o vulgo já existe no cadastro
    boolean existsByVulgo(String vulgo);
}