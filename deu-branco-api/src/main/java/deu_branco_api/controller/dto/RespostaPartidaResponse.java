package deu_branco_api.controller.dto;

import deu_branco_api.model.Resposta;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta registrada em uma partida.")
public record RespostaPartidaResponse(
        @Schema(description = "Identificador da resposta.", example = "70")
        Long id,

        @Schema(description = "Identificador da participacao.", example = "30")
        Long participacaoId,

        @Schema(description = "Identificador da questao dentro da partida.", example = "50")
        Long partidaQuestaoId,

        @Schema(description = "Alternativa marcada pelo jogador.", example = "B")
        String alternativaMarcada,

        @Schema(description = "Tempo gasto para responder, em segundos.", example = "12")
        Integer tempoResposta,

        @Schema(description = "Indica se a alternativa marcada esta correta.", example = "true")
        Boolean acertou,

        @Schema(description = "Pontuacao acumulada do jogador apos a resposta.", example = "150")
        Integer scorePartida) {

    public static RespostaPartidaResponse fromModel(Resposta resposta) {
        return new RespostaPartidaResponse(
                resposta.getId(),
                resposta.getParticipacao().getId(),
                resposta.getPartidaQuestao().getId(),
                resposta.getAlternativaMarcada(),
                resposta.getTempoResposta(),
                resposta.getAcertou(),
                resposta.getParticipacao().getScorePartida());
    }
}
