package deu_branco_api.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pergunta")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Questao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pergunta", unique = true)
    private Long id;

    @NotBlank
    @Column(name = "enunciado", columnDefinition = "TEXT", nullable = false)
    private String enunciado;

    @NotBlank
    @Column(name = "opcao_a", columnDefinition = "TEXT", nullable = false)
    private String opcaoA;

    @NotBlank
    @Column(name = "opcao_b", columnDefinition = "TEXT", nullable = false)
    private String opcaoB;

    @NotBlank
    @Column(name = "opcao_c", columnDefinition = "TEXT", nullable = false)
    private String opcaoC;

    @NotBlank
    @Column(name = "opcao_d", columnDefinition = "TEXT", nullable = false)
    private String opcaoD;

    @NotBlank
    @Column(name = "opcao_e", columnDefinition = "TEXT", nullable = false)
    private String opcaoE;

    @NotBlank
    @Size(min = 1, max = 1)
    @Column(name = "alternativa_correta", length = 1, nullable = false)
    private String alternativaCorreta;

    @Enumerated(EnumType.STRING)
    @Column(name = "disciplina", length = 100, nullable = false)
    private Disciplina disciplina;

    @Enumerated(EnumType.STRING)
    @Column(name = "instituicao", length = 150, nullable = false)
    private Instituicao instituicao;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "imagens", columnDefinition = "jsonb", nullable = false)
    private List<String> imagens = new ArrayList<>();

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
}
