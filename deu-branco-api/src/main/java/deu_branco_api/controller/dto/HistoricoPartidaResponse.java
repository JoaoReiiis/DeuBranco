package deu_branco_api.controller.dto;

import java.time.LocalDateTime;

import deu_branco_api.model.Participacao;
import deu_branco_api.model.PartidaStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Item do historico de partidas de um jogador.")
public record HistoricoPartidaResponse(
        @Schema(description = "Identificador da partida.", example = "10")
        Long partidaId,

        @Schema(description = "PIN da partida.", example = "ABC123")
        String codigoPin,

        @Schema(description = "Status da partida.", example = "FINALIZADA")
        PartidaStatus statusSala,

        @Schema(description = "Pontuacao obtida pelo jogador.", example = "450")
        Integer scorePartida,

        @Schema(description = "Data e hora de criacao da partida.")
        LocalDateTime criadoEm) {

    public static HistoricoPartidaResponse fromModel(Participacao participacao) {
        return new HistoricoPartidaResponse(
                participacao.getPartida().getId(),
                participacao.getPartida().getCodigoPin(),
                participacao.getPartida().getStatusSala(),
                participacao.getScorePartida(),
                participacao.getPartida().getCriadoEm());
    }
}
