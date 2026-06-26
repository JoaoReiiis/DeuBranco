package deu_branco_api.config;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

import deu_branco_api.model.Jogador;
import deu_branco_api.repository.JogadorRepository;
import deu_branco_api.repository.ParticipacaoRepository;

@Component
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private static final Pattern TOPICO_PARTIDA = Pattern.compile("^/topic/partidas/(\\d+)/.*$");

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final JogadorRepository jogadorRepository;
    private final ParticipacaoRepository participacaoRepository;

    public WebSocketAuthChannelInterceptor(
            JwtDecoder jwtDecoder,
            JwtAuthenticationConverter jwtAuthenticationConverter,
            JogadorRepository jogadorRepository,
            ParticipacaoRepository participacaoRepository) {
        this.jwtDecoder = jwtDecoder;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
        this.jogadorRepository = jogadorRepository;
        this.participacaoRepository = participacaoRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        if (accessor.getCommand() == StompCommand.SUBSCRIBE) {
            garantirAssinaturaAutorizada(accessor);
            return message;
        }

        if (accessor.getCommand() != StompCommand.CONNECT) {
            return message;
        }

        String token = extrairToken(accessor);
        Jwt jwt = jwtDecoder.decode(token);
        accessor.setUser(jwtAuthenticationConverter.convert(jwt));

        return message;
    }

    private void garantirAssinaturaAutorizada(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();

        if (destination == null) {
            return;
        }

        Matcher matcher = TOPICO_PARTIDA.matcher(destination);

        if (!matcher.matches()) {
            return;
        }

        if (accessor.getUser() == null || accessor.getUser().getName() == null) {
            throw new AccessDeniedException("Usuario autenticado e obrigatorio para assinar topicos de partida.");
        }

        Long partidaId = Long.valueOf(matcher.group(1));
        String email = accessor.getUser().getName().trim().toLowerCase();
        Jogador jogador = jogadorRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Jogador autenticado nao encontrado."));

        if (!participacaoRepository.existsByPartidaIdAndJogadorId(partidaId, jogador.getId())) {
            throw new AccessDeniedException("Jogador nao participa desta partida.");
        }
    }

    private String extrairToken(StompHeaderAccessor accessor) {
        String authorization = primeiroHeader(accessor, "Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            authorization = primeiroHeader(accessor, "authorization");
        }

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new JwtException("Token JWT ausente no CONNECT do WebSocket.");
        }

        return authorization.substring("Bearer ".length()).trim();
    }

    private String primeiroHeader(StompHeaderAccessor accessor, String nome) {
        List<String> valores = accessor.getNativeHeader(nome);

        if (valores == null || valores.isEmpty()) {
            return null;
        }

        return valores.getFirst();
    }
}
