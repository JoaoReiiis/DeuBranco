package deu_branco_api.controller.dto;

import deu_branco_api.model.Jogador;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualizar a conta do jogador autenticado.")
public record JogadorUpdateRequest(
        @Schema(description = "Nome publico do jogador.", example = "Joao Reis Atualizado")
        @NotBlank
        @Size(min = 2, max = 100)
        String nome,

        @Schema(description = "Email usado para login.", example = "joao.novo@email.com")
        @Email
        @NotBlank
        @Size(max = 180)
        String email,

        @Schema(description = "Nova senha. Quando nao enviada, a senha atual e mantida.", example = "novaSenha123")
        @Size(min = 8, max = 72)
        String senha) {

    public Jogador toModel() {
        Jogador jogador = new Jogador();
        jogador.setNome(nome);
        jogador.setEmail(email);
        jogador.setSenha(senha);
        return jogador;
    }
}
