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
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "partida_questao",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_partida_questao_partida_questao",
                        columnNames = { "id_partida", "id_questao" }),
                @UniqueConstraint(
                        name = "uk_partida_questao_partida_ordem",
                        columnNames = { "id_partida", "ordem" })
        })
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PartidaQuestao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partida_questao", unique = true)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_partida", nullable = false)
    private Partida partida;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_questao", referencedColumnName = "id_pergunta", nullable = false)
    private Questao questao;

    @NotNull
    @Positive
    @Column(name = "ordem", nullable = false)
    private Integer ordem;
}
