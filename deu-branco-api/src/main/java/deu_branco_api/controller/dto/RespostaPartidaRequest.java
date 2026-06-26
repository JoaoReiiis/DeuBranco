package deu_branco_api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para registrar uma resposta na partida.")
public record RespostaPartidaRequest(
        @Schema(description = "Identificador da questao dentro da partida.", example = "50")
        @NotNull
        Long partidaQuestaoId,

        @Schema(description = "Alternativa marcada pelo jogador. Pode ser nula quando o tempo expirar.", example = "B")
        @Size(max = 1)
        String alternativaMarcada,

        @Schema(description = "Tempo gasto para responder, em segundos.", example = "12")
        @NotNull
        @PositiveOrZero
        Integer tempoResposta) {
}
