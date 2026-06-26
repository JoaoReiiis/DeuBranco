package deu_branco_api.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import jakarta.persistence.EntityNotFoundException;

@Service
public class PartidaService {

    private static final int MINIMO_QUESTOES = 5;
    private static final int MAXIMO_QUESTOES = 30;
    private static final int TAMANHO_PIN = 6;
    private static final int MAX_TENTATIVAS_PIN = 20;
    private static final int PONTOS_ACERTO = 100;
    private static final int BONUS_MAXIMO_VELOCIDADE = 100;
    private static final String ALFABETO_PIN = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final Set<String> ALTERNATIVAS_VALIDAS = Set.of("A", "B", "C", "D", "E");

    private final SecureRandom secureRandom = new SecureRandom();
    private final JogadorRepository jogadorRepository;
    private final PartidaRepository partidaRepository;
    private final PartidaQuestaoRepository partidaQuestaoRepository;
    private final ParticipacaoRepository participacaoRepository;
    private final QuestaoRepository questaoRepository;
    private final RespostaRepository respostaRepository;

    public PartidaService(
            JogadorRepository jogadorRepository,
            PartidaRepository partidaRepository,
            PartidaQuestaoRepository partidaQuestaoRepository,
            ParticipacaoRepository participacaoRepository,
            QuestaoRepository questaoRepository,
            RespostaRepository respostaRepository) {
        this.jogadorRepository = jogadorRepository;
        this.partidaRepository = partidaRepository;
        this.partidaQuestaoRepository = partidaQuestaoRepository;
        this.participacaoRepository = participacaoRepository;
        this.questaoRepository = questaoRepository;
        this.respostaRepository = respostaRepository;
    }

    @Transactional
    public Partida criarPartida(
            String emailHost,
            int numeroQuestoes,
            List<Disciplina> disciplinas,
            int tempoDeJogo) {
        validarNumeroQuestoes(numeroQuestoes);
        validarTempoDeJogo(tempoDeJogo);

        Jogador host = buscarJogadorPorEmail(emailHost);
        List<Disciplina> disciplinasNormalizadas = normalizarDisciplinas(disciplinas);
        validarQuantidadeQuestoesDisponiveis(numeroQuestoes, disciplinasNormalizadas);

        List<Questao> questoes = sortearQuestoes(numeroQuestoes, disciplinasNormalizadas);
        String codigoPin = gerarPinUnico();

        Partida partida = new Partida();
        partida.setHost(host);
        partida.setTempoDeJogo(tempoDeJogo);
        partida.setCodigoPin(codigoPin);
        partida.setStatusSala(PartidaStatus.AGUARDANDO);
        Partida partidaSalva = partidaRepository.save(partida);

        Participacao participacaoHost = new Participacao();
        participacaoHost.setPartida(partidaSalva);
        participacaoHost.setJogador(host);
        participacaoHost.setScorePartida(0);
        participacaoRepository.save(participacaoHost);

        salvarQuestoesDaPartida(partidaSalva, questoes);

        return partidaSalva;
    }

    @Transactional
    public Participacao entrarNaPartida(String emailJogador, String codigoPin) {
        Jogador jogador = buscarJogadorPorEmail(emailJogador);
        Partida partida = buscarPartidaPorPinComLock(codigoPin);

        garantirStatus(partida, PartidaStatus.AGUARDANDO, "A partida nao esta aguardando jogadores.");

        return participacaoRepository.findByPartidaIdAndJogadorId(partida.getId(), jogador.getId())
                .orElseGet(() -> criarParticipacao(partida, jogador));
    }

    @Transactional
    public Partida iniciarPartida(String emailHost, Long partidaId) {
        Jogador host = buscarJogadorPorEmail(emailHost);
        Partida partida = buscarPartidaComLock(partidaId);

        garantirHost(partida, host);
        garantirStatus(partida, PartidaStatus.AGUARDANDO, "A partida so pode ser iniciada enquanto esta aguardando.");
        garantirMinimoParticipantes(partida.getId());
        garantirQuestoesVinculadas(partida.getId());

        partida.setStatusSala(PartidaStatus.ANDAMENTO);
        partida.setIniciadaEm(LocalDateTime.now());

        return partidaRepository.save(partida);
    }

