package deu_branco_api.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import deu_branco_api.controller.dto.QuestaoRequest;
import deu_branco_api.controller.dto.QuestaoResponse;
import deu_branco_api.model.Questao;
import deu_branco_api.service.QuestaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Questoes", description = "CRUD de questoes.")
@RestController
@RequestMapping("/questoes")
public class QuestaoController {

    private final QuestaoService questaoService;

    public QuestaoController(QuestaoService questaoService) {
        this.questaoService = questaoService;
    }

    @Operation(summary = "Listar questoes", description = "Lista questoes com filtros opcionais.")
    @ApiResponse(responseCode = "200", description = "Questoes listadas.")
    @GetMapping
    public ResponseEntity<Page<QuestaoResponse>> listar(
            @RequestParam(required = false) String disciplina,
            @RequestParam(required = false) String instituicao,
            @RequestParam(required = false) Boolean ativo,
            Pageable pageable) {
        Page<QuestaoResponse> questoes = questaoService.listar(disciplina, instituicao, ativo, pageable)
                .map(QuestaoResponse::fromModel);

        return ResponseEntity.ok(questoes);
    }

    @Operation(summary = "Buscar questao por ID", description = "Retorna uma questao pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Questao encontrada."),
            @ApiResponse(responseCode = "404", description = "Questao nao encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<QuestaoResponse> buscarPorId(@PathVariable Long id) {
        Questao questao = questaoService.buscarPorId(id);

        return ResponseEntity.ok(QuestaoResponse.fromModel(questao));
    }

    @Operation(summary = "Criar questao", description = "Cadastra uma nova questao.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Questao criada."),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou questao duplicada.")
    })
    @PostMapping
    public ResponseEntity<QuestaoResponse> criar(@Valid @RequestBody QuestaoRequest request) {
        Questao questao = questaoService.criar(request.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(questao.getId())
                .toUri();

        return ResponseEntity.created(location).body(QuestaoResponse.fromModel(questao));
    }

    @Operation(summary = "Atualizar questao", description = "Atualiza todos os dados de uma questao.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Questao atualizada."),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou questao duplicada."),
            @ApiResponse(responseCode = "404", description = "Questao nao encontrada.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<QuestaoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody QuestaoRequest request) {
        Questao questao = questaoService.atualizar(id, request.toModel());

        return ResponseEntity.ok(QuestaoResponse.fromModel(questao));
    }

    @Operation(summary = "Excluir questao", description = "Desativa uma questao por exclusao logica.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Questao desativada."),
            @ApiResponse(responseCode = "404", description = "Questao nao encontrada.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        questaoService.remover(id);

        return ResponseEntity.noContent().build();
    }
}
