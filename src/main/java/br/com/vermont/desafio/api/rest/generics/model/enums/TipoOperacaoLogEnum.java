package br.com.vermont.desafio.api.rest.generics.model.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public enum TipoOperacaoLogEnum {

    CREATE(1, "C", "1 - CREATE"),

    UPDATE(2, "U", "2 - UPDATE"),

    DELETE(3, "D", "3 - DELETE");

    private static final Map<Integer, TipoOperacaoLogEnum> lookup;

    static {
        lookup = new HashMap<>();
        EnumSet<TipoOperacaoLogEnum> enumSet = EnumSet.allOf(TipoOperacaoLogEnum.class);

        for (TipoOperacaoLogEnum type : enumSet)
            lookup.put(type.codigo, type);
    }

    private int codigo;
    private String codigoLiteral;
    private String descricao;

    TipoOperacaoLogEnum(int codigo, String codigoLiteral, String descricao) {
        inicialize(codigo, codigoLiteral, descricao);
    }

    private void inicialize(int codigo, String codigoLiteral, String descricao) {
        this.codigo = codigo;
        this.codigoLiteral = codigoLiteral;
        this.descricao = descricao;
    }

    public static TipoOperacaoLogEnum fromCodigo(int codigo) {
        if (lookup.containsKey(codigo))
            return lookup.get(codigo);
        throw new IllegalArgumentException(String.format("Código do Tipo de Operação Log inválido: %d", codigo));
    }

    public static TipoOperacaoLogEnum of(int codigo) {
        return Stream.of(TipoOperacaoLogEnum.values()).filter(p -> p.getCodigo() == codigo).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public int getCodigo() {
        return this.codigo;
    }

    public String getCodigoLiteral() {
        return this.codigoLiteral;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public boolean isCreate() {
        return Objects.equals(this, CREATE);
    }

    public boolean isUpdate() {
        return Objects.equals(this, UPDATE);
    }

    public boolean isDelete() {
        return Objects.equals(this, DELETE);
    }
}
