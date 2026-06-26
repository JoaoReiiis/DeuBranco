package deu_branco_api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import deu_branco_api.controller.dto.EntrarPartidaRequest;
import deu_branco_api.controller.dto.HistoricoPartidaResponse;
import deu_branco_api.controller.dto.LobbyPartidaResponse;
import deu_branco_api.controller.dto.ParticipacaoResponse;
import deu_branco_api.controller.dto.PartidaCreateRequest;
import deu_branco_api.controller.dto.PartidaQuestaoResponse;
import deu_branco_api.controller.dto.PartidaResponse;
import deu_branco_api.controller.dto.RespostaPartidaRequest;
import deu_branco_api.controller.dto.RespostaPartidaResponse;
import deu_branco_api.model.Participacao;
import deu_branco_api.model.Partida;
import deu_branco_api.model.Resposta;
import deu_branco_api.service.PartidaService;
import deu_branco_api.service.PartidaWebSocketPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(
        name = "Partidas",
        description = "Fluxo REST de partidas multiplayer. Eventos em tempo real usam STOMP no endpoint /ws.")
@RestController
@RequestMapping("/partidas")
@SecurityRequirement(name = "bearerAuth")
public class PartidaController {

    private final PartidaService partidaService;
    private final PartidaWebSocketPublisher partidaWebSocketPublisher;

    public PartidaController(
            PartidaService partidaService,
            PartidaWebSocketPublisher partidaWebSocketPublisher) {
        this.partidaService = partidaService;
        this.partidaWebSocketPublisher = partidaWebSocketPublisher;
    }

