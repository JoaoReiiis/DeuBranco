package deu_branco_api.controller;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import deu_branco_api.model.Disciplina;
import deu_branco_api.model.Instituicao;
import deu_branco_api.model.Jogador;
import deu_branco_api.model.Participacao;
import deu_branco_api.model.Partida;
import deu_branco_api.model.PartidaQuestao;
import deu_branco_api.model.PartidaStatus;
import deu_branco_api.model.Questao;
import deu_branco_api.model.Resposta;
import deu_branco_api.model.Role;
import deu_branco_api.service.PartidaService;
import deu_branco_api.service.PartidaWebSocketPublisher;

@WebMvcTest(PartidaController.class)
@AutoConfigureMockMvc
class PartidaControllerTest {

    private static final String EMAIL = "joao@email.com";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PartidaService partidaService;

    @MockitoBean
    private PartidaWebSocketPublisher partidaWebSocketPublisher;

    @Test
    void deveCriarPartida() throws Exception {
        Partida partida = partida(10L, PartidaStatus.AGUARDANDO);

        when(partidaService.criarPartida(eq(EMAIL), eq(10), eq(List.of(Disciplina.MATEMATICA)), eq(30)))
                .thenReturn(partida);

        mockMvc.perform(post("/partidas")
                .with(jwt().jwt(jwt -> jwt.subject(EMAIL)))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "numeroQuestoes": 10,
                          "disciplinas": ["MATEMATICA"],
                          "tempoDeJogo": 30
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/partidas/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.codigoPin").value("ABC123"))
                .andExpect(jsonPath("$.statusSala").value("AGUARDANDO"))
                .andExpect(jsonPath("$.host.email").value(EMAIL));

        verify(partidaService).criarPartida(EMAIL, 10, List.of(Disciplina.MATEMATICA), 30);
    }

    @Test
    void deveRejeitarCriacaoComNumeroDeQuestoesInvalido() throws Exception {
        mockMvc.perform(post("/partidas")
                .with(jwt().jwt(jwt -> jwt.subject(EMAIL)))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "numeroQuestoes": 4,
                          "disciplinas": ["MATEMATICA"],
                          "tempoDeJogo": 30
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveEntrarNaPartidaPorPin() throws Exception {
        Participacao participacao = participacao(30L, partida(10L, PartidaStatus.AGUARDANDO), jogador(2L, EMAIL), 0);

        when(partidaService.entrarNaPartida(EMAIL, "ABC123")).thenReturn(participacao);

        mockMvc.perform(post("/partidas/entrar")
                .with(jwt().jwt(jwt -> jwt.subject(EMAIL)))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "codigoPin": "ABC123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(30))
                .andExpect(jsonPath("$.partidaId").value(10))
                .andExpect(jsonPath("$.jogador.email").value(EMAIL))
                .andExpect(jsonPath("$.scorePartida").value(0));
    }

    @Test
    void deveIniciarCancelarEFinalizarPartida() throws Exception {
        Partida emAndamento = partida(10L, PartidaStatus.ANDAMENTO);
        Partida cancelada = partida(10L, PartidaStatus.CANCELADA);
        Partida finalizada = partida(10L, PartidaStatus.FINALIZADA);

        when(partidaService.iniciarPartida(EMAIL, 10L)).thenReturn(emAndamento);
        when(partidaService.cancelarPartida(EMAIL, 10L)).thenReturn(cancelada);
        when(partidaService.finalizarPartida(EMAIL, 10L)).thenReturn(finalizada);

        mockMvc.perform(post("/partidas/10/iniciar").with(jwt().jwt(jwt -> jwt.subject(EMAIL))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusSala").value("ANDAMENTO"));

        mockMvc.perform(post("/partidas/10/cancelar").with(jwt().jwt(jwt -> jwt.subject(EMAIL))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusSala").value("CANCELADA"));

        mockMvc.perform(post("/partidas/10/finalizar").with(jwt().jwt(jwt -> jwt.subject(EMAIL))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusSala").value("FINALIZADA"));
    }

    @Test
    void deveListarQuestoesDaPartidaSemGabarito() throws Exception {
        PartidaQuestao partidaQuestao = partidaQuestao(50L, partida(10L, PartidaStatus.ANDAMENTO), questao(40L));

        when(partidaService.listarQuestoesDaPartida(EMAIL, 10L)).thenReturn(List.of(partidaQuestao));

        mockMvc.perform(get("/partidas/10/questoes").with(jwt().jwt(jwt -> jwt.subject(EMAIL))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(50))
                .andExpect(jsonPath("$[0].questaoId").value(40))
                .andExpect(jsonPath("$[0].enunciado").value("Quanto e 2 + 2?"))
                .andExpect(jsonPath("$[0].alternativaCorreta").doesNotExist());
    }

    @Test
    void deveResponderQuestao() throws Exception {
        Partida partida = partida(10L, PartidaStatus.ANDAMENTO);
        Participacao participacao = participacao(30L, partida, jogador(2L, EMAIL), 150);
        Resposta resposta = resposta(70L, participacao, partidaQuestao(50L, partida, questao(40L)));

        when(partidaService.responderQuestao(EMAIL, 10L, 50L, "B", 12)).thenReturn(resposta);

        mockMvc.perform(post("/partidas/10/respostas")
                .with(jwt().jwt(jwt -> jwt.subject(EMAIL)))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "partidaQuestaoId": 50,
                          "alternativaMarcada": "B",
                          "tempoResposta": 12
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(70))
                .andExpect(jsonPath("$.participacaoId").value(30))
                .andExpect(jsonPath("$.partidaQuestaoId").value(50))
                .andExpect(jsonPath("$.acertou").value(true))
                .andExpect(jsonPath("$.scorePartida").value(150));
    }

    @Test
    void deveListarLeaderboardEHistorico() throws Exception {
        Partida partida = partida(10L, PartidaStatus.FINALIZADA);
        Participacao participacao = participacao(30L, partida, jogador(2L, EMAIL), 450);

        when(partidaService.listarLeaderboard(EMAIL, 10L)).thenReturn(List.of(participacao));
        when(partidaService.listarHistorico(EMAIL)).thenReturn(List.of(participacao));

        mockMvc.perform(get("/partidas/10/leaderboard").with(jwt().jwt(jwt -> jwt.subject(EMAIL))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].jogador.email").value(EMAIL))
                .andExpect(jsonPath("$[0].scorePartida").value(450));

        mockMvc.perform(get("/partidas/historico").with(jwt().jwt(jwt -> jwt.subject(EMAIL))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].partidaId").value(10))
                .andExpect(jsonPath("$[0].statusSala").value("FINALIZADA"))
                .andExpect(jsonPath("$[0].scorePartida").value(450));
    }

    @Test
    void deveRetornarForbiddenQuandoJogadorNaoParticipaDaPartida() throws Exception {
        when(partidaService.buscarPartida(EMAIL, 10L))
                .thenThrow(new AccessDeniedException("Jogador nao participa desta partida."));

        mockMvc.perform(get("/partidas/10").with(jwt().jwt(jwt -> jwt.subject(EMAIL))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.mensagem").value("Jogador nao participa desta partida."));
    }

    private Partida partida(Long id, PartidaStatus status) {
        Partida partida = new Partida();
        partida.setId(id);
        partida.setHost(jogador(1L, EMAIL));
        partida.setTempoDeJogo(30);
        partida.setCodigoPin("ABC123");
        partida.setStatusSala(status);
        partida.setCriadoEm(LocalDateTime.of(2026, 6, 26, 18, 0));
        return partida;
    }

    private Jogador jogador(Long id, String email) {
        Jogador jogador = new Jogador();
        jogador.setId(id);
        jogador.setNome("Joao Reis");
        jogador.setEmail(email);
        jogador.setSenha("senha-hash");
        jogador.setRole(Role.JOGADOR);
        return jogador;
    }

    private Participacao participacao(Long id, Partida partida, Jogador jogador, int score) {
        Participacao participacao = new Participacao();
        participacao.setId(id);
        participacao.setPartida(partida);
        participacao.setJogador(jogador);
        participacao.setScorePartida(score);
        return participacao;
    }

    private Questao questao(Long id) {
        Questao questao = new Questao();
        questao.setId(id);
        questao.setEnunciado("Quanto e 2 + 2?");
        questao.setOpcaoA("1");
        questao.setOpcaoB("4");
        questao.setOpcaoC("5");
        questao.setOpcaoD("8");
        questao.setOpcaoE("10");
        questao.setAlternativaCorreta("B");
        questao.setDisciplina(Disciplina.MATEMATICA);
        questao.setInstituicao(Instituicao.ENEM);
        questao.setImagens(List.of());
        questao.setAtivo(true);
        return questao;
    }

    private PartidaQuestao partidaQuestao(Long id, Partida partida, Questao questao) {
        PartidaQuestao partidaQuestao = new PartidaQuestao();
        partidaQuestao.setId(id);
        partidaQuestao.setPartida(partida);
        partidaQuestao.setQuestao(questao);
        partidaQuestao.setOrdem(1);
        return partidaQuestao;
    }

    private Resposta resposta(Long id, Participacao participacao, PartidaQuestao partidaQuestao) {
        Resposta resposta = new Resposta();
        resposta.setId(id);
        resposta.setParticipacao(participacao);
        resposta.setPartidaQuestao(partidaQuestao);
        resposta.setAlternativaMarcada("B");
        resposta.setTempoResposta(12);
        resposta.setAcertou(true);
        return resposta;
    }
}
