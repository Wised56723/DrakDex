package com.luis.drakdex.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luis.drakdex.dto.ItemResponseDTO;
import com.luis.drakdex.dto.MagiaResponseDTO;
import com.luis.drakdex.dto.NpcRequestDTO;
import com.luis.drakdex.dto.NpcResponseDTO;
import com.luis.drakdex.model.Item;
import com.luis.drakdex.model.Magia;
import com.luis.drakdex.model.Npc;
import com.luis.drakdex.model.Pasta;
import com.luis.drakdex.model.Usuario;
import com.luis.drakdex.repository.ItemRepository;
import com.luis.drakdex.repository.MagiaRepository;
import com.luis.drakdex.repository.NpcRepository;
import com.luis.drakdex.repository.PastaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NpcService {

    private final NpcRepository repository;
    private final PastaRepository pastaRepository;
    private final ItemRepository itemRepository;
    private final MagiaRepository magiaRepository;

    @Transactional
    public NpcResponseDTO criar(NpcRequestDTO dados, Usuario usuario) {
        Pasta pasta = pastaRepository.findById(dados.pastaId())
                .orElseThrow(() -> new RuntimeException("Pasta não encontrada"));

        if (!pasta.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Sem permissão nesta pasta.");
        }

        Npc npc = new Npc();
        npc.setPasta(pasta);
        npc.setUsuario(usuario);
        
        atualizarDadosNpc(npc, dados);
        
        repository.save(npc);
        return converterParaDTO(npc);
    }

    @Transactional
    public NpcResponseDTO atualizar(Long id, NpcRequestDTO dados, Usuario usuario) {
        Npc npc = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("NPC não encontrado"));

        if (!npc.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Sem permissão para editar.");
        }

        atualizarDadosNpc(npc, dados);
        repository.save(npc);
        return converterParaDTO(npc);
    }

    @Transactional
    public void deletar(Long id, Usuario usuario) {
        Npc npc = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("NPC não encontrado"));
        if (!npc.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Sem permissão para deletar.");
        }
        repository.delete(npc);
    }

    private void atualizarDadosNpc(Npc npc, NpcRequestDTO dados) {
        // Campos Básicos
        npc.setNome(dados.nome());
        npc.setTipoFicha(dados.tipoFicha());
        npc.setAparencia(dados.aparencia());
        npc.setPersonalidade(dados.personalidade());
        npc.setHistoria(dados.historia());

        // Campos D&D 5e
        npc.setNivelDesafio(dados.nivelDesafio());
        npc.setClasseArmadura(dados.classeArmadura());
        npc.setPontosVida(dados.pontosVida());
        npc.setForca(dados.forca());
        npc.setDestreza(dados.destreza());
        npc.setConstituicao(dados.constituicao());
        npc.setInteligencia(dados.inteligencia());
        npc.setSabedoria(dados.sabedoria());
        npc.setCarisma(dados.carisma());

        // Campos Livres
        npc.setRegrasCustomizadas(dados.regrasCustomizadas());

        // --- RELACIONAMENTOS (A parte complexa) ---
        
        // 1. Vincular Equipamentos
        if (dados.equipamentosIds() != null && !dados.equipamentosIds().isEmpty()) {
            List<Item> itens = itemRepository.findAllById(dados.equipamentosIds());
            npc.setEquipamento(itens);
        } else {
            npc.setEquipamento(new ArrayList<>());
        }

        // 2. Vincular Magias
        if (dados.magiasIds() != null && !dados.magiasIds().isEmpty()) {
            List<Magia> magias = magiaRepository.findAllById(dados.magiasIds());
            npc.setMagiasConhecidas(magias);
        } else {
            npc.setMagiasConhecidas(new ArrayList<>());
        }
    }

    public NpcResponseDTO converterParaDTO(Npc npc) {
        // Converter a lista de Itens (Entidade) para DTOs
        var equipamentosDTO = npc.getEquipamento().stream()
            .map(i -> new ItemResponseDTO(
                i.getId(), i.getNome(), i.getDescricao(), 
                i.getTipo().name(), i.getRaridade().name(), 
                i.getPeso(), i.getPreco(), i.getDano(), 
                i.getDefesa(), i.getPropriedades(), i.getImagemUrl(), 
                i.getUsuario().getVulgo()
            )).toList();

        // Converter a lista de Magias (Entidade) para DTOs
        var magiasDTO = npc.getMagiasConhecidas().stream()
            .map(m -> new MagiaResponseDTO(
                m.getId(), m.getNome(), m.getEscola(), m.getTempoExecucao(),
                m.getAlcance(), m.getDuracao(), m.getComponentes(),
                m.getCusto(), m.getSistema(), m.getDescricao(),
                m.getUsuario().getVulgo()
            )).toList();

        return new NpcResponseDTO(
            npc.getId(), npc.getNome(), npc.getTipoFicha(),
            npc.getAparencia(), npc.getPersonalidade(), npc.getHistoria(),
            npc.getNivelDesafio(), npc.getClasseArmadura(), npc.getPontosVida(),
            npc.getForca(), npc.getDestreza(), npc.getConstituicao(),
            npc.getInteligencia(), npc.getSabedoria(), npc.getCarisma(),
            npc.getRegrasCustomizadas(),
            equipamentosDTO, // Agora retornamos a lista cheia!
            magiasDTO,       // Agora retornamos a lista cheia!
            npc.getUsuario().getVulgo()
        );
    }
}