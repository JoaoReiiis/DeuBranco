package deu_branco_api.controller.dto;

import java.util.List;

import deu_branco_api.model.Partida;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estado atual do lobby de uma partida.")
public record LobbyPartidaResponse(
        @Schema(description = "Dados da partida.")
        PartidaResponse partida,

        @Schema(description = "Participantes vinculados a partida.")
        List<ParticipacaoResponse> participantes) {

    public static LobbyPartidaResponse fromModel(Partida partida, List<ParticipacaoResponse> participantes) {
        return new LobbyPartidaResponse(PartidaResponse.fromModel(partida), participantes);
    }
}
