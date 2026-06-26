package deu_branco_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import deu_branco_api.model.Resposta;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {

    boolean existsByParticipacaoIdAndPartidaQuestaoId(Long participacaoId, Long partidaQuestaoId);

    long countByParticipacaoPartidaId(Long partidaId);

    List<Resposta> findByParticipacaoPartidaId(Long partidaId);
}
