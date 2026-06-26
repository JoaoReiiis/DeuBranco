package deu_branco_api.controller.dto;

import deu_branco_api.model.Participacao;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados da participacao de um jogador na partida.")
public record ParticipacaoResponse(
        @Schema(description = "Identificador da participacao.", example = "30")
        Long id,

        @Schema(description = "Identificador da partida.", example = "10")
        Long partidaId,

        @Schema(description = "Jogador participante.")
        JogadorResponse jogador,

        @Schema(description = "Pontuacao acumulada na partida.", example = "150")
        Integer scorePartida) {

    public static ParticipacaoResponse fromModel(Participacao participacao) {
        return new ParticipacaoResponse(
                participacao.getId(),
                participacao.getPartida().getId(),
                JogadorResponse.fromModel(participacao.getJogador()),
                participacao.getScorePartida());
    }
}
