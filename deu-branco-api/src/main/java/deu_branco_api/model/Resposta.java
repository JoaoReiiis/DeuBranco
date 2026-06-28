package deu_branco_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "resposta",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_resposta_participacao_partida_questao",
                        columnNames = { "id_participacao", "id_partida_questao" })
        })
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resposta", unique = true)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_participacao", nullable = false)
    private Participacao participacao;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_partida_questao", nullable = false)
    private PartidaQuestao partidaQuestao;

    @Size(max = 1)
    @Column(name = "alternativa_marcada", length = 1)
    private String alternativaMarcada;

    @NotNull
    @PositiveOrZero
    @Column(name = "tempo_resposta", nullable = false)
    private Integer tempoResposta;

    @NotNull
    @Column(name = "acertou", nullable = false)
    private Boolean acertou = false;
}
