package deu_branco_api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para entrar em uma partida por PIN.")
public record EntrarPartidaRequest(
        @Schema(description = "PIN alfanumerico da partida.", example = "ABC123")
        @NotBlank
        @Size(max = 10)
        String codigoPin) {
}
