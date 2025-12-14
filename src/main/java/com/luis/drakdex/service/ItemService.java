package com.luis.drakdex.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luis.drakdex.dto.ItemRequestDTO;
import com.luis.drakdex.dto.ItemResponseDTO;
import com.luis.drakdex.model.Item;
import com.luis.drakdex.model.Pasta;
import com.luis.drakdex.model.Usuario;
import com.luis.drakdex.model.enums.CategoriaPasta;
import com.luis.drakdex.repository.ItemRepository;
import com.luis.drakdex.repository.PastaRepository;

@Service
public class ItemService {

    @Autowired
    private ItemRepository repository;

    @Autowired
    private PastaRepository pastaRepository; // Necessário para validar a pasta

    @Transactional
    public ItemResponseDTO criar(ItemRequestDTO dados, Usuario usuario) {
        Item item = new Item();
        copiarDados(item, dados);
        item.setUsuario(usuario);

        // LÓGICA DE VÍNCULO COM PASTA
        if (dados.pastaId() != null) {
            Pasta pasta = pastaRepository.findById(dados.pastaId())
                    .orElseThrow(() -> new RuntimeException("Pasta não encontrada"));

            // Segurança: A pasta é minha?
            if (!pasta.getUsuario().getId().equals(usuario.getId())) {
                throw new RuntimeException("Você não pode adicionar itens na pasta de outro usuário!");
            }

            // Validação de Categoria: É uma pasta de Itens?
            if (pasta.getCategoria() != CategoriaPasta.ITEM) {
                throw new RuntimeException("Esta pasta é para Criaturas, não para Itens!");
            }

            item.setPasta(pasta);
        }

        repository.save(item);
        return converterParaDTO(item);
    }

    public List<ItemResponseDTO> listarMeusItens(Usuario usuario) {
        return repository.findByUsuarioId(usuario.getId())
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public ItemResponseDTO buscarPorId(Long id, Usuario usuario) {
        Item item = buscarItemValidado(id, usuario);
        return converterParaDTO(item);
    }

    @Transactional
    public ItemResponseDTO atualizar(Long id, ItemRequestDTO dados, Usuario usuario) {
        Item item = buscarItemValidado(id, usuario);
        copiarDados(item, dados);
        return converterParaDTO(item);
    }

    @Transactional
    public void deletar(Long id, Usuario usuario) {
        Item item = buscarItemValidado(id, usuario);
        repository.delete(item);
    }

    // --- MÉTODOS AUXILIARES ---

    private Item buscarItemValidado(Long id, Usuario usuario) {
        Item item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));
        if (!item.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para mexer neste item!");
        }
        return item;
    }

    private void copiarDados(Item item, ItemRequestDTO dados) {
        item.setNome(dados.nome());
        item.setDescricao(dados.descricao());
        item.setTipo(dados.tipo());
        item.setRaridade(dados.raridade());
        item.setPeso(dados.peso());
        item.setPreco(dados.preco());
        item.setDano(dados.dano());
        item.setDefesa(dados.defesa());
        item.setPropriedades(dados.propriedades());
    }

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