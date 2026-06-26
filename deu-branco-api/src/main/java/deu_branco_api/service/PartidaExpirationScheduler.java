package deu_branco_api.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import deu_branco_api.model.Partida;
import deu_branco_api.repository.PartidaRepository;

@Component
public class PartidaExpirationScheduler {

    private final PartidaRepository partidaRepository;
    private final PartidaService partidaService;
    private final PartidaWebSocketPublisher webSocketPublisher;

    public PartidaExpirationScheduler(
            PartidaRepository partidaRepository,
            PartidaService partidaService,
            PartidaWebSocketPublisher webSocketPublisher) {
        this.partidaRepository = partidaRepository;
        this.partidaService = partidaService;
        this.webSocketPublisher = webSocketPublisher;
    }

    @Scheduled(fixedDelayString = "${app.partidas.expiration-check-ms:5000}")
    public void finalizarPartidasExpiradas() {
        List<Long> partidasExpiradas = partidaRepository.buscarIdsPartidasExpiradas();

        for (Long partidaId : partidasExpiradas) {
            Partida partida = partidaService.finalizarPartidaPorSistema(partidaId);
            webSocketPublisher.publicarPartida(partida, "PARTIDA_FINALIZADA_POR_TEMPO");
            webSocketPublisher.publicarPlacarParcial(partidaId);
            webSocketPublisher.publicarLeaderboard(partidaId);
        }
    }
}