    @Transactional
    public Partida cancelarPartida(String emailHost, Long partidaId) {
        Jogador host = buscarJogadorPorEmail(emailHost);
        Partida partida = buscarPartidaComLock(partidaId);

        garantirHost(partida, host);
        garantirStatus(partida, PartidaStatus.AGUARDANDO, "Apenas partidas aguardando podem ser canceladas.");

        partida.setStatusSala(PartidaStatus.CANCELADA);
        participacaoRepository.deleteByPartidaId(partida.getId());

        return partidaRepository.save(partida);
    }

    @Transactional
    public Resposta responderQuestao(
            String emailJogador,
            Long partidaId,
            Long partidaQuestaoId,
            String alternativaMarcada,
            int tempoResposta) {
        validarTempoResposta(tempoResposta);

        Jogador jogador = buscarJogadorPorEmail(emailJogador);
        Participacao participacao = participacaoRepository
                .buscarPorPartidaEJogadorComLock(partidaId, jogador.getId())
                .orElseThrow(() -> new IllegalArgumentException("Jogador nao participa desta partida."));
        Partida partida = participacao.getPartida();

        garantirStatus(partida, PartidaStatus.ANDAMENTO, "Respostas so podem ser registradas com a partida em andamento.");

        PartidaQuestao partidaQuestao = partidaQuestaoRepository.findByIdAndPartidaId(partidaQuestaoId, partidaId)
                .orElseThrow(() -> new EntityNotFoundException("Questao da partida nao encontrada."));

        if (respostaRepository.existsByParticipacaoIdAndPartidaQuestaoId(participacao.getId(), partidaQuestao.getId())) {
            throw new IllegalArgumentException("Esta questao ja foi respondida por este jogador nesta partida.");
        }

        String alternativa = normalizarAlternativaOpcional(alternativaMarcada);
        boolean acertou = alternativa != null
                && alternativa.equals(partidaQuestao.getQuestao().getAlternativaCorreta());
        int pontos = calcularPontuacao(acertou, tempoResposta, partida.getTempoDeJogo());

        Resposta resposta = new Resposta();
        resposta.setParticipacao(participacao);
        resposta.setPartidaQuestao(partidaQuestao);
        resposta.setAlternativaMarcada(alternativa);
        resposta.setTempoResposta(tempoResposta);
        resposta.setAcertou(acertou);
        Resposta respostaSalva = respostaRepository.save(resposta);

        if (pontos > 0) {
            int scoreAtual = participacao.getScorePartida() == null ? 0 : participacao.getScorePartida();
            participacao.setScorePartida(scoreAtual + pontos);
            participacaoRepository.save(participacao);
        }

        finalizarSeTodosResponderam(partida.getId());

        return respostaSalva;
    }

    @Transactional
    public Partida finalizarPartida(String emailHost, Long partidaId) {
        Jogador host = buscarJogadorPorEmail(emailHost);
        Partida partida = buscarPartidaComLock(partidaId);

        garantirHost(partida, host);

        return finalizarPartidaEmAndamento(partida);
    }

    @Transactional
    public Partida finalizarPartidaPorSistema(Long partidaId) {
        Partida partida = buscarPartidaComLock(partidaId);

        return finalizarPartidaEmAndamento(partida);
    }

    @Transactional(readOnly = true)
    public Partida buscarPartida(Long partidaId) {
        return partidaRepository.findById(partidaId)
                .orElseThrow(() -> new EntityNotFoundException("Partida nao encontrada com id: " + partidaId));
    }

    @Transactional(readOnly = true)
    public Partida buscarPartida(String emailJogador, Long partidaId) {
        garantirParticipante(emailJogador, partidaId);

        return buscarPartida(partidaId);
    }

