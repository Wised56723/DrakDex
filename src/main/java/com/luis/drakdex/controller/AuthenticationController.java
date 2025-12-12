package com.luis.drakdex.controller;

import com.luis.drakdex.dto.LoginDTO;
import com.luis.drakdex.dto.LoginResponseDTO;
import com.luis.drakdex.dto.RegistroDTO;
import com.luis.drakdex.infra.security.TokenService;
import com.luis.drakdex.model.Usuario;
import com.luis.drakdex.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDTO data) {
        // O Spring Security autentica pra nós
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // Se deu certo, geramos o token
        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        // Devolvemos o Token e o Vulgo para o Frontend
        Usuario usuario = (Usuario) auth.getPrincipal();
        return ResponseEntity.ok(new LoginResponseDTO(token, usuario.getVulgo()));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegistroDTO data) {
        if (this.repository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().body("Email já existe");
        if (this.repository.existsByVulgo(data.vulgo())) return ResponseEntity.badRequest().body("Vulgo já existe");

        // Encriptar a senha antes de salvar
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        
        Usuario newUser = new Usuario();
        newUser.setNomeCompleto(data.nomeCompleto());
        newUser.setEmail(data.email());
        newUser.setVulgo(data.vulgo());
        newUser.setSenha(encryptedPassword);

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}