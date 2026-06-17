package deu_branco_api.controller.dto;

import java.util.List;

import deu_branco_api.model.Questao;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de uma questao cadastrada.")
public record QuestaoResponse(
        @Schema(description = "Identificador unico da questao.", example = "1")
        Long id,

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

        @Schema(description = "Gabarito da questao.", example = "B")
        String alternativaCorreta,

        @Schema(description = "Disciplina da questao.", example = "Ingles")
        String disciplina,

        @Schema(description = "Instituicao de origem da questao.", example = "ENEM")
        String instituicao,

        @Schema(description = "URLs ou caminhos de imagens associadas a questao.")
        List<String> imagens,

        @Schema(description = "Indica se a questao esta ativa.", example = "true")
        Boolean ativo) {

    public static QuestaoResponse fromModel(Questao questao) {
        return new QuestaoResponse(
                questao.getId(),
                questao.getEnunciado(),
                questao.getOpcaoA(),
                questao.getOpcaoB(),
                questao.getOpcaoC(),
                questao.getOpcaoD(),
                questao.getOpcaoE(),
                questao.getAlternativaCorreta(),
                questao.getDisciplina().name(),
                questao.getInstituicao().name(),
                questao.getImagens(),
                questao.getAtivo());
    }
}
