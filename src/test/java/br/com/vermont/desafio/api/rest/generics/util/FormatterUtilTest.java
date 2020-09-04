package br.com.vermont.desafio.api.rest.generics.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FormatterUtilTest {

    @Before
    public void setUp() {
    }

    @Test
    public void deveRetirarCaracteresNaoNumericosDeUmaString() {
        System.out.println("#TEST: deveRetirarCaracteresNaoNumericosDeUmaString: ");

        // -- 01_Cenário
        String texto = GeraCpfUtil.cpf(true);
        System.out.println("String com caracters: ".concat(texto));

        // -- 02_Ação
        String resultSemCaracters = FormatterUtil.retirarCaracteresNaoNumericos(texto);

        // -- 03_Verificacao-Validacao
        assertTrue(!resultSemCaracters.contains(".") || !resultSemCaracters.contains("-"));

        System.out.println("String sem caracters: ".concat(resultSemCaracters));
        System.out.println("-------------------------------------------------------------");
    }
}
