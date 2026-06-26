package deu_branco_api.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import deu_branco_api.model.Disciplina;
import deu_branco_api.model.Instituicao;
import deu_branco_api.model.PartidaStatus;
import deu_branco_api.model.Questao;
import deu_branco_api.repository.PartidaQuestaoRepository;
import deu_branco_api.repository.QuestaoRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class QuestaoServiceTest {

    @Mock
    private QuestaoRepository questaoRepository;

    @Mock
    private PartidaQuestaoRepository partidaQuestaoRepository;

    private QuestaoService questaoService;

    @BeforeEach
    void setUp() {
        questaoService = new QuestaoService(questaoRepository, partidaQuestaoRepository);
    }

    @Test
    void deveCriarQuestaoNormalizandoDados() {
        Questao questao = questaoValida();
        questao.setId(10L);
        questao.setEnunciado("  Texto da pergunta  ");
        questao.setDisciplina(Disciplina.PORTUGUES);
        questao.setInstituicao(Instituicao.ENEM);
        questao.setAlternativaCorreta(" b ");
        questao.setImagens(List.of("  https://enem.dev/imagem.jpg  ", " "));

        when(questaoRepository.existsByEnunciadoAndInstituicao("Texto da pergunta", Instituicao.ENEM)).thenReturn(false);
        when(questaoRepository.save(questao)).thenReturn(questao);

        Questao criada = questaoService.criar(questao);

        assertThat(criada.getId()).isNull();
        assertThat(criada.getEnunciado()).isEqualTo("Texto da pergunta");
        assertThat(criada.getDisciplina()).isEqualTo(Disciplina.PORTUGUES);
        assertThat(criada.getInstituicao()).isEqualTo(Instituicao.ENEM);
        assertThat(criada.getAlternativaCorreta()).isEqualTo("B");
        assertThat(criada.getImagens()).containsExactly("https://enem.dev/imagem.jpg");
        assertThat(criada.getAtivo()).isTrue();
        verify(questaoRepository).save(questao);
    }

    @Test
    void deveImpedirCriacaoDuplicadaPorEnunciadoEInstituicao() {
        Questao questao = questaoValida();

        when(questaoRepository.existsByEnunciadoAndInstituicao("Texto da pergunta", Instituicao.ENEM)).thenReturn(true);

        try {
            questaoService.criar(questao);
            fail("Deveria lancar erro para questao duplicada.");
        } catch (IllegalArgumentException exception) {
        }

        verify(questaoRepository, never()).save(questao);
    }

    @Test
    void deveImpedirCriacaoQuandoAlternativaCorretaForInvalida() {
        Questao questao = questaoValida();
        questao.setAlternativaCorreta("Z");

        try {
            questaoService.criar(questao);
            fail("Deveria lancar erro quando alternativa correta nao existir.");
        } catch (IllegalArgumentException exception) {
        }

        verify(questaoRepository, never()).save(questao);
    }

    @Test
    void deveBuscarQuestaoPorId() {
        Questao questao = questaoValida();
        questao.setId(1L);

        when(questaoRepository.findById(1L)).thenReturn(Optional.of(questao));

        Questao encontrada = questaoService.buscarPorId(1L);

        assertThat(encontrada).isSameAs(questao);
    }

    @Test
    void deveLancarErroQuandoBuscarIdInexistente() {
        when(questaoRepository.findById(99L)).thenReturn(Optional.empty());

        try {
            questaoService.buscarPorId(99L);
            fail("Deveria lancar erro para questao inexistente.");
        } catch (EntityNotFoundException exception) {
        }
    }

    @Test
    void deveListarQuestoesFiltrandoPorDisciplinaEInstituicao() {
        Questao questao = questaoValida();
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Questao> pagina = new PageImpl<>(List.of(questao));

        when(questaoRepository.buscarComFiltros(Disciplina.PORTUGUES, Instituicao.ENEM, true, pageable)).thenReturn(pagina);

        Page<Questao> resultado = questaoService.listar("PORTUGUES", " ENEM ", true, pageable);

        assertThat(resultado.getContent()).containsExactly(questao);
    }

    @Test
    void deveAtualizarQuestaoExistente() {
        Questao questao = questaoValida();
        questao.setId(1L);
        Questao dadosAtualizados = questaoValida();
        dadosAtualizados.setEnunciado("  Nova pergunta  ");
        dadosAtualizados.setAlternativaCorreta(" c ");
        dadosAtualizados.setAtivo(false);

        when(questaoRepository.findById(1L)).thenReturn(Optional.of(questao));
        when(questaoRepository.existsByEnunciadoAndInstituicaoAndIdNot("Nova pergunta", Instituicao.ENEM, 1L))
                .thenReturn(false);
        when(questaoRepository.save(questao)).thenReturn(questao);

        Questao atualizada = questaoService.atualizar(1L, dadosAtualizados);

        assertThat(atualizada.getEnunciado()).isEqualTo("Nova pergunta");
        assertThat(atualizada.getAlternativaCorreta()).isEqualTo("C");
        assertThat(atualizada.getAtivo()).isFalse();
        verify(questaoRepository).save(questao);
    }

    @Test
    void deveRemoverQuestaoComExclusaoLogica() {
        Questao questao = questaoValida();
        questao.setId(1L);

        when(questaoRepository.findById(1L)).thenReturn(Optional.of(questao));
        when(partidaQuestaoRepository.existsByQuestaoIdAndPartidaStatusSala(1L, PartidaStatus.ANDAMENTO))
                .thenReturn(false);

        questaoService.remover(1L);

        assertThat(questao.getAtivo()).isFalse();
        verify(questaoRepository).save(questao);
    }

    @Test
    void deveImpedirRemocaoQuandoQuestaoEstaVinculadaAPartidaEmAndamento() {
        Questao questao = questaoValida();
        questao.setId(1L);

        when(questaoRepository.findById(1L)).thenReturn(Optional.of(questao));
        when(partidaQuestaoRepository.existsByQuestaoIdAndPartidaStatusSala(1L, PartidaStatus.ANDAMENTO))
                .thenReturn(true);

        try {
            questaoService.remover(1L);
            fail("Deveria bloquear questao vinculada a partida em andamento.");
        } catch (IllegalArgumentException exception) {
            assertThat(exception).hasMessage("Pergunta vinculada a partida em andamento nao pode ser excluida.");
        }

        assertThat(questao.getAtivo()).isTrue();
        verify(questaoRepository, never()).save(questao);
    }

    private Questao questaoValida() {
        Questao questao = new Questao();
        questao.setEnunciado("Texto da pergunta");
        questao.setOpcaoA("Texto A");
        questao.setOpcaoB("Texto B");
        questao.setOpcaoC("Texto C");
        questao.setOpcaoD("Texto D");
        questao.setOpcaoE("Texto E");
        questao.setAlternativaCorreta("B");
        questao.setDisciplina(Disciplina.PORTUGUES);
        questao.setInstituicao(Instituicao.ENEM);
        questao.setImagens(List.of("https://enem.dev/imagem.jpg"));
        return questao;
    }
}
