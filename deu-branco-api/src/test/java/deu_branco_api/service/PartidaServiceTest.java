package deu_branco_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.security.access.AccessDeniedException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.stubbing.Answer;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import deu_branco_api.model.Disciplina;
import deu_branco_api.model.Jogador;
import deu_branco_api.model.Participacao;
import deu_branco_api.model.Partida;
import deu_branco_api.model.PartidaQuestao;
import deu_branco_api.model.PartidaStatus;
import deu_branco_api.model.Questao;
import deu_branco_api.model.Resposta;
import deu_branco_api.repository.JogadorRepository;
import deu_branco_api.repository.ParticipacaoRepository;
import deu_branco_api.repository.PartidaQuestaoRepository;
import deu_branco_api.repository.PartidaRepository;
import deu_branco_api.repository.QuestaoRepository;
import deu_branco_api.repository.RespostaRepository;

@ExtendWith(MockitoExtension.class)
class PartidaServiceTest {

    private static final Long ID_HOST = 1L;
    private static final Long ID_JOGADOR = 2L;
    private static final Long ID_PARTIDA = 10L;
    private static final Long ID_PARTICIPACAO = 30L;
    private static final Long ID_QUESTAO = 40L;
    private static final Long ID_PARTIDA_QUESTAO = 50L;
    private static final int NUMERO_QUESTOES_PADRAO = 5;
    private static final int TEMPO_DE_JOGO_PADRAO = 30;
    private static final String EMAIL_HOST = "host@email.com";
    private static final String EMAIL_JOGADOR = "joao@email.com";
    private static final String PIN_PADRAO = "ABC123";
    private static final List<Disciplina> DISCIPLINAS_PADRAO = List.of(Disciplina.MATEMATICA);

    @Mock
    private JogadorRepository jogadorRepository;

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private PartidaQuestaoRepository partidaQuestaoRepository;

    @Mock
    private ParticipacaoRepository participacaoRepository;

    @Mock
    private QuestaoRepository questaoRepository;

    @Mock
    private RespostaRepository respostaRepository;

    private PartidaService partidaService;

    @BeforeEach
    void setUp() {
        partidaService = new PartidaService(
                jogadorRepository,
                partidaRepository,
                partidaQuestaoRepository,
                participacaoRepository,
                questaoRepository,
                respostaRepository);
    }

    @Test
    void deveCriarPartidaComQuestoesValidasPinStatusInicialEParticipacaoDoHost() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        List<Questao> questoes = questoes(NUMERO_QUESTOES_PADRAO, Disciplina.MATEMATICA);

        when(jogadorRepository.findByEmail(EMAIL_HOST)).thenReturn(Optional.of(host));
        when(questaoRepository.countByAtivoTrueAndDisciplinaIn(DISCIPLINAS_PADRAO)).thenReturn(5L);
        when(questaoRepository.sortearAtivasPorDisciplinas(List.of("MATEMATICA"), 5)).thenReturn(questoes);
        when(partidaRepository.existsByCodigoPinAndStatusSalaNot(anyString(), eq(PartidaStatus.FINALIZADA)))
                .thenReturn(false);
        when(partidaRepository.save(any(Partida.class))).thenAnswer(salvarPartidaComId(ID_PARTIDA));
        when(participacaoRepository.save(any(Participacao.class))).thenAnswer(salvarParticipacaoComId(ID_PARTICIPACAO));
        when(partidaQuestaoRepository.save(any(PartidaQuestao.class))).thenAnswer(retornarPrimeiroArgumento());

        Partida partida = partidaService.criarPartida(
                " HOST@email.com ",
                NUMERO_QUESTOES_PADRAO,
                DISCIPLINAS_PADRAO,
                TEMPO_DE_JOGO_PADRAO);

        assertThat(partida.getId()).isEqualTo(ID_PARTIDA);
        assertThat(partida.getHost()).isSameAs(host);
        assertThat(partida.getTempoDeJogo()).isEqualTo(TEMPO_DE_JOGO_PADRAO);
        assertThat(partida.getStatusSala()).isEqualTo(PartidaStatus.AGUARDANDO);
        assertThat(partida.getCodigoPin()).hasSize(6);

