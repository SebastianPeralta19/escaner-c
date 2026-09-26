import java.awt.font.TextHitInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;



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
    *   finDelCodigo() hecho por sebastian
        verSiguiente()
        verDespues() hecho por sarai
        avanzar()  hecho por sarai
        coincide()
*
*
*       omitirEspacios() hecho por sarai
*
*
*       identificador() hecho por sebastian
        numero() hecho por sebastian
*
*
*       operador()
        delimitador()
*
*
*       escanearToken() hecho por sebastian
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
/*FUNCIÓN caracter():
    avanzar()                                            // consume la comilla simple de apertura
    inicio = posicion

    MIENTRAS NO finDelCodigo() Y verSiguiente() NO ES '\'' HACER
        avanzar()

    lexema = codigo.substring(inicio, posicion)

    SI finDelCodigo() ENTONCES
        // manejar error: carácter sin cerrar
    SI NO
        avanzar()                                        // consume la comilla simple de cierre

    DEVOLVER new Token(Token.Tipo.CARACTER, lexema) */
    private Token caracter() {
        avanzar();
        int inicio = posicion;
        while (!finDelCodigo() && verSiguiente() != '\'') {
            avanzar();
        }
        String lexema = codigo.substring(inicio, posicion);
        if(finDelCodigo()){
            System.out.println("Error: caracter sin cerrar");
        }else{
            avanzar()
            return new Token(Token.Tipo.CARACTER,lexema);
        }
    }

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
        int inicio = posicion;

        while(Character.isDigit(verSiguiente())){
        avanzar();
        }

        boolean decimal = false;
        if(verSiguiente() == '.' && Character.isDigit(verDespues())){
            decimal = true;
            avanzar();

            while (Character.isDigit(verSiguiente())){
                avanzar();
            }

            String lexema = codigo.substring(inicio, posicion);

            if(decimal){
                return new Token(Token.Tipo.DECIMAL, lexema);
            }

            return new Token(Token.Tipo.ENTERO, lexema);
        }
    }

    private Token identificador() {
        int inicio = posicion;

        while(Character.isLetterOrDigit(verSiguiente()) || verSiguiente() == '_'){
            avanzar();
        }

        String lexema = codigo.substring(inicio,posicion);

        if(PALABRAS_RESERVADAS.contains(lexema)){
            return new Token(Token.Tipo.PALABRA_RESERVADA, lexema);
        }

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