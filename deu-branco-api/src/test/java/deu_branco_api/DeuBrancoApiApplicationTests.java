package deu_branco_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import deu_branco_api.repository.JogadorRepository;
import deu_branco_api.repository.ParticipacaoRepository;
import deu_branco_api.repository.PartidaQuestaoRepository;
import deu_branco_api.repository.PartidaRepository;
import deu_branco_api.repository.QuestaoRepository;
import deu_branco_api.repository.RespostaRepository;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude="
				+ "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,"
				+ "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration,"
				+ "org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration,"
				+ "org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration"
})
class DeuBrancoApiApplicationTests {

	@MockitoBean
	private JogadorRepository jogadorRepository;

	@MockitoBean
	private QuestaoRepository questaoRepository;

	@MockitoBean
	private PartidaRepository partidaRepository;

	@MockitoBean
	private PartidaQuestaoRepository partidaQuestaoRepository;

	@MockitoBean
	private ParticipacaoRepository participacaoRepository;

	@MockitoBean
	private RespostaRepository respostaRepository;

	@Test
	void deveCarregarContextoComRepositoriesMockadosSemBanco() {
	}

}
