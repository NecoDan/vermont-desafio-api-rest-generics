package br.com.vermont.desafio.api.rest.generics.util;

import java.util.Random;

public final class RandomicoUtil {

    private static final int LIMITE_MAX_RANDOMICO_INTEIRO = 500000;

    private RandomicoUtil() {
    }

    private static Random getRandom() {
        return new Random();
    }

    public static int gerarValorRandomico() {
        int min = 1;
        return min + getRandom().nextInt(LIMITE_MAX_RANDOMICO_INTEIRO);
    }

    public static Long gerarValorRandomicoLong() {
        return (long) gerarValorRandomico();
    }
}
