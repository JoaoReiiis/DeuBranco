package deu_branco_api.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import deu_branco_api.model.Jogador;

public interface JogadorRepository extends JpaRepository<Jogador, Long> {

    Optional<Jogador> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}
