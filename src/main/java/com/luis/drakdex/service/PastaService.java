package com.luis.drakdex.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luis.drakdex.dto.PastaRequestDTO;
import com.luis.drakdex.dto.PastaResponseDTO;
import com.luis.drakdex.model.Pasta;
import com.luis.drakdex.model.Usuario;
import com.luis.drakdex.repository.PastaRepository;

@Service
public class PastaService {

    @Autowired
    private PastaRepository repository;

    @Transactional
    public PastaResponseDTO criarPasta(PastaRequestDTO dados, Usuario usuario) {
        Pasta novaPasta = new Pasta();
        novaPasta.setNome(dados.nome());
        novaPasta.setUsuario(usuario);
        
        // REGRA DE PRIVACIDADE INICIAL
        novaPasta.setPublica(dados.publica());

        // LÓGICA DE HIERARQUIA
        if (dados.pastaPaiId() != null) {
            Pasta pai = repository.findById(dados.pastaPaiId())
                    .orElseThrow(() -> new RuntimeException("Pasta pai não encontrada"));

            // Validação: Só posso criar subpastas nas minhas próprias pastas
            if (!pai.getUsuario().getId().equals(usuario.getId())) {
                throw new RuntimeException("Você não pode criar subpastas no bestiário de outro caçador!");
            }

            // REGRA 1: LIMITE DE 3 CAMADAS
            // Nível 1 (Raiz) -> Nível 2 (Filho) -> Nível 3 (Neto)
            // Se o pai já tiver um pai, e esse avô tiver outro pai... bloqueia.
            int nivel = 1;
            Pasta temp = pai;
            while (temp != null) {
                nivel++;
                temp = temp.getPastaPai();
            }
            
            if (nivel > 3) {
                throw new RuntimeException("Limite de profundidade atingido! (Máximo: 3 níveis)");
            }

            // REGRA 2: HERANÇA DE PRIVACIDADE
            // Se o pai é privado, o filho OBRIGATORIAMENTE é privado.
            if (!pai.isPublica()) {
                novaPasta.setPublica(false); 
            }

            novaPasta.setPastaPai(pai);
        }

        repository.save(novaPasta);
        return converterParaDTO(novaPasta);
    }

    public List<PastaResponseDTO> listarMinhasPastasRaiz(Usuario usuario) {
        return repository.findByUsuarioIdAndPastaPaiIsNull(usuario.getId())
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar recursivo para montar a árvore
    private PastaResponseDTO converterParaDTO(Pasta pasta) {
        List<PastaResponseDTO> filhosDTO = pasta.getSubPastas().stream()
                .map(this::converterParaDTO) // Recursividade mágica aqui
                .collect(Collectors.toList());

        return new PastaResponseDTO(
            pasta.getId(),
            pasta.getNome(),
            pasta.isPublica(),
            pasta.getPastaPai() != null ? pasta.getPastaPai().getId() : null,
            filhosDTO,
            pasta.getCriaturas().size()
        );
    }
}