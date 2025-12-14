package com.luis.drakdex.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luis.drakdex.dto.ItemRequestDTO;
import com.luis.drakdex.dto.ItemResponseDTO;
import com.luis.drakdex.model.Item;
import com.luis.drakdex.model.Usuario;
import com.luis.drakdex.repository.ItemRepository;

@Service
public class ItemService {

    @Autowired
    private ItemRepository repository;

    @Transactional
    public ItemResponseDTO criar(ItemRequestDTO dados, Usuario usuario) {
        Item item = new Item();
        
        // Mapeamento DTO -> Entidade
        item.setNome(dados.nome());
        item.setDescricao(dados.descricao());
        item.setTipo(dados.tipo());
        item.setRaridade(dados.raridade()); // Pode ser null, sem problemas
        item.setPeso(dados.peso());
        item.setPreco(dados.preco());
        item.setDano(dados.dano());
        item.setDefesa(dados.defesa());
        item.setPropriedades(dados.propriedades());
        
        // Dono do Item
        item.setUsuario(usuario);

        repository.save(item);
        
        return converterParaDTO(item);
    }

    public List<ItemResponseDTO> listarMeusItens(Usuario usuario) {
        return repository.findByUsuarioId(usuario.getId())
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar para converter Entidade -> DTO de Resposta
    private ItemResponseDTO converterParaDTO(Item item) {
        return new ItemResponseDTO(
            item.getId(),
            item.getNome(),
            item.getDescricao(),
            item.getTipo(),
            item.getRaridade(),
            item.getPeso(),
            item.getPreco(),
            item.getDano(),
            item.getDefesa(),
            item.getPropriedades(),
            item.getUsuario().getVulgo()
        );
    }
}