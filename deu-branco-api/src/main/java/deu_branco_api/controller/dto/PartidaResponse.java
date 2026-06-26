package deu_branco_api.controller.dto;

import java.time.LocalDateTime;

import deu_branco_api.model.Partida;
import deu_branco_api.model.PartidaStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de uma partida multiplayer.")
public record PartidaResponse(
        @Schema(description = "Identificador unico da partida.", example = "10")
        Long id,

        @Schema(description = "Jogador host da partida.")
        JogadorResponse host,

        @Schema(description = "Tempo limite para resposta, em segundos.", example = "30")
        Integer tempoDeJogo,

        @Schema(description = "PIN da sala.", example = "ABC123")
        String codigoPin,

        @Schema(description = "Status atual da sala.", example = "AGUARDANDO")
        PartidaStatus statusSala,

        @Schema(description = "Data e hora de criacao da partida.")
        LocalDateTime criadoEm,

        @Schema(description = "Data e hora em que a partida foi iniciada.")
        LocalDateTime iniciadaEm,

        @Schema(description = "Data e hora em que a partida foi finalizada.")
        LocalDateTime finalizadaEm) {

    public static PartidaResponse fromModel(Partida partida) {
        return new PartidaResponse(
                partida.getId(),
                JogadorResponse.fromModel(partida.getHost()),
                partida.getTempoDeJogo(),
                partida.getCodigoPin(),
                partida.getStatusSala(),
                partida.getCriadoEm(),
                partida.getIniciadaEm(),
                partida.getFinalizadaEm());
    }
}
