package deu_branco_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import deu_branco_api.model.Participacao;
import deu_branco_api.model.PartidaStatus;
import jakarta.persistence.LockModeType;

public interface ParticipacaoRepository extends JpaRepository<Participacao, Long> {

    long countByPartidaId(Long partidaId);

    void deleteByPartidaId(Long partidaId);

    boolean existsByPartidaIdAndJogadorId(Long partidaId, Long jogadorId);

    Optional<Participacao> findByPartidaIdAndJogadorId(Long partidaId, Long jogadorId);

    @EntityGraph(attributePaths = { "partida", "jogador" })
    List<Participacao> findByPartidaIdOrderByIdAsc(Long partidaId);

    @EntityGraph(attributePaths = { "partida", "jogador" })
    List<Participacao> findByPartidaIdOrderByScorePartidaDesc(Long partidaId);

    @EntityGraph(attributePaths = { "partida", "jogador" })
    List<Participacao> findByJogadorEmailAndPartidaStatusSalaOrderByPartidaCriadoEmDesc(
            String email,
            PartidaStatus statusSala);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p
            FROM Participacao p
            JOIN FETCH p.partida partida
            JOIN FETCH p.jogador jogador
            WHERE partida.id = :partidaId
              AND jogador.id = :jogadorId
            """)
    Optional<Participacao> buscarPorPartidaEJogadorComLock(
            @Param("partidaId") Long partidaId,
            @Param("jogadorId") Long jogadorId);
}
