package com.mycompany.solosis;

/**
 * Se lanza cuando el analizador léxico encuentra un símbolo
 * o carácter que no pertenece al alfabeto del lenguaje Solosis.
 *
 * Ejemplos: @, %, ^, ~, !, #!
 */
public class ExcepcionLexica extends ExcepcionSolosis {
    private final char simbolo;

    public ExcepcionLexica(char simbolo, int linea, int columna) {
        super("Error Léxico: Símbolo no reconocido '" + simbolo + "'", linea, columna);
        this.simbolo = simbolo;
    }

    public char getSimbolo() { return simbolo; }
}
