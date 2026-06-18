package deu_branco_api.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import deu_branco_api.model.Jogador;
import deu_branco_api.repository.JogadorRepository;

@Service
public class JogadorDetailsService implements UserDetailsService {

    private final JogadorRepository jogadorRepository;

    public JogadorDetailsService(JogadorRepository jogadorRepository) {
        this.jogadorRepository = jogadorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        String emailNormalizado = email == null ? null : email.trim().toLowerCase();
        Jogador jogador = jogadorRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new UsernameNotFoundException("Jogador nao encontrado."));

        return User.builder()
                .username(jogador.getEmail())
                .password(jogador.getSenha())
                .authorities("ROLE_" + jogador.getRole().name())
                .build();
    }
}