    @Operation(
            summary = "Criar partida",
            description = "Cria uma sala multiplayer, sorteia as questoes, gera o PIN e inclui o host como participante.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Partida criada."),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou questoes insuficientes."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido.")
    })
    @PostMapping
    public ResponseEntity<PartidaResponse> criar(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody PartidaCreateRequest request) {
        Partida partida = partidaService.criarPartida(
                jwt.getSubject(),
                request.numeroQuestoes(),
                request.disciplinasComoEnum(),
                request.tempoDeJogo());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(partida.getId())
                .toUri();
        partidaWebSocketPublisher.publicarLobby(partida.getId(), "PARTIDA_CRIADA");

        return ResponseEntity.created(location).body(PartidaResponse.fromModel(partida));
    }

    @Operation(
            summary = "Buscar partida",
            description = "Retorna os dados basicos de uma partida. Apenas participantes podem consultar.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partida encontrada."),
            @ApiResponse(responseCode = "403", description = "Jogador nao participa da partida."),
            @ApiResponse(responseCode = "404", description = "Partida nao encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PartidaResponse> buscar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        Partida partida = partidaService.buscarPartida(jwt.getSubject(), id);

        return ResponseEntity.ok(PartidaResponse.fromModel(partida));
    }

    @Operation(summary = "Entrar na partida", description = "Adiciona o jogador autenticado a uma sala pelo PIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jogador entrou na partida."),
            @ApiResponse(responseCode = "400", description = "Partida indisponivel para entrada."),
            @ApiResponse(responseCode = "404", description = "PIN nao encontrado.")
    })
    @PostMapping("/entrar")
    public ResponseEntity<ParticipacaoResponse> entrar(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody EntrarPartidaRequest request) {
        Participacao participacao = partidaService.entrarNaPartida(jwt.getSubject(), request.codigoPin());
        partidaWebSocketPublisher.publicarLobby(participacao.getPartida().getId(), "JOGADOR_ENTROU");

        return ResponseEntity.ok(ParticipacaoResponse.fromModel(participacao));
    }

    @Operation(
            summary = "Iniciar partida",
            description = "Permite que o host inicie uma partida aguardando jogadores. Registra o horario de inicio para controle de tempo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partida iniciada."),
            @ApiResponse(responseCode = "400", description = "Partida nao pode ser iniciada.")
    })
    @PostMapping("/{id}/iniciar")
    public ResponseEntity<PartidaResponse> iniciar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        Partida partida = partidaService.iniciarPartida(jwt.getSubject(), id);
        partidaWebSocketPublisher.publicarPartida(partida, "PARTIDA_INICIADA");

        return ResponseEntity.ok(PartidaResponse.fromModel(partida));
    }

    @Operation(summary = "Cancelar partida", description = "Permite que o host cancele uma partida ainda nao iniciada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partida cancelada."),
            @ApiResponse(responseCode = "400", description = "Partida nao pode ser cancelada.")
    })
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<PartidaResponse> cancelar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        Partida partida = partidaService.cancelarPartida(jwt.getSubject(), id);
        partidaWebSocketPublisher.publicarPartida(partida, "PARTIDA_CANCELADA");

        return ResponseEntity.ok(PartidaResponse.fromModel(partida));
    }

    @Operation(
            summary = "Finalizar partida",
            description = "Permite que o host finalize uma partida em andamento. Respostas ausentes sao registradas automaticamente sem pontuar.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partida finalizada."),
            @ApiResponse(responseCode = "400", description = "Partida nao pode ser finalizada.")
    })
    @PostMapping("/{id}/finalizar")
    public ResponseEntity<PartidaResponse> finalizar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        Partida partida = partidaService.finalizarPartida(jwt.getSubject(), id);
        partidaWebSocketPublisher.publicarPartida(partida, "PARTIDA_FINALIZADA");
        partidaWebSocketPublisher.publicarLeaderboard(id);

        return ResponseEntity.ok(PartidaResponse.fromModel(partida));
    }

    @Operation(
            summary = "Listar questoes da partida",
            description = "Retorna as questoes da partida sem expor o gabarito. Apenas participantes podem consultar.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Questoes listadas."),
            @ApiResponse(responseCode = "403", description = "Jogador nao participa da partida.")
    })
    @GetMapping("/{id}/questoes")
    public ResponseEntity<List<PartidaQuestaoResponse>> listarQuestoes(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        List<PartidaQuestaoResponse> questoes = partidaService.listarQuestoesDaPartida(jwt.getSubject(), id)
                .stream()
                .map(PartidaQuestaoResponse::fromModel)
                .toList();

        return ResponseEntity.ok(questoes);
    }

    @Operation(
            summary = "Listar participantes",
            description = "Retorna o estado atual do lobby da partida. Apenas participantes podem consultar.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lobby listado."),
            @ApiResponse(responseCode = "403", description = "Jogador nao participa da partida.")
    })
    @GetMapping("/{id}/participantes")
    public ResponseEntity<LobbyPartidaResponse> listarParticipantes(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        Partida partida = partidaService.buscarPartida(jwt.getSubject(), id);
        List<ParticipacaoResponse> participantes = partidaService.listarParticipantes(jwt.getSubject(), id)
                .stream()
                .map(ParticipacaoResponse::fromModel)
                .toList();

        return ResponseEntity.ok(LobbyPartidaResponse.fromModel(partida, participantes));
    }

    @Operation(
            summary = "Responder questao",
            description = "Registra a resposta do jogador autenticado, calcula a pontuacao e publica atualizacoes WebSocket.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resposta registrada."),
            @ApiResponse(responseCode = "400", description = "Resposta invalida ou duplicada.")
    })
    @PostMapping("/{id}/respostas")
    public ResponseEntity<RespostaPartidaResponse> responder(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @Valid @RequestBody RespostaPartidaRequest request) {
        Resposta resposta = partidaService.responderQuestao(
                jwt.getSubject(),
                id,
                request.partidaQuestaoId(),
                request.alternativaMarcada(),
                request.tempoResposta());
        partidaWebSocketPublisher.publicarResposta(resposta);

        return ResponseEntity.ok(RespostaPartidaResponse.fromModel(resposta));
    }

    @Operation(
            summary = "Listar leaderboard",
            description = "Retorna o ranking final da partida. Disponivel apenas apos finalizar e somente para participantes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Leaderboard listado."),
            @ApiResponse(responseCode = "400", description = "Partida ainda nao finalizada."),
            @ApiResponse(responseCode = "403", description = "Jogador nao participa da partida.")
    })
    @GetMapping("/{id}/leaderboard")
    public ResponseEntity<List<ParticipacaoResponse>> listarLeaderboard(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        List<ParticipacaoResponse> leaderboard = partidaService.listarLeaderboard(jwt.getSubject(), id)
                .stream()
                .map(ParticipacaoResponse::fromModel)
                .toList();

        return ResponseEntity.ok(leaderboard);
    }

    @Operation(summary = "Listar historico", description = "Retorna o historico de partidas finalizadas do jogador autenticado.")
    @ApiResponse(responseCode = "200", description = "Historico listado.")
    @GetMapping("/historico")
    public ResponseEntity<List<HistoricoPartidaResponse>> listarHistorico(@AuthenticationPrincipal Jwt jwt) {
        List<HistoricoPartidaResponse> historico = partidaService.listarHistorico(jwt.getSubject())
                .stream()
                .map(HistoricoPartidaResponse::fromModel)
                .toList();

        return ResponseEntity.ok(historico);
    }
}
