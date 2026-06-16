package deu_branco_api.controller.dto;

import deu_branco_api.model.Jogador;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criar uma conta de jogador.")
public record JogadorCreateRequest(
        @Schema(description = "Nome publico do jogador.", example = "Joao Reis")
        @NotBlank
        @Size(min = 2, max = 100)
        String nome,

        @Schema(description = "Email usado para login.", example = "joao@email.com")
        @Email
        @NotBlank
        @Size(max = 180)
        String email,

        @Schema(description = "Senha em texto puro enviada apenas na requisicao. Sera armazenada com hash BCrypt.", example = "12345678")
        @NotBlank
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
