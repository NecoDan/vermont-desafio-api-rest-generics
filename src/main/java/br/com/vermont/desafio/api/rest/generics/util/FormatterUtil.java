package br.com.vermont.desafio.api.rest.generics.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class FormatterUtil {

    private static final String MENSAGEM_VALIDACAO = "Parâmetro {DATA}, encontra-se inválida e/ou inexistente.";

    private FormatterUtil() {
    }

    public static String retirarCaracteresNaoNumericos(String value) {
        return (Objects.isNull(value) || value.isEmpty()) ? "" : value.replaceAll("[^\\d]", "");
    }

    public static String toStringLocalDateFormatadaPor(LocalDate data, String strFormato) {
        if (Objects.isNull(data))
            throw new IllegalArgumentException(MENSAGEM_VALIDACAO);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(strFormato);
        return data.format(formatter);
    }

    public static String toStringLocalDateFormatada(LocalDate data) {
        return toStringLocalDateFormatadaPor(data, "dd/MM/yyyy");
    }

    public static String toStringLocalDateTimeFormatada(LocalDateTime data) {
        return toStringLocalDateTimeFormatadaPor(data, "dd/MM/yyyy HH:mm:ss");
    }

    public static String toStringLocalDateTimeFormatadaPor(LocalDateTime data, String strFormato) {
        if (Objects.isNull(data))
            throw new IllegalArgumentException(MENSAGEM_VALIDACAO);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(strFormato);
        return data.format(formatter);
    }

    public static String formatConteudoJSONFrom(String conteudo) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(conteudo);
        return gson.toJson(je);
    }
}
