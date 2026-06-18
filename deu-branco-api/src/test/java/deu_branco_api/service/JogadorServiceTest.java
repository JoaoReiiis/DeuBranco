package deu_branco_api.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import deu_branco_api.model.Jogador;
import deu_branco_api.model.Role;
import deu_branco_api.repository.JogadorRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class JogadorServiceTest {

    @Mock
    private JogadorRepository jogadorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private JogadorService jogadorService;

    @BeforeEach
    void setUp() {
        jogadorService = new JogadorService(jogadorRepository, passwordEncoder);
    }

    @Test
    void deveImpedirCriacaoComEmailDuplicado() {
        Jogador novoJogador = jogador(null, "Joao Reis", "joao@email.com", "12345678");

        when(jogadorRepository.existsByEmail("joao@email.com")).thenReturn(true);

        try {
            jogadorService.criar(novoJogador);
            fail("Deveria lancar erro para email duplicado.");
        } catch (IllegalArgumentException exception) {
        }

        verify(jogadorRepository).existsByEmail("joao@email.com");
        verify(jogadorRepository, never()).save(novoJogador);
    }

    @Test
    void deveGerarHashDaSenhaAoCriarJogador() {
        Jogador novoJogador = jogador(null, "Joao Reis", "joao@email.com", "12345678");

        when(jogadorRepository.existsByEmail("joao@email.com")).thenReturn(false);
        when(passwordEncoder.encode("12345678")).thenReturn("senha-hash");
        when(jogadorRepository.save(novoJogador)).thenReturn(novoJogador);

        Jogador jogadorCriado = jogadorService.criar(novoJogador);

        assertThat(jogadorCriado.getSenha()).isEqualTo("senha-hash");
        assertThat(jogadorCriado.getRole()).isEqualTo(Role.JOGADOR);
        verify(passwordEncoder).encode("12345678");
        verify(jogadorRepository).save(novoJogador);
    }

    @Test
    void deveBuscarJogadorPorEmail() {
        Jogador jogador = jogador(1L, "Joao Reis", "joao@email.com", "senha-hash");

        when(jogadorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(jogador));

        Jogador jogadorEncontrado = jogadorService.buscarPorEmail("joao@email.com");

        assertThat(jogadorEncontrado).isSameAs(jogador);
    }

    @Test
    void deveLancarErroQuandoBuscarEmailInexistente() {
        when(jogadorRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        try {
            jogadorService.buscarPorEmail("naoexiste@email.com");
            fail("Deveria lancar erro para jogador inexistente.");
        } catch (EntityNotFoundException exception) {
        }
    }

    @Test
    void deveImpedirUpdateComEmailDuplicado() {
        Jogador jogadorExistente = jogador(1L, "Joao Reis", "joao@email.com", "senha-hash");
        Jogador dadosAtualizados = jogador(null, "Joao Atualizado", "maria@email.com", null);

        when(jogadorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(jogadorExistente));
        when(jogadorRepository.existsByEmailAndIdNot("maria@email.com", 1L)).thenReturn(true);

        try {
            jogadorService.atualizarJogadorAutenticado("joao@email.com", dadosAtualizados);
            fail("Deveria lancar erro para email duplicado.");
        } catch (IllegalArgumentException exception) {
        }

        verify(jogadorRepository).existsByEmailAndIdNot("maria@email.com", 1L);
        verify(jogadorRepository, never()).save(jogadorExistente);
    }

    @Test
    void deveAlterarNomeEmailESenhaDaConta() {
        Jogador jogadorExistente = jogador(1L, "Joao Reis", "joao@email.com", "senha-antiga-hash");
        Jogador dadosAtualizados = jogador(null, "Joao Atualizado", "joao.novo@email.com", "novaSenha123");

        when(jogadorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(jogadorExistente));
        when(jogadorRepository.existsByEmailAndIdNot("joao.novo@email.com", 1L)).thenReturn(false);
        when(passwordEncoder.encode("novaSenha123")).thenReturn("nova-senha-hash");
        when(jogadorRepository.save(jogadorExistente)).thenReturn(jogadorExistente);

        Jogador jogadorAtualizado = jogadorService.atualizarJogadorAutenticado("joao@email.com", dadosAtualizados);

        assertThat(jogadorAtualizado.getNome()).isEqualTo("Joao Atualizado");
        assertThat(jogadorAtualizado.getEmail()).isEqualTo("joao.novo@email.com");
        assertThat(jogadorAtualizado.getSenha()).isEqualTo("nova-senha-hash");
    }

    @Test
    void deveManterSenhaAtualQuandoAlterarContaSemEnviarSenha() {
        Jogador jogadorExistente = jogador(1L, "Joao Reis", "joao@email.com", "senha-antiga-hash");
        Jogador dadosAtualizados = jogador(null, "Joao Atualizado", "joao@email.com", null);

        when(jogadorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(jogadorExistente));
        when(jogadorRepository.existsByEmailAndIdNot("joao@email.com", 1L)).thenReturn(false);
        when(jogadorRepository.save(jogadorExistente)).thenReturn(jogadorExistente);

        Jogador jogadorAtualizado = jogadorService.atualizarJogadorAutenticado("joao@email.com", dadosAtualizados);

        assertThat(jogadorAtualizado.getNome()).isEqualTo("Joao Atualizado");
        assertThat(jogadorAtualizado.getSenha()).isEqualTo("senha-antiga-hash");
    }

    @Test
    void deveRemoverJogadorAutenticado() {
        Jogador jogador = jogador(1L, "Joao Reis", "joao@email.com", "senha-hash");

        when(jogadorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(jogador));

        jogadorService.removerJogadorAutenticado("joao@email.com");

        verify(jogadorRepository).delete(jogador);
    }

    @Test
    void devePermitirAdminAlterarNomeDeOutroJogador() {
        Jogador admin = jogador(1L, "Admin", "admin@email.com", "senha-hash", Role.ADMIN);
        Jogador jogador = jogador(2L, "Joao Reis", "joao@email.com", "senha-hash");

        when(jogadorRepository.findByEmail("admin@email.com")).thenReturn(Optional.of(admin));
        when(jogadorRepository.findById(2L)).thenReturn(Optional.of(jogador));
        when(jogadorRepository.save(jogador)).thenReturn(jogador);

        Jogador jogadorAtualizado = jogadorService.atualizarNomeDeOutroJogador(
                "admin@email.com", 2L, "  Joao Atualizado  ");

        assertThat(jogadorAtualizado.getNome()).isEqualTo("Joao Atualizado");
        verify(jogadorRepository).save(jogador);
    }

    @Test
    void deveImpedirAdminAlterarNomePeloEndpointDeOutroJogadorQuandoForEleMesmo() {
        Jogador admin = jogador(1L, "Admin", "admin@email.com", "senha-hash", Role.ADMIN);

        when(jogadorRepository.findByEmail("admin@email.com")).thenReturn(Optional.of(admin));
        when(jogadorRepository.findById(1L)).thenReturn(Optional.of(admin));

        try {
            jogadorService.atualizarNomeDeOutroJogador("admin@email.com", 1L, "Admin Novo");
            fail("Deveria lancar erro ao tentar atualizar a propria conta pelo endpoint de outro jogador.");
        } catch (IllegalArgumentException exception) {
        }

        verify(jogadorRepository, never()).save(admin);
    }

    @Test
    void devePermitirAdminRemoverOutroJogador() {
        Jogador admin = jogador(1L, "Admin", "admin@email.com", "senha-hash", Role.ADMIN);
        Jogador jogador = jogador(2L, "Joao Reis", "joao@email.com", "senha-hash");

        when(jogadorRepository.findByEmail("admin@email.com")).thenReturn(Optional.of(admin));
        when(jogadorRepository.findById(2L)).thenReturn(Optional.of(jogador));

        jogadorService.removerOutroJogador("admin@email.com", 2L);

        verify(jogadorRepository).delete(jogador);
    }

    @Test
    void deveImpedirAdminRemoverEleMesmoPeloEndpointDeOutroJogador() {
        Jogador admin = jogador(1L, "Admin", "admin@email.com", "senha-hash", Role.ADMIN);

        when(jogadorRepository.findByEmail("admin@email.com")).thenReturn(Optional.of(admin));
        when(jogadorRepository.findById(1L)).thenReturn(Optional.of(admin));

        try {
            jogadorService.removerOutroJogador("admin@email.com", 1L);
            fail("Deveria lancar erro ao tentar remover a propria conta pelo endpoint de outro jogador.");
        } catch (IllegalArgumentException exception) {
        }

        verify(jogadorRepository, never()).delete(admin);
    }

    private Jogador jogador(Long id, String nome, String email, String senha) {
        return jogador(id, nome, email, senha, Role.JOGADOR);
    }

    private Jogador jogador(Long id, String nome, String email, String senha, Role role) {
        Jogador jogador = new Jogador();
        jogador.setId(id);
        jogador.setNome(nome);
        jogador.setEmail(email);
        jogador.setSenha(senha);
        jogador.setRole(role);
        return jogador;
    }
}
