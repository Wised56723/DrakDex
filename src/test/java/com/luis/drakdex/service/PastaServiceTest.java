package com.luis.drakdex.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.luis.drakdex.dto.PastaRequestDTO;
import com.luis.drakdex.model.Pasta;
import com.luis.drakdex.model.Usuario;
import com.luis.drakdex.model.enums.CategoriaPasta;
import com.luis.drakdex.repository.PastaRepository;

@ExtendWith(MockitoExtension.class)
class PastaServiceTest {

    @Mock
    private PastaRepository repository;

    @InjectMocks
    private PastaService service;

    @Test
    @DisplayName("Deve criar uma pasta raiz com sucesso")
    void deveCriarPastaRaiz() {
        // CENÁRIO
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setVulgo("CaçadorTeste");

        // Atualizado: Adicionado CategoriaPasta.CRIATURA
        PastaRequestDTO dto = new PastaRequestDTO("Pasta Raiz", true, null, CategoriaPasta.CRIATURA);

        when(repository.save(any(Pasta.class))).thenAnswer(invocation -> {
            Pasta p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        // AÇÃO
        var resultado = service.criarPasta(dto, usuario);

        // VERIFICAÇÃO
        assertNotNull(resultado);
        assertEquals("Pasta Raiz", resultado.nome());
        assertNull(resultado.pastaPaiId());
        verify(repository, times(1)).save(any(Pasta.class));
    }

    @Test
    @DisplayName("Deve BLOQUEAR a criação de uma 4ª camada de pastas (Bisneto)")
    void deveBloquearQuartaCamada() {
        // CENÁRIO
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        // Hierarquia configurada com Categoria correta
        Pasta avo = new Pasta(); avo.setId(1L); avo.setUsuario(usuario); avo.setCategoria(CategoriaPasta.CRIATURA);
        Pasta pai = new Pasta(); pai.setId(2L); pai.setPastaPai(avo); pai.setUsuario(usuario); pai.setCategoria(CategoriaPasta.CRIATURA);
        Pasta neto = new Pasta(); neto.setId(3L); neto.setPastaPai(pai); neto.setUsuario(usuario); neto.setCategoria(CategoriaPasta.CRIATURA);

        // Atualizado: Adicionado CategoriaPasta.CRIATURA
        PastaRequestDTO dtoBisneto = new PastaRequestDTO("Bisneto Ilegal", true, 3L, CategoriaPasta.CRIATURA);

        when(repository.findById(3L)).thenReturn(Optional.of(neto));

        // AÇÃO & VERIFICAÇÃO
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.criarPasta(dtoBisneto, usuario);
        });

        assertEquals("Limite de profundidade atingido! (Máximo: 3 níveis)", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve permitir criar pasta dentro do bestiário de outro usuário")
    void deveBloquearInvasaoDePasta() {
        // CENÁRIO
        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setId(1L);

        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(99L);

        Pasta pastaAlheia = new Pasta();
        pastaAlheia.setId(5L);
        pastaAlheia.setUsuario(outroUsuario);
        pastaAlheia.setCategoria(CategoriaPasta.CRIATURA); // Importante definir a categoria

        // Atualizado: Adicionado CategoriaPasta.CRIATURA
        PastaRequestDTO dto = new PastaRequestDTO("Tentativa de Invasão", true, 5L, CategoriaPasta.CRIATURA);

        when(repository.findById(5L)).thenReturn(Optional.of(pastaAlheia));

        // AÇÃO & VERIFICAÇÃO
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.criarPasta(dto, usuarioLogado);
        });

        assertEquals("Você não pode criar subpastas no bestiário de outro caçador!", exception.getMessage());
    }

    @Test
    @DisplayName("Deve listar APENAS pastas públicas que são Raiz e da categoria correta")
    void deveListarApenasPublicasRaiz() {
        // CENÁRIO
        Pasta p1 = new Pasta(); p1.setId(1L); p1.setPublica(true); p1.setPastaPai(null);
        p1.setUsuario(new Usuario());
        p1.setCategoria(CategoriaPasta.CRIATURA);

        // Atualizado: Mockando o novo método do repositório que filtra por categoria
        when(repository.findByPublicaTrueAndPastaPaiIsNullAndCategoria(CategoriaPasta.CRIATURA))
                .thenReturn(List.of(p1));

        // AÇÃO (Atualizado: Passando a categoria)
        var resultado = service.listarPublicas(CategoriaPasta.CRIATURA);

        // VERIFICAÇÃO
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).id());
    }

    @Test
    @DisplayName("Deve listar apenas as pastas raiz do meu usuário")
    void deveListarMinhasPastas() {
        // CENÁRIO
        Usuario eu = new Usuario(); eu.setId(10L); eu.setVulgo("Eu");
        
        Pasta minhaPasta = new Pasta(); 
        minhaPasta.setId(5L); 
        minhaPasta.setNome("Minha Pasta");
        minhaPasta.setUsuario(eu);
        minhaPasta.setCategoria(CategoriaPasta.CRIATURA);

        // Atualizado: Mockando o método com Categoria
        when(repository.findByUsuarioIdAndPastaPaiIsNullAndCategoria(10L, CategoriaPasta.CRIATURA))
                .thenReturn(List.of(minhaPasta));

        // AÇÃO (Atualizado: Passando a categoria)
        var resultado = service.listarMinhasPastasRaiz(eu, CategoriaPasta.CRIATURA);

        // VERIFICAÇÃO
        assertFalse(resultado.isEmpty());
        assertEquals("Minha Pasta", resultado.get(0).nome());
        
        // Verifica se chamou o método novo
        verify(repository).findByUsuarioIdAndPastaPaiIsNullAndCategoria(10L, CategoriaPasta.CRIATURA);
    }
}