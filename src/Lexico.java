import java.awt.font.TextHitInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Token.Tipo;

public class Lexico {
    private final String codigo;
    private int posicion = 0;

    private static final Set<String> PALABRAS_RESERVADAS = Set.of(
            "int",
            "float",
            "char",
            "void",
            "if",
            "else",
            "while",
            "for",
            "return"
    );

    public Lexico(String codigo) {
        this.codigo = codigo;
    }

    public List<Token> analizar() {

        List<Token> tokens = new ArrayList<>();

        while (!finDelCodigo()) {
            tokens.add(escanearToken());
        }

        tokens.add(
                new Token(
                        Token.Tipo.FIN_ARCHIVO,
                        ""
                )
        );

        return tokens;
    }

    /*
    * FUNCIONES PENDIENTES (EN ESE ORDEN)
    *
    *   finDelCodigo()
        verSiguiente()
        verDespues()
        avanzar()
        coincide()
*
*
*       omitirEspacios()
*
*
*       identificador()
        numero()
*
*
*       operador()
        delimitador()
*
*
*       escanearToken()
*
*
*       cadena()
        caracter()


    * */

    private boolean finDelCodigo (){
            return posicion >= codigo.length();
        }



    private Token escanearToken() {

        omitirEspacios();

        if (finDelCodigo()) {
            return new Token(Token.Tipo.FIN_ARCHIVO, "");
        }

        char actual = verSiguiente();

        // Identificadores y palabras reservadas
        if (Character.isLetter(actual) || actual == '_') {
            return identificador();
        }

        // Números enteros o decimales
        if (Character.isDigit(actual)) {
            return numero();
        }

        // Cadenas
        if (actual == '"') {
            return cadena();
        }

        // Caracteres
        if (actual == '\'') {
            return caracter();
        }

        // Operadores
        if ("+-*/%=<>!&|".indexOf(actual) != -1) {
            return operador();
        }

        // Delimitadores
        if ("(){}[];,.".indexOf(actual) != -1) {
            return delimitador();
        }

        // Carácter desconocido
        avanzar();

        return new Token(
                Token.Tipo.DESCONOCIDO,
                String.valueOf(actual)
        );
    }


    private char avanzar() {
       char actual = verSiguiente();
       posicion = posicion + 1;
       return actual;
    }

    private boolean coincide(char esperado){
        if(finDelCodigo()){
            return false;
        }else if(verSiguiente() !=esperado){
            return false;
        }else{
            avanzar();
            return true;
        }
    }


    private Token delimitador() {
        char c=avanzar();
        return  new Token(Token.Tipo.DELIMITADOR, String.valueOf(c));
    }



    private Token operador() {
        int inicio = posicion;
        char c = avanzar();
        if(c == 'c' && coincide('=')){
            return new Token(Token.Tipo.OPERADOR, "==");
        }else if(c == '!'&& coincide('!')){
            return new Token(Token.Tipo.OPERADOR, "!=");
        }else if(c == '<'&& coincide('!')){
            return new Token(Token.Tipo.OPERADOR, "<=");
        }else if(c == '>'&& coincide('!')){
            return new Token(Token.Tipo.OPERADOR, ">=");
        }else if(c == '&'&& coincide('!')){
            return new Token(Token.Tipo.OPERADOR, "&&");
        }else if(c == '|'&& coincide('!')){
            return new Token(Token.Tipo.OPERADOR, "||");
        }else{
            String lexema = codigo.substring(inicio, posicion);
            return new Token(Token.Tipo.OPERADOR, lexema);     }

    }

    private Token caracter() {
    }
/*FUNCIÓN cadena():
    avanzar()                                            // consume la comilla de APERTURA (no la incluyas en el lexema)
    inicio = posicion

    MIENTRAS NO finDelCodigo() Y verSiguiente() NO ES '"' HACER
        avanzar()

    lexema = codigo.substring(inicio, posicion)          // el contenido, SIN las comillas

    SI finDelCodigo() ENTONCES
        // manejar error: cadena sin cerrar (nunca encontró la comilla de cierre)
    SI NO
        avanzar()                                        // consume la comilla de CIERRE

    DEVOLVER new Token(Token.Tipo.CADENA, lexema) */
    private Token cadena() {
        avanzar();
        int inicio = posicion;
        while(!finDelCodigo() && verSiguiente()!='"'){
            avanzar();
        }
        String lexema = codigo.substring(inicio, posicion);
        if(finDelCodigo()){
            System.out.println("Error: La cadena no fue cerrada");
        }else{
            avanzar();
        }
        return new Token(Token.Tipo.CADENA, lexema);
        
    }

    private Token numero() {
    }

    private Token identificador() {
    }

    private char verSiguiente() {
    }


/* Necesito ver el carácter que viene después del actual, sin moverme del sitio. */
    private char verDespues(){
        if(posicion+1 >= codigo.length()){
            return '\0';
        }
        return codigo.charAt(posicion+1);
    }


    private void omitirEspacios() {
        while(!finDelCodigo() && (verSiguiente()=='\t' || verSiguiente()=='\n' || verSiguiente()== '\r')){
            avanzar();
        }
    }
}