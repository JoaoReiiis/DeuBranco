package deu_branco_api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deu_branco_api.model.Jogador;
import deu_branco_api.repository.JogadorRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class JogadorService {

    private final JogadorRepository jogadorRepository;
    private final PasswordEncoder passwordEncoder;

    public JogadorService(JogadorRepository jogadorRepository, PasswordEncoder passwordEncoder) {
        this.jogadorRepository = jogadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Jogador buscarPorEmail(String email) {
        return jogadorRepository.findByEmail(normalizarEmail(email))
                .orElseThrow(() -> new EntityNotFoundException("Jogador nao encontrado com email: " + email));
    }

    @Transactional
    public Jogador criar(Jogador jogador) {
        String email = normalizarEmail(jogador.getEmail());

        if (jogadorRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Ja existe um jogador cadastrado com este email.");
        }

        jogador.setId(null);
        jogador.setEmail(email);
        jogador.setSenha(passwordEncoder.encode(jogador.getSenha()));

        return jogadorRepository.save(jogador);
    }

    @Transactional
    public Jogador atualizarJogadorAutenticado(String emailAutenticado, Jogador dadosAtualizados) {
        Jogador jogador = buscarPorEmail(emailAutenticado);
        String email = normalizarEmail(dadosAtualizados.getEmail());

        if (jogadorRepository.existsByEmailAndIdNot(email, jogador.getId())) {
            throw new IllegalArgumentException("Ja existe um jogador cadastrado com este email.");
        }

        jogador.setNome(dadosAtualizados.getNome());
        jogador.setEmail(email);

        if (dadosAtualizados.getSenha() != null && !dadosAtualizados.getSenha().isBlank()) {
            jogador.setSenha(passwordEncoder.encode(dadosAtualizados.getSenha()));
        }

        return jogadorRepository.save(jogador);
    }

    @Transactional
    public void removerJogadorAutenticado(String emailAutenticado) {
        Jogador jogador = buscarPorEmail(emailAutenticado);

        jogadorRepository.delete(jogador);
    }

    private String normalizarEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
