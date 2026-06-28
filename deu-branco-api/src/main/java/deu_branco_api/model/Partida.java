package deu_branco_api.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "partida")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partida", unique = true)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_jogador_host", referencedColumnName = "id", nullable = false)
    private Jogador host;

    @NotNull
    @Positive
    @Column(name = "tempo_de_jogo", nullable = false)
    private Integer tempoDeJogo;

    @NotBlank
    @Size(max = 10)
    @Column(name = "codigo_pin", length = 10, nullable = false)
    private String codigoPin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_sala", length = 30, nullable = false)
    private PartidaStatus statusSala = PartidaStatus.AGUARDANDO;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "iniciada_em")
    private LocalDateTime iniciadaEm;

    @Column(name = "finalizada_em")
    private LocalDateTime finalizadaEm;
}
