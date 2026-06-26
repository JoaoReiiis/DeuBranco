package deu_branco_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI deuBrancoOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Deu Branco API")
                        .description("""
                                API do Deu Branco para jogadores, questoes e partidas multiplayer em tempo real.

                                Fluxo multiplayer REST:
                                1. Autentique em POST /auth/login e use o accessToken como Bearer token.
                                2. Crie a partida em POST /partidas. O host entra automaticamente na sala.
                                3. Outros jogadores entram por PIN em POST /partidas/entrar.
                                4. O host inicia em POST /partidas/{id}/iniciar.
                                5. Participantes buscam questoes em GET /partidas/{id}/questoes e respondem em POST /partidas/{id}/respostas.
                                6. A partida finaliza quando todos respondem, quando o host finaliza ou quando o tempo expira.
                                7. O ranking final fica em GET /partidas/{id}/leaderboard.

                                WebSocket STOMP:
                                - Handshake: ws://host/ws
                                - CONNECT deve enviar Authorization: Bearer {accessToken}
                                - Comandos: /app/partidas/criar, /app/partidas/entrar, /app/partidas/{id}/iniciar,
                                  /app/partidas/{id}/cancelar, /app/partidas/{id}/finalizar,
                                  /app/partidas/{id}/responder e /app/partidas/{id}/sincronizar.
                                - Topicos: /topic/partidas/{id}/lobby, /status, /respostas, /placar e /leaderboard.
                                - Apenas participantes podem assinar topicos de uma partida.
                                """)
                        .version("v1"))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, bearerAuthScheme()))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }

    private SecurityScheme bearerAuthScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
}
