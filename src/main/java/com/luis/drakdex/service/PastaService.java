package com.luis.drakdex.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luis.drakdex.dto.CriaturaDTO;
import com.luis.drakdex.dto.ItemResponseDTO;
import com.luis.drakdex.dto.PastaRequestDTO;
import com.luis.drakdex.dto.PastaResponseDTO;
import com.luis.drakdex.model.Pasta;
import com.luis.drakdex.model.Usuario;
import com.luis.drakdex.model.enums.CategoriaPasta;
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
        novaPasta.setPublica(dados.publica());
        
        // Se não vier categoria, assume que é Bestiário (CRIATURA) para compatibilidade
        novaPasta.setCategoria(dados.categoria() != null ? dados.categoria() : CategoriaPasta.CRIATURA);

        // LÓGICA DE HIERARQUIA
        if (dados.pastaPaiId() != null) {
            Pasta pai = repository.findById(dados.pastaPaiId())
                    .orElseThrow(() -> new RuntimeException("Pasta pai não encontrada"));

            // Validação de Propriedade
            if (!pai.getUsuario().getId().equals(usuario.getId())) {
                throw new RuntimeException("Você não pode criar subpastas no bestiário de outro caçador!");
            }

            // Validação de Tipo: Não misturar itens com monstros
            if (pai.getCategoria() != novaPasta.getCategoria()) {
                throw new RuntimeException("Você não pode misturar tipos de pastas (Item vs Criatura)!");
            }

            // Validação de Profundidade (Max 3 níveis)
            int nivel = 1;
            Pasta temp = pai;
            while (temp != null) {
                nivel++;
                temp = temp.getPastaPai();
            }
            if (nivel > 3) {
                throw new RuntimeException("Limite de profundidade atingido! (Máximo: 3 níveis)");
            }

            // Herança de Privacidade
            if (!pai.isPublica()) {
                novaPasta.setPublica(false); 
            }

            novaPasta.setPastaPai(pai);
        }

        repository.save(novaPasta);
        return converterParaDTO(novaPasta);
    }

    // LISTAR MEUS (Filtrado por Categoria)
    public List<PastaResponseDTO> listarMinhasPastasRaiz(Usuario usuario, CategoriaPasta categoria) {
        return repository.findByUsuarioIdAndPastaPaiIsNullAndCategoria(usuario.getId(), categoria)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // LISTAR PÚBLICAS (Filtrado por Categoria)
    public List<PastaResponseDTO> listarPublicas(CategoriaPasta categoria) {
        return repository.findByPublicaTrueAndPastaPaiIsNullAndCategoria(categoria)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public PastaResponseDTO buscarPorId(Long id) {
        Pasta pasta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pasta não encontrada"));
        return converterParaDTO(pasta);
    }

    // CONVERSOR RECURSIVO (Agora com Itens!)
    private PastaResponseDTO converterParaDTO(Pasta pasta) {
        List<PastaResponseDTO> filhosDTO = pasta.getSubPastas().stream()
                .filter(filho -> !filho.getId().equals(pasta.getId())) // Evita loop visual
                .map(this::converterParaDTO)
                .collect(Collectors.toList());

        // Converter Criaturas
        List<CriaturaDTO> criaturasDTO = pasta.getCriaturas().stream()
                .map(c -> {
                     CriaturaDTO dto = new CriaturaDTO();
                     dto.setId(c.getId());
                     dto.setNome(c.getNome());
                     dto.setTipo(c.getTipo());
                     dto.setNivel(c.getNivel());
                     dto.setDescricao(c.getDescricao());
                     if (c.getUsuario() != null) dto.setCriadorVulgo(c.getUsuario().getVulgo());
                     return dto;
                }).collect(Collectors.toList());

        // Converter Itens (NOVO)
        List<ItemResponseDTO> itensDTO = pasta.getItens().stream()
                .map(i -> new ItemResponseDTO(
                    i.getId(), i.getNome(), i.getDescricao(), i.getTipo(), i.getRaridade(),
                    i.getPeso(), i.getPreco(), i.getDano(), i.getDefesa(), i.getPropriedades(),
                    i.getUsuario().getVulgo()
                )).collect(Collectors.toList());

        String donoVulgo = pasta.getUsuario() != null ? pasta.getUsuario().getVulgo() : "Desconhecido";

        return new PastaResponseDTO(
            pasta.getId(),
            pasta.getNome(),
            pasta.isPublica(),
            pasta.getPastaPai() != null ? pasta.getPastaPai().getId() : null,
            filhosDTO,
            pasta.getCriaturas().size(),
            criaturasDTO,
            donoVulgo,
            pasta.getCategoria(), // Devolve a categoria
            itensDTO              // Devolve os itens
        );
    }
}