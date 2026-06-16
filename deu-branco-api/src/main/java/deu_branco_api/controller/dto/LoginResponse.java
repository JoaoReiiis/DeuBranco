package deu_branco_api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token retornado apos autenticacao.")
public record LoginResponse(
        @Schema(description = "Token JWT usado nas requisicoes autenticadas.")
        String accessToken,

        @Schema(description = "Tipo do token.", example = "Bearer")
        String tokenType,

        @Schema(description = "Tempo de expiracao do token em segundos.", example = "3600")
        long expiresIn) {
}
