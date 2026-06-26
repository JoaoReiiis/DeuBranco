package deu_branco_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import deu_branco_api.model.Partida;
import deu_branco_api.model.PartidaStatus;
import jakarta.persistence.LockModeType;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    boolean existsByCodigoPinAndStatusSalaNot(String codigoPin, PartidaStatus statusSala);

    Optional<Partida> findByCodigoPin(String codigoPin);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p
            FROM Partida p
            JOIN FETCH p.host
            WHERE p.id = :id
            """)
    Optional<Partida> buscarPorIdComLock(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p
            FROM Partida p
            JOIN FETCH p.host
            WHERE p.codigoPin = :codigoPin
            """)
    Optional<Partida> buscarPorCodigoPinComLock(@Param("codigoPin") String codigoPin);

    @Query(value = """
            SELECT id_partida
            FROM partida
            WHERE status_sala = 'ANDAMENTO'
              AND iniciada_em IS NOT NULL
              AND iniciada_em + (tempo_de_jogo * INTERVAL '1 second') <= NOW()
            """, nativeQuery = true)
    List<Long> buscarIdsPartidasExpiradas();
}
