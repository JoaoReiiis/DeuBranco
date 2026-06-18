package deu_branco_api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualizar o nome publico de um jogador.")
public record JogadorNomeUpdateRequest(
        @Schema(description = "Novo nome publico do jogador.", example = "Joao Reis")
        @NotBlank
        @Size(min = 2, max = 100)
        String nome) {
}
