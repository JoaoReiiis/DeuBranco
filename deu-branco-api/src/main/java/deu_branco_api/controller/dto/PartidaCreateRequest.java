package deu_branco_api.controller.dto;

import java.util.List;

import deu_branco_api.model.Disciplina;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Dados para criar uma partida multiplayer.")
public record PartidaCreateRequest(
        @Schema(description = "Numero de questoes da partida.", example = "10")
        @NotNull
        @Min(5)
        @Max(30)
        Integer numeroQuestoes,

        @Schema(description = "Disciplinas usadas no sorteio das questoes.", example = "[\"MATEMATICA\"]")
        @NotEmpty
        List<String> disciplinas,

        @Schema(description = "Tempo limite para resposta, em segundos.", example = "30")
        @NotNull
        @Positive
        Integer tempoDeJogo) {

    public List<Disciplina> disciplinasComoEnum() {
        return disciplinas.stream()
                .map(Disciplina::from)
                .toList();
    }
}
