package br.com.vermont.desafio.api.rest.generics.pessoa.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public enum TipoSexo {

    F("F", "1 - FEMININO"),

    M("M", "2 - MASCULINO");

    private static final Map<String, TipoSexo> lookup;

    static {
        lookup = new HashMap<>();
        EnumSet<TipoSexo> enumSet = EnumSet.allOf(TipoSexo.class);

        for (TipoSexo type : enumSet)
            lookup.put(type.codigo, type);
    }

    private String codigo;
    private String descricao;

    TipoSexo(String codigo, String descricao) {
        inicialize(codigo, descricao);
    }

    private void inicialize(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static TipoSexo fromCodigo(String codigo) {
        if (lookup.containsKey(codigo))
            return lookup.get(codigo);
        throw new IllegalArgumentException("Código do Tipo Sexo inválido: " + codigo);
    }

    public static TipoSexo of(String codigo) {
        return Stream.of(TipoSexo.values()).filter(p -> p.getCodigo().equals(codigo)).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public String getCodigo() {
        return this.codigo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public boolean isFeminino() {
        return Objects.equals(this, F);
    }

    public boolean isMasculino() {
        return Objects.equals(this, M);
    }
}