        ArgumentCaptor<Participacao> participacaoCaptor = ArgumentCaptor.forClass(Participacao.class);
        verify(participacaoRepository).save(participacaoCaptor.capture());
        assertThat(participacaoCaptor.getValue().getPartida()).isSameAs(partida);
        assertThat(participacaoCaptor.getValue().getJogador()).isSameAs(host);
        assertThat(participacaoCaptor.getValue().getScorePartida()).isZero();

        ArgumentCaptor<PartidaQuestao> partidaQuestaoCaptor = ArgumentCaptor.forClass(PartidaQuestao.class);
        verify(partidaQuestaoRepository, times(NUMERO_QUESTOES_PADRAO)).save(partidaQuestaoCaptor.capture());

        assertThat(partidaQuestaoCaptor.getAllValues())
                .extracting(PartidaQuestao::getOrdem)
                .containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    void deveTentarNovoPinQuandoCodigoGeradoJaEstaEmUso() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        List<Questao> questoes = questoes(NUMERO_QUESTOES_PADRAO, Disciplina.MATEMATICA);

        when(jogadorRepository.findByEmail(EMAIL_HOST)).thenReturn(Optional.of(host));
        when(questaoRepository.countByAtivoTrueAndDisciplinaIn(DISCIPLINAS_PADRAO))
                .thenReturn((long) NUMERO_QUESTOES_PADRAO);
        when(questaoRepository.sortearAtivasPorDisciplinas(List.of("MATEMATICA"), NUMERO_QUESTOES_PADRAO))
                .thenReturn(questoes);
        when(partidaRepository.existsByCodigoPinAndStatusSalaNot(anyString(), eq(PartidaStatus.FINALIZADA)))
                .thenReturn(true, false);
        when(partidaRepository.save(any(Partida.class))).thenAnswer(salvarPartidaComId(ID_PARTIDA));
        when(participacaoRepository.save(any(Participacao.class))).thenAnswer(salvarParticipacaoComId(ID_PARTICIPACAO));
        when(partidaQuestaoRepository.save(any(PartidaQuestao.class))).thenAnswer(retornarPrimeiroArgumento());

        Partida partida = partidaService.criarPartida(
                EMAIL_HOST,
                NUMERO_QUESTOES_PADRAO,
                DISCIPLINAS_PADRAO,
                TEMPO_DE_JOGO_PADRAO);