    @Transactional(readOnly = true)
    public List<PartidaQuestao> listarQuestoesDaPartida(Long partidaId) {
        buscarPartida(partidaId);

        return partidaQuestaoRepository.findByPartidaIdOrderByOrdemAsc(partidaId);
    }

    @Transactional(readOnly = true)
    public List<PartidaQuestao> listarQuestoesDaPartida(String emailJogador, Long partidaId) {
        garantirParticipante(emailJogador, partidaId);

        return listarQuestoesDaPartida(partidaId);
    }

    @Transactional(readOnly = true)
    public List<Participacao> listarLeaderboard(Long partidaId) {
        Partida partida = buscarPartida(partidaId);

        garantirStatus(partida, PartidaStatus.FINALIZADA, "O leaderboard so fica disponivel apos finalizar a partida.");

        return participacaoRepository.findByPartidaIdOrderByScorePartidaDesc(partidaId);
    }

    @Transactional(readOnly = true)
    public List<Participacao> listarLeaderboard(String emailJogador, Long partidaId) {
        garantirParticipante(emailJogador, partidaId);

        return listarLeaderboard(partidaId);
    }

    @Transactional(readOnly = true)
    public List<Participacao> listarParticipantes(Long partidaId) {
        buscarPartida(partidaId);

        return participacaoRepository.findByPartidaIdOrderByIdAsc(partidaId);
    }

    @Transactional(readOnly = true)
    public List<Participacao> listarParticipantes(String emailJogador, Long partidaId) {
        garantirParticipante(emailJogador, partidaId);

        return listarParticipantes(partidaId);
    }

    @Transactional(readOnly = true)
    public List<Participacao> listarHistorico(String emailJogador) {
        String email = normalizarEmailObrigatorio(emailJogador);

        return participacaoRepository.findByJogadorEmailAndPartidaStatusSalaOrderByPartidaCriadoEmDesc(
                email,
                PartidaStatus.FINALIZADA);
    }

    private void salvarQuestoesDaPartida(Partida partida, List<Questao> questoes) {
        for (int indice = 0; indice < questoes.size(); indice++) {
            PartidaQuestao partidaQuestao = new PartidaQuestao();
            partidaQuestao.setPartida(partida);
            partidaQuestao.setQuestao(questoes.get(indice));
            partidaQuestao.setOrdem(indice + 1);
            partidaQuestaoRepository.save(partidaQuestao);
        }
    }

    private Participacao criarParticipacao(Partida partida, Jogador jogador) {
        Participacao participacao = new Participacao();
        participacao.setPartida(partida);
        participacao.setJogador(jogador);
        participacao.setScorePartida(0);

        return participacaoRepository.save(participacao);
    }

    private void finalizarSeTodosResponderam(Long partidaId) {
        long totalParticipantes = participacaoRepository.countByPartidaId(partidaId);
        long totalQuestoes = partidaQuestaoRepository.countByPartidaId(partidaId);

        if (totalParticipantes == 0 || totalQuestoes == 0) {
            return;
        }

        long respostasEsperadas = totalParticipantes * totalQuestoes;
        long respostasRegistradas = respostaRepository.countByParticipacaoPartidaId(partidaId);

        if (respostasRegistradas >= respostasEsperadas) {
            Partida partida = buscarPartidaComLock(partidaId);

            if (partida.getStatusSala() == PartidaStatus.ANDAMENTO) {
                finalizarPartidaEmAndamento(partida);
            }
        }
    }

    private Partida finalizarPartidaEmAndamento(Partida partida) {
        if (partida.getStatusSala() == PartidaStatus.FINALIZADA) {
            return partida;
        }

        garantirStatus(partida, PartidaStatus.ANDAMENTO, "Somente partidas em andamento podem ser finalizadas.");
        registrarRespostasAusentes(partida);
        partida.setStatusSala(PartidaStatus.FINALIZADA);
        partida.setFinalizadaEm(LocalDateTime.now());

        return partidaRepository.save(partida);
    }

