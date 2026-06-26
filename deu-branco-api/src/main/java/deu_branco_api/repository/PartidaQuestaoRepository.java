package deu_branco_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import deu_branco_api.model.PartidaQuestao;
import deu_branco_api.model.PartidaStatus;

public interface PartidaQuestaoRepository extends JpaRepository<PartidaQuestao, Long> {

    long countByPartidaId(Long partidaId);

    boolean existsByQuestaoIdAndPartidaStatusSala(Long questaoId, PartidaStatus statusSala);

    List<PartidaQuestao> findByPartidaIdOrderByOrdemAsc(Long partidaId);

    @EntityGraph(attributePaths = { "partida", "questao" })
    Optional<PartidaQuestao> findByIdAndPartidaId(Long id, Long partidaId);
}
