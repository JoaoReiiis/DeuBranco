package deu_branco_api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import deu_branco_api.controller.dto.JogadorCreateRequest;
import deu_branco_api.controller.dto.JogadorNomeUpdateRequest;
import deu_branco_api.controller.dto.JogadorResponse;
import deu_branco_api.controller.dto.JogadorUpdateRequest;
import deu_branco_api.model.Jogador;
import deu_branco_api.service.JogadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Jogadores", description = "Cadastro e gerenciamento da conta do jogador autenticado.")
@RestController
@RequestMapping("/jogadores")
public class JogadorController {

    private final JogadorService jogadorService;

    public JogadorController(JogadorService jogadorService) {
        this.jogadorService = jogadorService;
    }

    @Operation(
            summary = "Buscar minha conta",
            description = "Retorna os dados da conta vinculada ao token JWT enviado na requisicao.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta encontrada."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido.")
    })
    @GetMapping("/me")
    public ResponseEntity<JogadorResponse> buscarMe(@AuthenticationPrincipal Jwt jwt) {
        Jogador jogador = jogadorService.buscarPorEmail(jwt.getSubject());

        return ResponseEntity.ok(JogadorResponse.fromModel(jogador));
    }

    @Operation(
            summary = "Criar conta",
            description = "Cria uma nova conta de jogador. Este endpoint e publico e nao exige token.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conta criada."),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou email ja cadastrado.")
    })
    @PostMapping
    public ResponseEntity<JogadorResponse> criar(@Valid @RequestBody JogadorCreateRequest request) {
        Jogador jogador = jogadorService.criar(request.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(jogador.getId())
                .toUri();

        return ResponseEntity.created(location).body(JogadorResponse.fromModel(jogador));
    }

    @Operation(
            summary = "Atualizar minha conta",
            description = "Atualiza nome, email e opcionalmente a senha da conta vinculada ao token JWT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta atualizada."),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou email ja cadastrado."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido.")
    })
    @PutMapping("/me")
    public ResponseEntity<JogadorResponse> atualizarMe(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody JogadorUpdateRequest request) {
        Jogador jogador = jogadorService.atualizarJogadorAutenticado(jwt.getSubject(), request.toModel());

        return ResponseEntity.ok(JogadorResponse.fromModel(jogador));
    }

    @Operation(
            summary = "Excluir minha conta",
            description = "Remove a conta vinculada ao token JWT enviado na requisicao.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Conta removida."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido.")
    })
    @DeleteMapping("/me")
    public ResponseEntity<Void> removerMe(@AuthenticationPrincipal Jwt jwt) {
        jogadorService.removerJogadorAutenticado(jwt.getSubject());

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualizar nome de outro jogador",
            description = "Permite que um ADMIN atualize apenas o nome publico de outro jogador.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nome atualizado."),
            @ApiResponse(responseCode = "400", description = "Dados invalidos."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido."),
            @ApiResponse(responseCode = "403", description = "Usuario autenticado nao e ADMIN."),
            @ApiResponse(responseCode = "404", description = "Jogador nao encontrado.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<JogadorResponse> atualizarNomeDeOutroJogador(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @Valid @RequestBody JogadorNomeUpdateRequest request) {
        Jogador jogador = jogadorService.atualizarNomeDeOutroJogador(jwt.getSubject(), id, request.nome());

        return ResponseEntity.ok(JogadorResponse.fromModel(jogador));
    }

    @Operation(
            summary = "Excluir outro jogador",
            description = "Permite que um ADMIN remova a conta de outro jogador.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Jogador removido."),
            @ApiResponse(responseCode = "400", description = "Operacao invalida."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido."),
            @ApiResponse(responseCode = "403", description = "Usuario autenticado nao e ADMIN."),
            @ApiResponse(responseCode = "404", description = "Jogador nao encontrado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerOutroJogador(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        jogadorService.removerOutroJogador(jwt.getSubject(), id);

        return ResponseEntity.noContent().build();
    }
}