    private void registrarRespostasAusentes(Partida partida) {
        List<Participacao> participacoes = participacaoRepository.findByPartidaIdOrderByIdAsc(partida.getId());
        List<PartidaQuestao> questoes = partidaQuestaoRepository.findByPartidaIdOrderByOrdemAsc(partida.getId());

        if (participacoes.isEmpty() || questoes.isEmpty()) {
            return;
        }

        Set<String> respostasRegistradas = new HashSet<>();
        respostaRepository.findByParticipacaoPartidaId(partida.getId())
                .forEach(resposta -> respostasRegistradas.add(chaveResposta(
                        resposta.getParticipacao().getId(),
                        resposta.getPartidaQuestao().getId())));

        for (Participacao participacao : participacoes) {
            for (PartidaQuestao questao : questoes) {
                String chave = chaveResposta(participacao.getId(), questao.getId());

                if (!respostasRegistradas.contains(chave)) {
                    Resposta respostaAusente = new Resposta();
                    respostaAusente.setParticipacao(participacao);
                    respostaAusente.setPartidaQuestao(questao);
                    respostaAusente.setAlternativaMarcada(null);
                    respostaAusente.setTempoResposta(partida.getTempoDeJogo());
                    respostaAusente.setAcertou(false);
                    respostaRepository.save(respostaAusente);
                    respostasRegistradas.add(chave);
                }
            }
        }
    }

    private String chaveResposta(Long participacaoId, Long partidaQuestaoId) {
        return participacaoId + ":" + partidaQuestaoId;
    }

    private int calcularPontuacao(boolean acertou, int tempoResposta, int tempoDeJogo) {
        if (!acertou) {
            return 0;
        }

        int tempoRestante = Math.max(0, tempoDeJogo - tempoResposta);
        int bonusVelocidade = tempoRestante * BONUS_MAXIMO_VELOCIDADE / tempoDeJogo;

        return PONTOS_ACERTO + bonusVelocidade;
    }

    private void garantirMinimoParticipantes(Long partidaId) {
        long totalParticipantes = participacaoRepository.countByPartidaId(partidaId);

        if (totalParticipantes < 2) {
            throw new IllegalArgumentException("Aguardando pelo menos um outro jogador para iniciar.");
        }
    }

    private void garantirQuestoesVinculadas(Long partidaId) {
        long totalQuestoes = partidaQuestaoRepository.countByPartidaId(partidaId);

        if (totalQuestoes < MINIMO_QUESTOES) {
            throw new IllegalArgumentException("A partida nao possui questoes suficientes para iniciar.");
        }
    }

    private void garantirHost(Partida partida, Jogador jogador) {
        if (!partida.getHost().getId().equals(jogador.getId())) {
            throw new IllegalArgumentException("Somente o host pode executar esta acao na partida.");
        }
    }

    public void garantirParticipante(String emailJogador, Long partidaId) {
        Jogador jogador = buscarJogadorPorEmail(emailJogador);
        buscarPartida(partidaId);

        if (!participacaoRepository.existsByPartidaIdAndJogadorId(partidaId, jogador.getId())) {
            throw new AccessDeniedException("Jogador nao participa desta partida.");
        }
    }

    private void garantirStatus(Partida partida, PartidaStatus statusEsperado, String mensagem) {
        if (partida.getStatusSala() != statusEsperado) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    private Jogador buscarJogadorPorEmail(String email) {
        String emailNormalizado = normalizarEmailObrigatorio(email);

        return jogadorRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new EntityNotFoundException("Jogador nao encontrado com email: " + emailNormalizado));
    }

    private Partida buscarPartidaComLock(Long partidaId) {
        if (partidaId == null) {
            throw new IllegalArgumentException("Id da partida e obrigatorio.");
        }

        return partidaRepository.buscarPorIdComLock(partidaId)
                .orElseThrow(() -> new EntityNotFoundException("Partida nao encontrada com id: " + partidaId));
    }

