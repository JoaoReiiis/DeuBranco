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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "participacao",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_participacao_partida_jogador",
                        columnNames = { "id_partida", "id_jogador" })
        })
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Participacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_participacao", unique = true)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_partida", nullable = false)
    private Partida partida;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_jogador", referencedColumnName = "id", nullable = false)
    private Jogador jogador;

    @NotNull
    @PositiveOrZero
    @Column(name = "score_partida", nullable = false)
    private Integer scorePartida = 0;
}
