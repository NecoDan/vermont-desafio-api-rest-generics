package br.com.vermont.desafio.api.rest.generics.model.perfil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Perfil {

    private String descricao;
    private String token;

    public static List<Perfil> getListAll() {
        return Arrays.asList(
                criarPerfilAdvogadoCadastroTrabalhista(),
                criarPerfilAdvogadoInternoTrabalhista(),
                criarPerfilAnalitaApuracaoTrabalhista(),
                criarPerfilSupervisorApuracaoTrabalhista(),
                criarPerfilAdvogadoEscritorioPatrono()
        );
    }

    public static String tokenPerfilAdvogadoCadastroTrabalhista() {
        return "03624e47-61d3-44ff-960f-c6b3e8a06cef";
    }

    public static String tokenPerfilAdvogadoInternoTrabalhista() {
        return "3f9551e2-8b61-41f4-8b46-6ac604f9491a";
    }

    public static Perfil criarPerfilAdvogadoCadastroTrabalhista() {
        return Perfil.builder().token(tokenPerfilAdvogadoCadastroTrabalhista()).descricao("Advogado de Cadastro Trabalhista").build();
    }

    public static Perfil criarPerfilAdvogadoInternoTrabalhista() {
        return Perfil.builder().token(tokenPerfilAdvogadoInternoTrabalhista()).descricao("Advogado Interno Trabalhista").build();
    }

    public static Perfil criarPerfilAnalitaApuracaoTrabalhista() {
        String token = "b0b58295-90fe-4341-b7f5-262977132cde";
        return Perfil.builder().token(token).descricao("Analista de Apuração Trabalhista").build();
    }

    public static Perfil criarPerfilSupervisorApuracaoTrabalhista() {
        String token = "ecfd5f65-4d53-4c67-ae49-8f14542de829";
        return Perfil.builder().token(token).descricao("Supervisor de Apuração Trabalhista").build();
    }

    public static Perfil criarPerfilAdvogadoEscritorioPatrono() {
        String token = "41413793-eb51-4cf9-aec0-654bfb80a88b";
        return Perfil.builder().token(token).descricao("Advogado Escritório Patrono").build();
    }
}
