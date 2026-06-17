package deu_branco_api.controller.dto;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta padronizada para erros da API.")
public record ErroResponse(
        @Schema(description = "Data e hora do erro.")
        OffsetDateTime timestamp,

        @Schema(description = "Codigo HTTP.", example = "400")
        Integer status,

        @Schema(description = "Descricao curta do erro.", example = "Bad Request")
        String erro,

        @Schema(description = "Mensagem detalhada do erro.")
        String mensagem) {
}
