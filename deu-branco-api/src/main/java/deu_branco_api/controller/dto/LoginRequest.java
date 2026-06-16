package deu_branco_api.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciais para autenticacao.")
public record LoginRequest(
        @Schema(description = "Email cadastrado para login.", example = "joao@email.com")
        @Email
        @NotBlank
        String email,

        @Schema(description = "Senha do jogador.", example = "12345678")
        @NotBlank
        String senha) {
}
