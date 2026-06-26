package deu_branco_api.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import deu_branco_api.model.Partida;
import deu_branco_api.model.PartidaStatus;
import deu_branco_api.repository.PartidaRepository;

@ExtendWith(MockitoExtension.class)
class PartidaExpirationSchedulerTest {

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private PartidaService partidaService;

    @Mock
    private PartidaWebSocketPublisher webSocketPublisher;

    @Test
    void deveFinalizarPartidasExpiradasEPublicarEventos() {
        Partida partida = new Partida();
        partida.setId(10L);
        partida.setStatusSala(PartidaStatus.FINALIZADA);

        when(partidaRepository.buscarIdsPartidasExpiradas()).thenReturn(List.of(10L));
        when(partidaService.finalizarPartidaPorSistema(10L)).thenReturn(partida);

        PartidaExpirationScheduler scheduler = new PartidaExpirationScheduler(
                partidaRepository,
                partidaService,
                webSocketPublisher);

        scheduler.finalizarPartidasExpiradas();

        verify(partidaService).finalizarPartidaPorSistema(10L);
        verify(webSocketPublisher).publicarPartida(partida, "PARTIDA_FINALIZADA_POR_TEMPO");
        verify(webSocketPublisher).publicarPlacarParcial(10L);
        verify(webSocketPublisher).publicarLeaderboard(10L);
    }
}
