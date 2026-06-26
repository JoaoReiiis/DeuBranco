package deu_branco_api.controller.dto;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Evento publicado nos topicos WebSocket de uma partida.")
public record PartidaEventoResponse(
        @Schema(description = "Tipo do evento.", example = "PARTIDA_INICIADA")
        String tipo,

        @Schema(description = "Identificador da partida.", example = "10")
        Long partidaId,

        @Schema(description = "Dados relacionados ao evento.")
        Object dados,

        @Schema(description = "Data e hora de publicacao do evento.")
        OffsetDateTime publicadoEm) {

    public static PartidaEventoResponse of(String tipo, Long partidaId, Object dados) {
        return new PartidaEventoResponse(tipo, partidaId, dados, OffsetDateTime.now());
    }
}
