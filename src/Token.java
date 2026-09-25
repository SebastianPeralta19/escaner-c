public class Token {
    public enum Tipo {
        PALABRA_RESERVADA,
        IDENTIFICADOR,
        ENTERO,
        DECIMAL,
        OPERADOR,
        DELIMITADOR,
        CADENA,
        CARACTER,
        COMENTARIO,
        DESCONOCIDO,
        FIN_ARCHIVO
    }

    private final Tipo tipo;
    private final String lexema;

    public Token(Tipo tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getLexema() {
        return lexema;
    }

    @Override
    public String toString() {
        return tipo + " -> " + lexema;
    }
}
