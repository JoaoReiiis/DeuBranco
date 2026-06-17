package deu_branco_api.controller.dto;

import java.util.List;

import deu_branco_api.model.Disciplina;
import deu_branco_api.model.Instituicao;
import deu_branco_api.model.Questao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criar ou atualizar uma questao.")
public record QuestaoRequest(
        @Schema(description = "Texto completo da pergunta.", example = "Qual alternativa completa corretamente a frase?")
        @NotBlank
        String enunciado,

        @Schema(description = "Alternativa A.", example = "Option A")
        @NotBlank
        String opcaoA,

        @Schema(description = "Alternativa B.", example = "Option B")
        @NotBlank
        String opcaoB,

        @Schema(description = "Alternativa C.", example = "Option C")
        @NotBlank
        String opcaoC,

        @Schema(description = "Alternativa D.", example = "Option D")
        @NotBlank
        String opcaoD,

        @Schema(description = "Alternativa E.", example = "Option E")
        @NotBlank
        String opcaoE,

        @Schema(description = "Gabarito da questao.", example = "B", allowableValues = {"A", "B", "C", "D", "E"})
        @NotBlank
        @Size(min = 1, max = 1)
        String alternativaCorreta,

        @Schema(description = "Disciplina da questao. Deve ser um dos valores definidos no enum.",
                example = "INGLES")
        @NotBlank
        @Size(max = 100)
        String disciplina,

        @Schema(description = "Instituicao de origem da questao. Deve ser um dos valores definidos no enum.",
                example = "ENEM")
        @NotBlank
        @Size(max = 150)
        String instituicao,

        @Schema(description = "URLs ou caminhos de imagens associadas a questao.")
        List<@Size(max = 255) String> imagens,

        @Schema(description = "Indica se a questao esta ativa.", example = "true")
        Boolean ativo) {

    public Questao toModel() {
        Questao questao = new Questao();
        questao.setEnunciado(enunciado);
        questao.setOpcaoA(opcaoA);
        questao.setOpcaoB(opcaoB);
        questao.setOpcaoC(opcaoC);
        questao.setOpcaoD(opcaoD);
        questao.setOpcaoE(opcaoE);
        questao.setAlternativaCorreta(alternativaCorreta);
        questao.setDisciplina(Disciplina.from(disciplina));
        questao.setInstituicao(Instituicao.from(instituicao));
        questao.setImagens(imagens);
        questao.setAtivo(ativo);
        return questao;
    }
}
