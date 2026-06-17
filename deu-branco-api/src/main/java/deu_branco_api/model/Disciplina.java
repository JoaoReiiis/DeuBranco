package deu_branco_api.model;

import java.util.Arrays;

public enum Disciplina {
    PORTUGUES,
    INGLES,
    ESPANHOL,
    MATEMATICA,
    FISICA,
    QUIMICA,
    BIOLOGIA,
    HISTORIA,
    GEOGRAFIA,
    FILOSOFIA,
    SOCIOLOGIA,
    ARTES,
    EDUCACAO_FISICA,
    REDACAO;

    public static Disciplina from(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Disciplina da pergunta e obrigatoria.");
        }

        try {
            return valueOf(valor.trim());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Disciplina invalida. Valores aceitos: " + valoresAceitos());
        }
    }

    private static String valoresAceitos() {
        return String.join(", ", Arrays.stream(values()).map(Enum::name).toList());
    }
}