        assertThat(partida.getCodigoPin()).hasSize(6);
        verify(partidaRepository, times(2))
                .existsByCodigoPinAndStatusSalaNot(anyString(), eq(PartidaStatus.FINALIZADA));
    }

    @Test
    void deveImpedirCriacaoComNumeroDeQuestoesInvalido() {
        assertErroDeNegocio(
                () -> partidaService.criarPartida(EMAIL_HOST, 4, DISCIPLINAS_PADRAO, TEMPO_DE_JOGO_PADRAO),
                "Numero de questoes deve estar entre 5 e 30.");

        verifyNoInteractions(jogadorRepository, questaoRepository, partidaRepository);
    }

    @Test
    void deveImpedirCriacaoQuandoNaoHaQuestoesSuficientes() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);

        when(jogadorRepository.findByEmail(EMAIL_HOST)).thenReturn(Optional.of(host));
        when(questaoRepository.countByAtivoTrueAndDisciplinaIn(DISCIPLINAS_PADRAO)).thenReturn(4L);

        assertErroDeNegocio(
                () -> partidaService.criarPartida(
                        EMAIL_HOST,
                        NUMERO_QUESTOES_PADRAO,
                        DISCIPLINAS_PADRAO,
                        TEMPO_DE_JOGO_PADRAO),
                "Nao ha questoes suficientes para a configuracao escolhida.");

        verify(questaoRepository, never()).sortearAtivasPorDisciplinas(any(), eq(NUMERO_QUESTOES_PADRAO));
        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void deveEntrarNaPartidaPorPin() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        Jogador jogador = jogador(ID_JOGADOR, "Joao", EMAIL_JOGADOR);
        Partida partida = partida(ID_PARTIDA, host, PartidaStatus.AGUARDANDO, TEMPO_DE_JOGO_PADRAO);

        when(jogadorRepository.findByEmail(EMAIL_JOGADOR)).thenReturn(Optional.of(jogador));
        when(partidaRepository.buscarPorCodigoPinComLock(PIN_PADRAO)).thenReturn(Optional.of(partida));
        when(participacaoRepository.findByPartidaIdAndJogadorId(ID_PARTIDA, ID_JOGADOR)).thenReturn(Optional.empty());
        when(participacaoRepository.save(any(Participacao.class))).thenAnswer(salvarParticipacaoComId(ID_PARTICIPACAO));

        Participacao participacao = partidaService.entrarNaPartida(EMAIL_JOGADOR, " abc123 ");

        assertThat(participacao.getId()).isEqualTo(ID_PARTICIPACAO);
        assertThat(participacao.getPartida()).isSameAs(partida);
        assertThat(participacao.getJogador()).isSameAs(jogador);
        assertThat(participacao.getScorePartida()).isZero();
    }

    @Test
    void deveReutilizarParticipacaoExistenteAoEntrarNovamenteNaPartida() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        Jogador jogador = jogador(ID_JOGADOR, "Joao", EMAIL_JOGADOR);
        Partida partida = partida(ID_PARTIDA, host, PartidaStatus.AGUARDANDO, TEMPO_DE_JOGO_PADRAO);
        Participacao participacaoExistente = participacao(ID_PARTICIPACAO, partida, jogador, 0);

        when(jogadorRepository.findByEmail(EMAIL_JOGADOR)).thenReturn(Optional.of(jogador));
        when(partidaRepository.buscarPorCodigoPinComLock(PIN_PADRAO)).thenReturn(Optional.of(partida));
        when(participacaoRepository.findByPartidaIdAndJogadorId(ID_PARTIDA, ID_JOGADOR))
                .thenReturn(Optional.of(participacaoExistente));

        Participacao participacao = partidaService.entrarNaPartida(EMAIL_JOGADOR, PIN_PADRAO);

        assertThat(participacao).isSameAs(participacaoExistente);
        verify(participacaoRepository, never()).save(any(Participacao.class));
    }

    @Test
    void deveImpedirEntradaQuandoPartidaNaoEstaAguardando() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        Jogador jogador = jogador(ID_JOGADOR, "Joao", EMAIL_JOGADOR);
        Partida partida = partida(ID_PARTIDA, host, PartidaStatus.ANDAMENTO, TEMPO_DE_JOGO_PADRAO);

        when(jogadorRepository.findByEmail(EMAIL_JOGADOR)).thenReturn(Optional.of(jogador));
        when(partidaRepository.buscarPorCodigoPinComLock(PIN_PADRAO)).thenReturn(Optional.of(partida));

        assertErroDeNegocio(
                () -> partidaService.entrarNaPartida(EMAIL_JOGADOR, PIN_PADRAO),
                "A partida nao esta aguardando jogadores.");

        verify(participacaoRepository, never()).save(any(Participacao.class));
    }

    @Test
    void deveIniciarPartidaSomenteQuandoJogadorForHost() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        Partida partida = partida(ID_PARTIDA, host, PartidaStatus.AGUARDANDO, TEMPO_DE_JOGO_PADRAO);

        when(jogadorRepository.findByEmail(EMAIL_HOST)).thenReturn(Optional.of(host));
        when(partidaRepository.buscarPorIdComLock(ID_PARTIDA)).thenReturn(Optional.of(partida));
        when(participacaoRepository.countByPartidaId(ID_PARTIDA)).thenReturn(2L);
        when(partidaQuestaoRepository.countByPartidaId(ID_PARTIDA)).thenReturn((long) NUMERO_QUESTOES_PADRAO);
        when(partidaRepository.save(partida)).thenReturn(partida);

        Partida iniciada = partidaService.iniciarPartida(EMAIL_HOST, ID_PARTIDA);

        assertThat(iniciada.getStatusSala()).isEqualTo(PartidaStatus.ANDAMENTO);
        assertThat(iniciada.getIniciadaEm()).isNotNull();
        verify(partidaRepository).save(partida);
    }

    @Test
    void deveImpedirInicioQuandoJogadorNaoForHost() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        Jogador outro = jogador(ID_JOGADOR, "Joao", EMAIL_JOGADOR);
        Partida partida = partida(ID_PARTIDA, host, PartidaStatus.AGUARDANDO, TEMPO_DE_JOGO_PADRAO);

        when(jogadorRepository.findByEmail(EMAIL_JOGADOR)).thenReturn(Optional.of(outro));
        when(partidaRepository.buscarPorIdComLock(ID_PARTIDA)).thenReturn(Optional.of(partida));

        assertErroDeNegocio(
                () -> partidaService.iniciarPartida(EMAIL_JOGADOR, ID_PARTIDA),
                "Somente o host pode executar esta acao na partida.");

        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void deveBloquearInicioComApenasUmParticipante() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        Partida partida = partida(ID_PARTIDA, host, PartidaStatus.AGUARDANDO, TEMPO_DE_JOGO_PADRAO);

        when(jogadorRepository.findByEmail(EMAIL_HOST)).thenReturn(Optional.of(host));
        when(partidaRepository.buscarPorIdComLock(ID_PARTIDA)).thenReturn(Optional.of(partida));
        when(participacaoRepository.countByPartidaId(ID_PARTIDA)).thenReturn(1L);

        assertErroDeNegocio(
                () -> partidaService.iniciarPartida(EMAIL_HOST, ID_PARTIDA),
                "Aguardando pelo menos um outro jogador para iniciar.");

        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void deveCancelarPartidaSomenteQuandoJogadorForHost() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        Partida partida = partida(ID_PARTIDA, host, PartidaStatus.AGUARDANDO, TEMPO_DE_JOGO_PADRAO);

        when(jogadorRepository.findByEmail(EMAIL_HOST)).thenReturn(Optional.of(host));
        when(partidaRepository.buscarPorIdComLock(ID_PARTIDA)).thenReturn(Optional.of(partida));
        when(partidaRepository.save(partida)).thenReturn(partida);

        Partida cancelada = partidaService.cancelarPartida(EMAIL_HOST, ID_PARTIDA);

        assertThat(cancelada.getStatusSala()).isEqualTo(PartidaStatus.CANCELADA);
        verify(participacaoRepository).deleteByPartidaId(ID_PARTIDA);
        verify(partidaRepository).save(partida);
    }

    @Test
    void deveImpedirCancelamentoQuandoJogadorNaoForHost() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        Jogador outro = jogador(ID_JOGADOR, "Joao", EMAIL_JOGADOR);
        Partida partida = partida(ID_PARTIDA, host, PartidaStatus.AGUARDANDO, TEMPO_DE_JOGO_PADRAO);

        when(jogadorRepository.findByEmail(EMAIL_JOGADOR)).thenReturn(Optional.of(outro));
        when(partidaRepository.buscarPorIdComLock(ID_PARTIDA)).thenReturn(Optional.of(partida));

        assertErroDeNegocio(
                () -> partidaService.cancelarPartida(EMAIL_JOGADOR, ID_PARTIDA),
                "Somente o host pode executar esta acao na partida.");

        verify(participacaoRepository, never()).deleteByPartidaId(ID_PARTIDA);
        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void deveRegistrarRespostaCorretaComBonusDeVelocidade() {
        ContextoResposta contexto = prepararResposta(PartidaStatus.ANDAMENTO, "B", 0, 20);
        when(respostaRepository.existsByParticipacaoIdAndPartidaQuestaoId(ID_PARTICIPACAO, ID_PARTIDA_QUESTAO))
                .thenReturn(false);
        when(respostaRepository.save(any(Resposta.class))).thenAnswer(salvarRespostaComId(60L));
        simularPartidaAindaNaoFinalizada();

        Resposta resposta = partidaService.responderQuestao(EMAIL_JOGADOR, ID_PARTIDA, ID_PARTIDA_QUESTAO, " b ", 10);

        assertThat(resposta.getId()).isEqualTo(60L);
        assertThat(resposta.getAlternativaMarcada()).isEqualTo("B");
        assertThat(resposta.getAcertou()).isTrue();
        assertThat(contexto.participacao().getScorePartida()).isEqualTo(150);
        verify(participacaoRepository).save(contexto.participacao());
    }

    @Test
    void deveRegistrarRespostaErradaSemPontuar() {
        ContextoResposta contexto = prepararResposta(PartidaStatus.ANDAMENTO, "B", 25, 20);
        when(respostaRepository.existsByParticipacaoIdAndPartidaQuestaoId(ID_PARTICIPACAO, ID_PARTIDA_QUESTAO))
                .thenReturn(false);
        when(respostaRepository.save(any(Resposta.class))).thenAnswer(retornarPrimeiroArgumento());
        simularPartidaAindaNaoFinalizada();

        Resposta resposta = partidaService.responderQuestao(EMAIL_JOGADOR, ID_PARTIDA, ID_PARTIDA_QUESTAO, "A", 10);

        assertThat(resposta.getAcertou()).isFalse();
        assertThat(contexto.participacao().getScorePartida()).isEqualTo(25);
        verify(participacaoRepository, never()).save(contexto.participacao());
    }

    @Test
    void deveRegistrarRespostaSemAlternativaQuandoTempoExpira() {
        ContextoResposta contexto = prepararResposta(PartidaStatus.ANDAMENTO, "B", 10, 20);
        when(respostaRepository.existsByParticipacaoIdAndPartidaQuestaoId(ID_PARTICIPACAO, ID_PARTIDA_QUESTAO))
                .thenReturn(false);
        when(respostaRepository.save(any(Resposta.class))).thenAnswer(retornarPrimeiroArgumento());
        simularPartidaAindaNaoFinalizada();

        Resposta resposta = partidaService.responderQuestao(EMAIL_JOGADOR, ID_PARTIDA, ID_PARTIDA_QUESTAO, null, 20);

        assertThat(resposta.getAlternativaMarcada()).isNull();
        assertThat(resposta.getAcertou()).isFalse();
        assertThat(contexto.participacao().getScorePartida()).isEqualTo(10);
        verify(participacaoRepository, never()).save(contexto.participacao());
    }

    @Test
    void deveImpedirRespostaQuandoPartidaNaoEstaEmAndamento() {
        prepararResposta(PartidaStatus.AGUARDANDO, "B", 0, 20);

        assertErroDeNegocio(
                () -> partidaService.responderQuestao(EMAIL_JOGADOR, ID_PARTIDA, ID_PARTIDA_QUESTAO, "B", 10),
                "Respostas so podem ser registradas com a partida em andamento.");

        verify(partidaQuestaoRepository, never()).findByIdAndPartidaId(ID_PARTIDA_QUESTAO, ID_PARTIDA);
        verify(respostaRepository, never()).save(any(Resposta.class));
    }

    @Test
    void deveImpedirRespostaDuplicadaPorJogadorEQuestao() {
        prepararResposta(PartidaStatus.ANDAMENTO, "B", 0, 20);
        when(respostaRepository.existsByParticipacaoIdAndPartidaQuestaoId(ID_PARTICIPACAO, ID_PARTIDA_QUESTAO))
                .thenReturn(true);

        assertErroDeNegocio(
                () -> partidaService.responderQuestao(EMAIL_JOGADOR, ID_PARTIDA, ID_PARTIDA_QUESTAO, "B", 10),
                "Esta questao ja foi respondida por este jogador nesta partida.");

        verify(respostaRepository, never()).save(any(Resposta.class));
        verify(participacaoRepository, never()).save(any(Participacao.class));
    }

    @Test
    void deveFinalizarAutomaticamenteQuandoTodosRespondem() {
        ContextoResposta contexto = prepararResposta(PartidaStatus.ANDAMENTO, "B", 0, 20);
        when(respostaRepository.existsByParticipacaoIdAndPartidaQuestaoId(ID_PARTICIPACAO, ID_PARTIDA_QUESTAO))
                .thenReturn(false);
        when(respostaRepository.save(any(Resposta.class))).thenAnswer(retornarPrimeiroArgumento());
        when(participacaoRepository.countByPartidaId(ID_PARTIDA)).thenReturn(2L);
        when(partidaQuestaoRepository.countByPartidaId(ID_PARTIDA)).thenReturn(1L);
        when(respostaRepository.countByParticipacaoPartidaId(ID_PARTIDA)).thenReturn(2L);
        when(partidaRepository.buscarPorIdComLock(ID_PARTIDA)).thenReturn(Optional.of(contexto.partida()));
        when(participacaoRepository.findByPartidaIdOrderByIdAsc(ID_PARTIDA)).thenReturn(List.of());
        when(partidaQuestaoRepository.findByPartidaIdOrderByOrdemAsc(ID_PARTIDA)).thenReturn(List.of());
        when(partidaRepository.save(contexto.partida())).thenReturn(contexto.partida());

        partidaService.responderQuestao(EMAIL_JOGADOR, ID_PARTIDA, ID_PARTIDA_QUESTAO, "B", 10);

        assertThat(contexto.partida().getStatusSala()).isEqualTo(PartidaStatus.FINALIZADA);
        assertThat(contexto.partida().getFinalizadaEm()).isNotNull();
        verify(partidaRepository).save(contexto.partida());
    }

    @Test
    void deveFinalizarPorSistemaRegistrandoRespostasAusentesSemPontuar() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        Jogador jogador = jogador(ID_JOGADOR, "Joao", EMAIL_JOGADOR);
        Partida partida = partida(ID_PARTIDA, host, PartidaStatus.ANDAMENTO, TEMPO_DE_JOGO_PADRAO);
        Participacao participacaoHost = participacao(31L, partida, host, 80);
        Participacao participacaoJogador = participacao(ID_PARTICIPACAO, partida, jogador, 120);
        Questao primeiraQuestao = questao(41L, Disciplina.MATEMATICA, "A");
        Questao segundaQuestao = questao(42L, Disciplina.MATEMATICA, "B");
        PartidaQuestao partidaQuestaoRespondida = partidaQuestao(51L, partida, primeiraQuestao);
        PartidaQuestao partidaQuestaoPendente = partidaQuestao(52L, partida, segundaQuestao);
        Resposta respostaExistente = resposta(61L, participacaoHost, partidaQuestaoRespondida, "A", true, 5);

        when(partidaRepository.buscarPorIdComLock(ID_PARTIDA)).thenReturn(Optional.of(partida));
        when(participacaoRepository.findByPartidaIdOrderByIdAsc(ID_PARTIDA))
                .thenReturn(List.of(participacaoHost, participacaoJogador));
        when(partidaQuestaoRepository.findByPartidaIdOrderByOrdemAsc(ID_PARTIDA))
                .thenReturn(List.of(partidaQuestaoRespondida, partidaQuestaoPendente));
        when(respostaRepository.findByParticipacaoPartidaId(ID_PARTIDA)).thenReturn(List.of(respostaExistente));
        when(respostaRepository.save(any(Resposta.class))).thenAnswer(retornarPrimeiroArgumento());
        when(partidaRepository.save(partida)).thenReturn(partida);

        Partida finalizada = partidaService.finalizarPartidaPorSistema(ID_PARTIDA);

        assertThat(finalizada.getStatusSala()).isEqualTo(PartidaStatus.FINALIZADA);
        assertThat(finalizada.getFinalizadaEm()).isNotNull();
        assertThat(participacaoHost.getScorePartida()).isEqualTo(80);
        assertThat(participacaoJogador.getScorePartida()).isEqualTo(120);

        ArgumentCaptor<Resposta> respostaCaptor = ArgumentCaptor.forClass(Resposta.class);
        verify(respostaRepository, times(3)).save(respostaCaptor.capture());
        assertThat(respostaCaptor.getAllValues())
                .allSatisfy(respostaAusente -> {
                    assertThat(respostaAusente.getAlternativaMarcada()).isNull();
                    assertThat(respostaAusente.getAcertou()).isFalse();
                    assertThat(respostaAusente.getTempoResposta()).isEqualTo(TEMPO_DE_JOGO_PADRAO);
                });
        assertThat(respostaCaptor.getAllValues())
                .extracting(respostaAusente -> respostaAusente.getParticipacao().getId()
                        + ":" + respostaAusente.getPartidaQuestao().getId())
                .containsExactly("31:52", "30:51", "30:52");
        verify(participacaoRepository, never()).save(any(Participacao.class));
        verify(partidaRepository).save(partida);
    }

    @Test
    void deveRetornarPartidaFinalizadaSemRegistrarAusenciasNovamente() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        Partida partida = partida(ID_PARTIDA, host, PartidaStatus.FINALIZADA, TEMPO_DE_JOGO_PADRAO);
        partida.setFinalizadaEm(LocalDateTime.now());

        when(partidaRepository.buscarPorIdComLock(ID_PARTIDA)).thenReturn(Optional.of(partida));

        Partida finalizada = partidaService.finalizarPartidaPorSistema(ID_PARTIDA);

        assertThat(finalizada).isSameAs(partida);
        verify(respostaRepository, never()).save(any(Resposta.class));
        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void devePermitirLeaderboardApenasParaPartidaFinalizada() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        Partida partida = partida(ID_PARTIDA, host, PartidaStatus.FINALIZADA, TEMPO_DE_JOGO_PADRAO);
        Participacao primeiro = participacao(ID_PARTICIPACAO, partida, host, 200);

        when(partidaRepository.findById(ID_PARTIDA)).thenReturn(Optional.of(partida));
        when(participacaoRepository.findByPartidaIdOrderByScorePartidaDesc(ID_PARTIDA)).thenReturn(List.of(primeiro));

        List<Participacao> leaderboard = partidaService.listarLeaderboard(ID_PARTIDA);

        assertThat(leaderboard).containsExactly(primeiro);
    }

    @Test
    void deveBloquearLeaderboardQuandoPartidaNaoFinalizada() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        Partida partida = partida(ID_PARTIDA, host, PartidaStatus.ANDAMENTO, TEMPO_DE_JOGO_PADRAO);

        when(partidaRepository.findById(ID_PARTIDA)).thenReturn(Optional.of(partida));

        assertErroDeNegocio(
                () -> partidaService.listarLeaderboard(ID_PARTIDA),
                "O leaderboard so fica disponivel apos finalizar a partida.");

        verify(participacaoRepository, never()).findByPartidaIdOrderByScorePartidaDesc(ID_PARTIDA);
    }

    @Test
    void deveListarHistoricoApenasComPartidasFinalizadasDoJogador() {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        Partida partidaFinalizada = partida(ID_PARTIDA, host, PartidaStatus.FINALIZADA, TEMPO_DE_JOGO_PADRAO);
        Participacao participacao = participacao(ID_PARTICIPACAO, partidaFinalizada, host, 200);

        when(participacaoRepository.findByJogadorEmailAndPartidaStatusSalaOrderByPartidaCriadoEmDesc(
                EMAIL_HOST,
                PartidaStatus.FINALIZADA))
                .thenReturn(List.of(participacao));

        List<Participacao> historico = partidaService.listarHistorico(" HOST@email.com ");

        assertThat(historico).containsExactly(participacao);
        verify(participacaoRepository).findByJogadorEmailAndPartidaStatusSalaOrderByPartidaCriadoEmDesc(
                EMAIL_HOST,
                PartidaStatus.FINALIZADA);
    }

    @Test
    void devePermitirConsultaAutorizadaQuandoJogadorParticipaDaPartida() {
        Jogador jogador = jogador(ID_JOGADOR, "Joao", EMAIL_JOGADOR);
        Partida partida = partida(ID_PARTIDA, jogador(ID_HOST, "Host", EMAIL_HOST), PartidaStatus.ANDAMENTO, 20);

        when(jogadorRepository.findByEmail(EMAIL_JOGADOR)).thenReturn(Optional.of(jogador));
        when(partidaRepository.findById(ID_PARTIDA)).thenReturn(Optional.of(partida));
        when(participacaoRepository.existsByPartidaIdAndJogadorId(ID_PARTIDA, ID_JOGADOR)).thenReturn(true);

        Partida partidaAutorizada = partidaService.buscarPartida(EMAIL_JOGADOR, ID_PARTIDA);

        assertThat(partidaAutorizada).isSameAs(partida);
    }

    @Test
    void deveBloquearConsultaQuandoJogadorNaoParticipaDaPartida() {
        Jogador jogador = jogador(ID_JOGADOR, "Joao", EMAIL_JOGADOR);
        Partida partida = partida(ID_PARTIDA, jogador(ID_HOST, "Host", EMAIL_HOST), PartidaStatus.ANDAMENTO, 20);

        when(jogadorRepository.findByEmail(EMAIL_JOGADOR)).thenReturn(Optional.of(jogador));
        when(partidaRepository.findById(ID_PARTIDA)).thenReturn(Optional.of(partida));
        when(participacaoRepository.existsByPartidaIdAndJogadorId(ID_PARTIDA, ID_JOGADOR)).thenReturn(false);

        assertThatThrownBy(() -> partidaService.buscarPartida(EMAIL_JOGADOR, ID_PARTIDA))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Jogador nao participa desta partida.");
    }

    private ContextoResposta prepararResposta(
            PartidaStatus statusPartida,
            String alternativaCorreta,
            int scoreInicial,
            int tempoDeJogo) {
        Jogador host = jogador(ID_HOST, "Host", EMAIL_HOST);
        Jogador jogador = jogador(ID_JOGADOR, "Joao", EMAIL_JOGADOR);
        Partida partida = partida(ID_PARTIDA, host, statusPartida, tempoDeJogo);
        Participacao participacao = participacao(ID_PARTICIPACAO, partida, jogador, scoreInicial);
        Questao questao = questao(ID_QUESTAO, Disciplina.MATEMATICA, alternativaCorreta);
        PartidaQuestao partidaQuestao = partidaQuestao(ID_PARTIDA_QUESTAO, partida, questao);

        when(jogadorRepository.findByEmail(EMAIL_JOGADOR)).thenReturn(Optional.of(jogador));
        when(participacaoRepository.buscarPorPartidaEJogadorComLock(ID_PARTIDA, ID_JOGADOR))
                .thenReturn(Optional.of(participacao));

        if (statusPartida == PartidaStatus.ANDAMENTO) {
            when(partidaQuestaoRepository.findByIdAndPartidaId(ID_PARTIDA_QUESTAO, ID_PARTIDA))
                    .thenReturn(Optional.of(partidaQuestao));
        }

        return new ContextoResposta(partida, participacao, partidaQuestao);
    }

    private void simularPartidaAindaNaoFinalizada() {
        when(participacaoRepository.countByPartidaId(ID_PARTIDA)).thenReturn(2L);
        when(partidaQuestaoRepository.countByPartidaId(ID_PARTIDA)).thenReturn((long) NUMERO_QUESTOES_PADRAO);
        when(respostaRepository.countByParticipacaoPartidaId(ID_PARTIDA)).thenReturn(1L);
    }

    private void assertErroDeNegocio(Runnable acao, String mensagem) {
        assertThatThrownBy(acao::run)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(mensagem);
    }

    private Answer<Partida> salvarPartidaComId(Long id) {
        return invocation -> {
            Partida partida = invocation.getArgument(0);
            partida.setId(id);
            return partida;
        };
    }

    private Answer<Participacao> salvarParticipacaoComId(Long id) {
        return invocation -> {
            Participacao participacao = invocation.getArgument(0);
            participacao.setId(id);
            return participacao;
        };
    }

    private Answer<Resposta> salvarRespostaComId(Long id) {
        return invocation -> {
            Resposta resposta = invocation.getArgument(0);
            resposta.setId(id);
            return resposta;
        };
    }

    private <T> Answer<T> retornarPrimeiroArgumento() {
        return invocation -> invocation.getArgument(0);
    }

    private List<Questao> questoes(int quantidade, Disciplina disciplina) {
        return java.util.stream.LongStream.rangeClosed(1, quantidade)
                .mapToObj(id -> questao(id, disciplina, "A"))
                .toList();
    }

    private Jogador jogador(Long id, String nome, String email) {
        Jogador jogador = new Jogador();
        jogador.setId(id);
        jogador.setNome(nome);
        jogador.setEmail(email);
        jogador.setSenha("senha-hash");
        return jogador;
    }

    private Partida partida(Long id, Jogador host, PartidaStatus status, int tempoDeJogo) {
        Partida partida = new Partida();
        partida.setId(id);
        partida.setHost(host);
        partida.setCodigoPin("ABC123");
        partida.setStatusSala(status);
        partida.setTempoDeJogo(tempoDeJogo);
        return partida;
    }

    private Questao questao(Long id, Disciplina disciplina, String alternativaCorreta) {
        Questao questao = new Questao();
        questao.setId(id);
        questao.setEnunciado("Questao " + id);
        questao.setOpcaoA("A");
        questao.setOpcaoB("B");
        questao.setOpcaoC("C");
        questao.setOpcaoD("D");
        questao.setOpcaoE("E");
        questao.setAlternativaCorreta(alternativaCorreta);
        questao.setDisciplina(disciplina);
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

    private Participacao participacao(Long id, Partida partida, Jogador jogador, int score) {
        Participacao participacao = new Participacao();
        participacao.setId(id);
        participacao.setPartida(partida);
        participacao.setJogador(jogador);
        participacao.setScorePartida(score);
        return participacao;
    }

    private Resposta resposta(
            Long id,
            Participacao participacao,
            PartidaQuestao partidaQuestao,
            String alternativaMarcada,
            boolean acertou,
            int tempoResposta) {
        Resposta resposta = new Resposta();
        resposta.setId(id);
        resposta.setParticipacao(participacao);
        resposta.setPartidaQuestao(partidaQuestao);
        resposta.setAlternativaMarcada(alternativaMarcada);
        resposta.setAcertou(acertou);
        resposta.setTempoResposta(tempoResposta);
        return resposta;
    }

    private record ContextoResposta(
            Partida partida,
            Participacao participacao,
            PartidaQuestao partidaQuestao) {
    }
}
