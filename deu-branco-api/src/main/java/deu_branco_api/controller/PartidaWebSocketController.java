package deu_branco_api.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import deu_branco_api.controller.dto.EntrarPartidaRequest;
import deu_branco_api.controller.dto.PartidaCreateRequest;
import deu_branco_api.controller.dto.PartidaEventoResponse;
import deu_branco_api.controller.dto.PartidaResponse;
import deu_branco_api.controller.dto.RespostaPartidaRequest;
import deu_branco_api.controller.dto.RespostaPartidaResponse;
import deu_branco_api.model.Participacao;
import deu_branco_api.model.Partida;
import deu_branco_api.model.Resposta;
import deu_branco_api.service.PartidaService;
import deu_branco_api.service.PartidaWebSocketPublisher;
import jakarta.validation.Valid;

@Controller
@Validated
public class PartidaWebSocketController {

    private final PartidaService partidaService;
    private final PartidaWebSocketPublisher partidaWebSocketPublisher;
    private final SimpMessagingTemplate messagingTemplate;

    public PartidaWebSocketController(
            PartidaService partidaService,
            PartidaWebSocketPublisher partidaWebSocketPublisher,
            SimpMessagingTemplate messagingTemplate) {
        this.partidaService = partidaService;
        this.partidaWebSocketPublisher = partidaWebSocketPublisher;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/partidas/criar")
    public void criar(
            Principal principal,
            @Valid @Payload PartidaCreateRequest request) {
        Partida partida = partidaService.criarPartida(
                email(principal),
                request.numeroQuestoes(),
                request.disciplinasComoEnum(),
                request.tempoDeJogo());
        PartidaResponse response = PartidaResponse.fromModel(partida);

        enviarParaUsuario(principal, "PARTIDA_CRIADA", partida.getId(), response);
        partidaWebSocketPublisher.publicarLobby(partida.getId(), "PARTIDA_CRIADA");
    }

    @MessageMapping("/partidas/entrar")
    public void entrar(
            Principal principal,
            @Valid @Payload EntrarPartidaRequest request) {
        Participacao participacao = partidaService.entrarNaPartida(email(principal), request.codigoPin());
        Long partidaId = participacao.getPartida().getId();

        enviarParaUsuario(principal, "JOGADOR_ENTROU", partidaId, PartidaResponse.fromModel(participacao.getPartida()));
        partidaWebSocketPublisher.publicarLobby(partidaId, "JOGADOR_ENTROU");
    }

    @MessageMapping("/partidas/{id}/iniciar")
    public void iniciar(Principal principal, @DestinationVariable Long id) {
        Partida partida = partidaService.iniciarPartida(email(principal), id);

        enviarParaUsuario(principal, "PARTIDA_INICIADA", id, PartidaResponse.fromModel(partida));
        partidaWebSocketPublisher.publicarPartida(partida, "PARTIDA_INICIADA");
    }

    @MessageMapping("/partidas/{id}/cancelar")
    public void cancelar(Principal principal, @DestinationVariable Long id) {
        Partida partida = partidaService.cancelarPartida(email(principal), id);

        enviarParaUsuario(principal, "PARTIDA_CANCELADA", id, PartidaResponse.fromModel(partida));
        partidaWebSocketPublisher.publicarPartida(partida, "PARTIDA_CANCELADA");
    }

    @MessageMapping("/partidas/{id}/finalizar")
    public void finalizar(Principal principal, @DestinationVariable Long id) {
        Partida partida = partidaService.finalizarPartida(email(principal), id);

        enviarParaUsuario(principal, "PARTIDA_FINALIZADA", id, PartidaResponse.fromModel(partida));
        partidaWebSocketPublisher.publicarPartida(partida, "PARTIDA_FINALIZADA");
        partidaWebSocketPublisher.publicarLeaderboard(id);
    }

    @MessageMapping("/partidas/{id}/responder")
    public void responder(
            Principal principal,
            @DestinationVariable Long id,
            @Valid @Payload RespostaPartidaRequest request) {
        Resposta resposta = partidaService.responderQuestao(
                email(principal),
                id,
                request.partidaQuestaoId(),
                request.alternativaMarcada(),
                request.tempoResposta());
        RespostaPartidaResponse response = RespostaPartidaResponse.fromModel(resposta);

        enviarParaUsuario(principal, "RESPOSTA_REGISTRADA", id, response);
        partidaWebSocketPublisher.publicarResposta(resposta);
    }

    @MessageMapping("/partidas/{id}/sincronizar")
    public void sincronizar(Principal principal, @DestinationVariable Long id) {
        partidaService.garantirParticipante(email(principal), id);
        partidaWebSocketPublisher.publicarLobby(id, "LOBBY_SINCRONIZADO");
        partidaWebSocketPublisher.publicarPlacarParcial(id);
        enviarParaUsuario(principal, "SINCRONIZACAO_SOLICITADA", id, null);
    }

    private void enviarParaUsuario(Principal principal, String tipo, Long partidaId, Object dados) {
        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/partidas",
                PartidaEventoResponse.of(tipo, partidaId, dados));
    }

    private String email(Principal principal) {
        if (principal == null || principal.getName() == null || principal.getName().isBlank()) {
            throw new IllegalArgumentException("Usuario autenticado e obrigatorio para a acao WebSocket.");
        }

        return principal.getName();
    }
}
