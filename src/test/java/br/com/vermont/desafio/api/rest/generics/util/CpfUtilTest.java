package br.com.vermont.desafio.api.rest.generics.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CpfUtilTest {

    @Before
    public void setUp() {
    }

    @Test
    public void deveValidarCpfEstahValidoERetornarVerdade() {
        System.out.println("#TEST: deveValidarCpfEstahValidoERetornarVerdade: ");

        // -- 01_Cenário
        String cpf = GeraCpfUtil.cpf(true);
        System.out.println("CPF: ".concat(cpf));

        // -- 02_Ação && -- 03_Verificacao-Validacao
        assertTrue(CpfUtil.isCPFValido(cpf));
        System.out.println("-------------------------------------------------------------");
    }

    @Test
    public void deveValidarCpfEstahValidoERetornarFalso() {
        System.out.println("#TEST: deveValidarCpfEstahValidoERetornarFalso: ");

        // -- 01_Cenário
        String cpf = String.valueOf(RandomicoUtil.gerarValorRandomicoLong());
        System.out.println("CPF: ".concat(cpf));

        // -- 02_Ação && -- 03_Verificacao-Validacao
        assertFalse(CpfUtil.isCPFValido(cpf));
        System.out.println("-------------------------------------------------------------");
    }
}
