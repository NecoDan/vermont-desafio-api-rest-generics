package br.com.vermont.desafio.api.rest.generics.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDate> {

    public LocalDateTimeDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(final JsonParser parser,
                                 final DeserializationContext context) throws IOException {
        final String value = parser.getValueAsString();
        return LocalDate.parse(value, DateTimeFormatter.ofPattern("ddMMyyyy"));
    }
}
