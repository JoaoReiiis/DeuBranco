package deu_branco_api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import deu_branco_api.model.Disciplina;
import deu_branco_api.model.Instituicao;
import deu_branco_api.model.Questao;

public interface QuestaoRepository extends JpaRepository<Questao, Long> {

    @Query("""
            SELECT q
            FROM Questao q
            WHERE (:disciplina IS NULL OR q.disciplina = :disciplina)
              AND (:instituicao IS NULL OR q.instituicao = :instituicao)
              AND (:ativo IS NULL OR q.ativo = :ativo)
            """)
    Page<Questao> buscarComFiltros(
            @Param("disciplina") Disciplina disciplina,
            @Param("instituicao") Instituicao instituicao,
            @Param("ativo") Boolean ativo,
            Pageable pageable);

    boolean existsByEnunciadoAndInstituicao(String enunciado, Instituicao instituicao);

    boolean existsByEnunciadoAndInstituicaoAndIdNot(String enunciado, Instituicao instituicao, Long id);

    long countByAtivoTrueAndDisciplinaIn(Collection<Disciplina> disciplinas);

    @Query(value = """
            SELECT *
            FROM pergunta
            WHERE ativo = TRUE
              AND disciplina IN (:disciplinas)
            ORDER BY random()
            LIMIT :quantidade
            """, nativeQuery = true)
    List<Questao> sortearAtivasPorDisciplinas(
            @Param("disciplinas") Collection<String> disciplinas,
            @Param("quantidade") int quantidade);
}
