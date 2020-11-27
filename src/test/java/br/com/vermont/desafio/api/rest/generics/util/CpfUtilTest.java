package br.com.vermont.desafio.api.rest.generics.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
public class CpfUtilTest {

    @Before
    public void setUp() {
    }


    @Test
    public void deveTestarDatas() {
        LocalDate dataVencimento = LocalDate.now();
        LocalDate secondDate = LocalDate.of(2020, 11, 24);
        boolean vencida = dataVencimento.isAfter(secondDate);
        System.out.println(vencida);
    }

    @Test
    public void deveGerarUmaListaCpfsValidosSemFormatacao() {
        System.out.println("#TEST: deveGerarUmaListaCpfsValidosSemFormatacao: ");

        // -- 01_Cenário
        int totalAGerar = 1;
        List<String> cpfList = new ArrayList<>(totalAGerar);

        // -- 02_Ação
        for (int i = 0; i < totalAGerar; i++) {
            String cpf = GeraCpfUtil.cpf(false);
            cpfList.add(i, cpf);
            System.out.println("CPF: ".concat(cpf));
        }

        // -- 03_Verificacao-Validacao
        assertEquals(totalAGerar, cpfList.size());
        System.out.println("-------------------------------------------------------------");
    }

    @Test
    public void deveValidarCpfEstahValidoERetornarVerdadeSemFormatacao() {
        System.out.println("#TEST: deveValidarCpfEstahValidoERetornarVerdadeSemFormatacao: ");

        // -- 01_Cenário
        String cpf = GeraCpfUtil.cpf(false);
        System.out.println("CPF: ".concat(cpf));

        // -- 02_Ação && -- 03_Verificacao-Validacao
        assertTrue(CpfUtil.isCPFValido(cpf));
        System.out.println("-------------------------------------------------------------");
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
