package com.luis.drakdex.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luis.drakdex.dto.CriaturaResponseDTO;
import com.luis.drakdex.dto.ItemResponseDTO;
import com.luis.drakdex.dto.MagiaResponseDTO;
import com.luis.drakdex.dto.NpcResponseDTO;
import com.luis.drakdex.dto.PastaRequestDTO;
import com.luis.drakdex.dto.PastaResponseDTO;
import com.luis.drakdex.model.Pasta;
import com.luis.drakdex.model.Usuario;
import com.luis.drakdex.model.enums.CategoriaPasta;
import com.luis.drakdex.repository.PastaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PastaService {

    private final PastaRepository repository;

    @Transactional
    public PastaResponseDTO criarPasta(PastaRequestDTO dados, Usuario usuario) {
        Pasta novaPasta = new Pasta();
        novaPasta.setNome(dados.nome());
        novaPasta.setPublica(dados.publica());
        novaPasta.setCategoria(dados.categoria());
        novaPasta.setUsuario(usuario);

        if (dados.pastaPaiId() != null) {
            Pasta pai = repository.findById(dados.pastaPaiId())
                    .orElseThrow(() -> new RuntimeException("Pasta pai não encontrada"));
            
            if (!pai.getUsuario().getId().equals(usuario.getId())) {
                throw new RuntimeException("Você não pode criar subpastas na pasta de outro usuário.");
            }
            if (pai.getCategoria() != dados.categoria()) {
                throw new RuntimeException("A subpasta deve ser da mesma categoria da pasta pai.");
            }
            
            int nivel = 0;
            Pasta temp = pai;
            while (temp != null) {
                nivel++;
                temp = temp.getPastaPai();
                if (nivel > 3) throw new RuntimeException("Limite de profundidade atingido!");
            }

            novaPasta.setPastaPai(pai);
        }

        repository.save(novaPasta);
        return converterParaDTO(novaPasta);
    }

    public List<PastaResponseDTO> listarMinhasPastas(Usuario usuario, CategoriaPasta tipo) {
        List<Pasta> pastas = repository.findByUsuarioIdAndPastaPaiIsNullAndCategoria(usuario.getId(), tipo);
        return pastas.stream().map(this::converterParaDTO).toList();
    }

    public List<PastaResponseDTO> listarPublicas(CategoriaPasta tipo) {
        List<Pasta> pastas = repository.findByPublicaTrueAndPastaPaiIsNullAndCategoria(tipo);
        return pastas.stream().map(this::converterParaDTO).toList();
    }

    public PastaResponseDTO buscarPorId(Long id) {
        Pasta pasta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pasta não encontrada"));
        return converterParaDTO(pasta);
    }

    @Transactional
    public PastaResponseDTO atualizar(Long id, PastaRequestDTO dados, Usuario usuario) {
        Pasta pasta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pasta não encontrada"));

        if (!pasta.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Sem permissão para editar esta pasta.");
        }

        pasta.setNome(dados.nome());
        pasta.setPublica(dados.publica());
        
        repository.save(pasta);
        return converterParaDTO(pasta);
    }

    @Transactional
    public void deletar(Long id, Usuario usuario) {
        Pasta pasta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pasta não encontrada"));

        if (!pasta.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Sem permissão para deletar esta pasta.");
        }

        repository.delete(pasta);
    }

    // --- CONVERSOR ENTIDADE -> DTO (CORRIGIDO) ---
    private PastaResponseDTO converterParaDTO(Pasta pasta) {
        
        var criaturasDTO = pasta.getCriaturas().stream()
            .map(c -> new CriaturaResponseDTO(
                c.getId(), c.getNome(), c.getDescricao(), c.getNivel(), 
                c.getTipo(), c.getImagemUrl(), c.getUsuario().getVulgo()
            )).toList();

        
        var itensDTO = pasta.getItens().stream()
            .map(i -> new ItemResponseDTO(
                i.getId(), 
                i.getNome(), 
                i.getDescricao(), 
                i.getTipo().name(),      
                i.getRaridade().name(),  
                i.getPeso(), 
                i.getPreco(), 
                i.getDano(), 
                i.getDefesa(), 
                i.getPropriedades(), 
                i.getImagemUrl(), 
                i.getUsuario().getVulgo()
            )).toList();

        var magiasDTO = pasta.getMagias().stream()
            .map(m -> new MagiaResponseDTO(
                m.getId(), m.getNome(), m.getEscola(), m.getTempoExecucao(),
                m.getAlcance(), m.getDuracao(), m.getComponentes(),
                m.getCusto(), m.getSistema(), m.getDescricao(),
                m.getUsuario().getVulgo()
            )).toList();

        var npcsDTO = pasta.getNpcs().stream()
            .map(n -> new NpcResponseDTO(
                n.getId(), n.getNome(), n.getTipoFicha(), n.getAparencia(),
                n.getPersonalidade(), n.getHistoria(), n.getNivelDesafio(),
                n.getClasseArmadura(), n.getPontosVida(), n.getForca(),
                n.getDestreza(), n.getConstituicao(), n.getInteligencia(),
                n.getSabedoria(), n.getCarisma(), n.getRegrasCustomizadas(),
                List.of(), // Listas vazias por enquanto
                List.of(),
                n.getUsuario().getVulgo()
            )).toList();

        return new PastaResponseDTO(
                pasta.getId(),
                pasta.getNome(),
                pasta.isPublica(),
                pasta.getPastaPai() != null ? pasta.getPastaPai().getId() : null,
                pasta.getCategoria().name(),
                pasta.getSubPastas().stream().map(this::converterParaDTO).toList(),
                criaturasDTO,
                itensDTO,
                magiasDTO,
                npcsDTO,
                pasta.getUsuario().getVulgo()
        );
    }
}