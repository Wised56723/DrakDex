package com.luis.drakdex.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luis.drakdex.dto.MagiaRequestDTO;
import com.luis.drakdex.dto.MagiaResponseDTO;
import com.luis.drakdex.model.Magia;
import com.luis.drakdex.model.Pasta;
import com.luis.drakdex.model.Usuario;
import com.luis.drakdex.repository.MagiaRepository;
import com.luis.drakdex.repository.PastaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MagiaService {

    private final MagiaRepository repository;
    private final PastaRepository pastaRepository;

    @Transactional
    public MagiaResponseDTO criar(MagiaRequestDTO dados, Usuario usuario) {
        Pasta pasta = pastaRepository.findById(dados.pastaId())
                .orElseThrow(() -> new RuntimeException("Pasta não encontrada"));

        if (!pasta.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para criar magias nesta pasta.");
        }

        Magia magia = new Magia();
        atualizarDados(magia, dados);
        magia.setPasta(pasta);
        magia.setUsuario(usuario);

        repository.save(magia);
        return converterParaDTO(magia);
    }

    @Transactional
    public MagiaResponseDTO atualizar(Long id, MagiaRequestDTO dados, Usuario usuario) {
        Magia magia = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Magia não encontrada"));

        if (!magia.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Sem permissão para editar.");
        }

        atualizarDados(magia, dados);
        repository.save(magia);
        return converterParaDTO(magia);
    }

    public void deletar(Long id, Usuario usuario) {
        Magia magia = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Magia não encontrada"));
        if (!magia.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Sem permissão para deletar.");
        }
        repository.delete(magia);
    }

    private void atualizarDados(Magia magia, MagiaRequestDTO dados) {
        magia.setNome(dados.nome());
        magia.setEscola(dados.escola());
        magia.setTempoExecucao(dados.tempoExecucao());
        magia.setAlcance(dados.alcance());
        magia.setDuracao(dados.duracao());
        magia.setComponentes(dados.componentes());
        magia.setCusto(dados.custo());
        magia.setSistema(dados.sistema());
        magia.setDescricao(dados.descricao());
    }

    public MagiaResponseDTO converterParaDTO(Magia m) {
        return new MagiaResponseDTO(
            m.getId(),
            m.getNome(),
            m.getEscola(),
            m.getTempoExecucao(),
            m.getAlcance(),
            m.getDuracao(),
            m.getComponentes(),
            m.getCusto(),
            m.getSistema(),
            m.getDescricao(),
            m.getUsuario().getVulgo()
        );
    }
}