    private Partida buscarPartidaPorPinComLock(String codigoPin) {
        String pinNormalizado = normalizarPin(codigoPin);

        return partidaRepository.buscarPorCodigoPinComLock(pinNormalizado)
                .orElseThrow(() -> new EntityNotFoundException("Partida nao encontrada com PIN informado."));
    }

    private void validarNumeroQuestoes(int numeroQuestoes) {
        if (numeroQuestoes < MINIMO_QUESTOES || numeroQuestoes > MAXIMO_QUESTOES) {
            throw new IllegalArgumentException("Numero de questoes deve estar entre 5 e 30.");
        }
    }

    private void validarTempoDeJogo(int tempoDeJogo) {
        if (tempoDeJogo <= 0) {
            throw new IllegalArgumentException("Tempo de jogo deve ser maior que zero.");
        }
    }

    private void validarTempoResposta(int tempoResposta) {
        if (tempoResposta < 0) {
            throw new IllegalArgumentException("Tempo de resposta nao pode ser negativo.");
        }
    }

    private void validarQuantidadeQuestoesDisponiveis(int numeroQuestoes, List<Disciplina> disciplinas) {
        long totalDisponivel = questaoRepository.countByAtivoTrueAndDisciplinaIn(disciplinas);

        if (totalDisponivel < numeroQuestoes) {
            throw new IllegalArgumentException("Nao ha questoes suficientes para a configuracao escolhida.");
        }
    }

    private List<Questao> sortearQuestoes(int numeroQuestoes, List<Disciplina> disciplinas) {
        List<String> nomesDisciplinas = disciplinas.stream()
                .map(Enum::name)
                .toList();
        List<Questao> questoes = questaoRepository.sortearAtivasPorDisciplinas(nomesDisciplinas, numeroQuestoes);

        if (questoes.size() < numeroQuestoes) {
            throw new IllegalArgumentException("Nao foi possivel sortear questoes suficientes para a partida.");
        }

        return questoes;
    }

    private List<Disciplina> normalizarDisciplinas(List<Disciplina> disciplinas) {
        if (disciplinas == null || disciplinas.isEmpty()) {
            throw new IllegalArgumentException("Ao menos uma disciplina deve ser informada.");
        }

        List<Disciplina> normalizadas = disciplinas.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (normalizadas.isEmpty()) {
            throw new IllegalArgumentException("Ao menos uma disciplina valida deve ser informada.");
        }

        return normalizadas;
    }

    private String normalizarAlternativaOpcional(String alternativaMarcada) {
        if (alternativaMarcada == null || alternativaMarcada.isBlank()) {
            return null;
        }

        String alternativa = alternativaMarcada.trim().toUpperCase();

        if (!ALTERNATIVAS_VALIDAS.contains(alternativa)) {
            throw new IllegalArgumentException("Alternativa marcada deve ser A, B, C, D ou E.");
        }

        return alternativa;
    }

    private String normalizarEmailObrigatorio(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email do jogador e obrigatorio.");
        }

        return email.trim().toLowerCase();
    }

    private String normalizarPin(String codigoPin) {
        if (codigoPin == null || codigoPin.isBlank()) {
            throw new IllegalArgumentException("PIN da partida e obrigatorio.");
        }

        return codigoPin.trim().toUpperCase();
    }

    private String gerarPinUnico() {
        for (int tentativa = 0; tentativa < MAX_TENTATIVAS_PIN; tentativa++) {
            String pin = gerarPin();

            if (!partidaRepository.existsByCodigoPinAndStatusSalaNot(pin, PartidaStatus.FINALIZADA)) {
                return pin;
            }
        }

        throw new IllegalStateException("Nao foi possivel gerar um PIN unico para a partida.");
    }

    private String gerarPin() {
        StringBuilder pin = new StringBuilder(TAMANHO_PIN);

        for (int indice = 0; indice < TAMANHO_PIN; indice++) {
            int posicao = secureRandom.nextInt(ALFABETO_PIN.length());
            pin.append(ALFABETO_PIN.charAt(posicao));
        }

        return pin.toString();
    }
}
