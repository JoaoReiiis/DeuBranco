package deu_branco_api.service;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import deu_branco_api.controller.dto.LobbyPartidaResponse;
import deu_branco_api.controller.dto.ParticipacaoResponse;
import deu_branco_api.controller.dto.PartidaEventoResponse;
import deu_branco_api.controller.dto.PartidaResponse;
import deu_branco_api.controller.dto.RespostaPartidaResponse;
import deu_branco_api.model.Participacao;
import deu_branco_api.model.Partida;
import deu_branco_api.model.PartidaStatus;
import deu_branco_api.model.Resposta;

@Component
public class PartidaWebSocketPublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final PartidaService partidaService;

    public PartidaWebSocketPublisher(SimpMessagingTemplate messagingTemplate, PartidaService partidaService) {
        this.messagingTemplate = messagingTemplate;
        this.partidaService = partidaService;
    }

    public void publicarLobby(Long partidaId, String tipo) {
        Partida partida = partidaService.buscarPartida(partidaId);
        List<ParticipacaoResponse> participantes = partidaService.listarParticipantes(partidaId)
                .stream()
                .map(ParticipacaoResponse::fromModel)
                .toList();

        publicar(partidaId, "lobby", tipo, LobbyPartidaResponse.fromModel(partida, participantes));
    }

    public void publicarPartida(Partida partida, String tipo) {
        publicar(partida.getId(), "status", tipo, PartidaResponse.fromModel(partida));
    }

    public void publicarResposta(Resposta resposta) {
        Long partidaId = resposta.getParticipacao().getPartida().getId();

        publicar(partidaId, "respostas", "RESPOSTA_REGISTRADA", RespostaPartidaResponse.fromModel(resposta));
        publicarPlacarParcial(partidaId);
        publicarFinalizacaoSeNecessario(partidaId);
    }

    public void publicarPlacarParcial(Long partidaId) {
        List<ParticipacaoResponse> placar = partidaService.listarParticipantes(partidaId)
                .stream()
                .sorted((primeiro, segundo) -> segundo.getScorePartida().compareTo(primeiro.getScorePartida()))
                .map(ParticipacaoResponse::fromModel)
                .toList();

        publicar(partidaId, "placar", "PLACAR_ATUALIZADO", placar);
    }

    public void publicarLeaderboard(Long partidaId) {
        List<ParticipacaoResponse> leaderboard = partidaService.listarLeaderboard(partidaId)
                .stream()
                .map(ParticipacaoResponse::fromModel)
                .toList();

        publicar(partidaId, "leaderboard", "LEADERBOARD_ATUALIZADO", leaderboard);
    }

    private void publicarFinalizacaoSeNecessario(Long partidaId) {
        Partida partida = partidaService.buscarPartida(partidaId);

        if (partida.getStatusSala() == PartidaStatus.FINALIZADA) {
            publicarPartida(partida, "PARTIDA_FINALIZADA");
            publicarLeaderboard(partidaId);
        }
    }

    private void publicar(Long partidaId, String canal, String tipo, Object dados) {
        messagingTemplate.convertAndSend(
                "/topic/partidas/" + partidaId + "/" + canal,
                PartidaEventoResponse.of(tipo, partidaId, dados));
    }
}
