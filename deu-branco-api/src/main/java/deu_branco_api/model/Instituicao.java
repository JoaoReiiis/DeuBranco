package deu_branco_api.model;

import java.util.Arrays;

public enum Instituicao {
    ENEM,
    FUVEST,
    UNICAMP,
    UNESP,
    UERJ,
    UFPR,
    UFMG,
    ITA,
    IME,
    OUTRA;

    public static Instituicao from(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Instituicao da pergunta e obrigatoria.");
        }

        try {
            return valueOf(valor.trim());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Instituicao invalida. Valores aceitos: " + valoresAceitos());
        }
    }

    private static String valoresAceitos() {
        return String.join(", ", Arrays.stream(values()).map(Enum::name).toList());
    }
}
