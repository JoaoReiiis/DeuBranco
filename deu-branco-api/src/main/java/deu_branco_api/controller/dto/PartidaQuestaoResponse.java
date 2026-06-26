package deu_branco_api.controller.dto;

import java.util.List;

import deu_branco_api.model.PartidaQuestao;
import deu_branco_api.model.Questao;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Questao vinculada a uma partida, sem exposicao do gabarito.")
public record PartidaQuestaoResponse(
        @Schema(description = "Identificador da questao dentro da partida.", example = "50")
        Long id,

        @Schema(description = "Identificador da questao original.", example = "40")
        Long questaoId,

        @Schema(description = "Ordem de exibicao da questao.", example = "1")
        Integer ordem,

        @Schema(description = "Texto completo da pergunta.")
        String enunciado,

        @Schema(description = "Alternativa A.")
        String opcaoA,

        @Schema(description = "Alternativa B.")
        String opcaoB,

        @Schema(description = "Alternativa C.")
        String opcaoC,

        @Schema(description = "Alternativa D.")
        String opcaoD,

        @Schema(description = "Alternativa E.")
        String opcaoE,

        @Schema(description = "Disciplina da questao.", example = "MATEMATICA")
        String disciplina,

        @Schema(description = "Instituicao de origem da questao.", example = "ENEM")
        String instituicao,

        @Schema(description = "URLs ou caminhos de imagens associadas a questao.")
        List<String> imagens) {

    public static PartidaQuestaoResponse fromModel(PartidaQuestao partidaQuestao) {
        Questao questao = partidaQuestao.getQuestao();

        return new PartidaQuestaoResponse(
                partidaQuestao.getId(),
                questao.getId(),
                partidaQuestao.getOrdem(),
                questao.getEnunciado(),
                questao.getOpcaoA(),
                questao.getOpcaoB(),
                questao.getOpcaoC(),
                questao.getOpcaoD(),
                questao.getOpcaoE(),
                questao.getDisciplina().name(),
                questao.getInstituicao().name(),
                questao.getImagens());
    }
}
