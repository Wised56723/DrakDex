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

        PastaRequestDTO dto = new PastaRequestDTO("Pasta Raiz", true, null);

        // Simulamos que quando o repositório salvar, ele devolve a pasta com ID 1
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
        // CENÁRIO: Vamos montar uma hierarquia: Avo (1) -> Pai (2) -> Neto (3)
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Pasta avo = new Pasta(); avo.setId(1L); avo.setUsuario(usuario);
        Pasta pai = new Pasta(); pai.setId(2L); pai.setPastaPai(avo); pai.setUsuario(usuario);
        Pasta neto = new Pasta(); neto.setId(3L); neto.setPastaPai(pai); neto.setUsuario(usuario);

        // O usuário tenta criar um filho para o Neto (o Bisneto)
        PastaRequestDTO dtoBisneto = new PastaRequestDTO("Bisneto Ilegal", true, 3L);

        when(repository.findById(3L)).thenReturn(Optional.of(neto));

        // AÇÃO & VERIFICAÇÃO
        // Esperamos que o código lance uma RuntimeException com a mensagem específica
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.criarPasta(dtoBisneto, usuario);
        });

        assertEquals("Limite de profundidade atingido! (Máximo: 3 níveis)", exception.getMessage());
        
        // Garante que NADA foi salvo no banco
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve permitir criar pasta dentro do bestiário de outro usuário")
    void deveBloquearInvasaoDePasta() {
        // CENÁRIO
        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setId(1L); // Eu sou o ID 1

        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(99L); // A pasta pertence ao ID 99

        Pasta pastaAlheia = new Pasta();
        pastaAlheia.setId(5L);
        pastaAlheia.setUsuario(outroUsuario);

        PastaRequestDTO dto = new PastaRequestDTO("Tentativa de Invasão", true, 5L);

        when(repository.findById(5L)).thenReturn(Optional.of(pastaAlheia));

        // AÇÃO & VERIFICAÇÃO
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.criarPasta(dto, usuarioLogado);
        });

        assertEquals("Você não pode criar subpastas no bestiário de outro caçador!", exception.getMessage());
    }

    @Test
    @DisplayName("Deve listar APENAS pastas públicas que são Raiz")
    void deveListarApenasPublicasRaiz() {
        // CENÁRIO
        // Pasta 1: Pública e Raiz (DEVE VIR)
        Pasta p1 = new Pasta(); p1.setId(1L); p1.setPublica(true); p1.setPastaPai(null);
        p1.setUsuario(new Usuario()); // Mock do usuário para evitar NullPointer no DTO

        // Pasta 2: Privada e Raiz (NÃO DEVE VIR)
        Pasta p2 = new Pasta(); p2.setId(2L); p2.setPublica(false); p2.setPastaPai(null);

        // Pasta 3: Pública mas é Subpasta (NÃO DEVE VIR AQUI, só dentro do pai)
        Pasta p3 = new Pasta(); p3.setId(3L); p3.setPublica(true); p3.setPastaPai(p1);

        // Simulamos que o banco devolve tudo misturado
        when(repository.findAll()).thenReturn(List.of(p1, p2, p3));

        // AÇÃO
        var resultado = service.listarPublicas();

        // VERIFICAÇÃO
        assertEquals(1, resultado.size()); // Só deve sobrar 1
        assertEquals(1L, resultado.get(0).id()); // Tem de ser a Pasta 1
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

        // Simulamos o retorno do repositório
        when(repository.findByUsuarioIdAndPastaPaiIsNull(10L))
                .thenReturn(List.of(minhaPasta));

        // AÇÃO
        var resultado = service.listarMinhasPastasRaiz(eu);

        // VERIFICAÇÃO
        assertFalse(resultado.isEmpty());
        assertEquals("Minha Pasta", resultado.get(0).nome());
        verify(repository).findByUsuarioIdAndPastaPaiIsNull(10L); // Confirma que usou o ID certo
    }
}