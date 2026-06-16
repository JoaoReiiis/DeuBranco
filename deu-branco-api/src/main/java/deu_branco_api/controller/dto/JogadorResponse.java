package deu_branco_api.controller.dto;

import deu_branco_api.model.Jogador;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados publicos da conta do jogador.")
public record JogadorResponse(
        @Schema(description = "Identificador unico do jogador.", example = "1")
        Long id,

        @Schema(description = "Nome publico do jogador.", example = "Joao Reis")
        String nome,

        @Schema(description = "Email do jogador.", example = "joao@email.com")
        String email) {

    public static JogadorResponse fromModel(Jogador jogador) {
        return new JogadorResponse(
                jogador.getId(),
                jogador.getNome(),
                jogador.getEmail());
    }
}
