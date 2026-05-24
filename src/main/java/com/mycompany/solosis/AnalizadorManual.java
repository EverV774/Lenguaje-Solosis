/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.solosis;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analizador léxico manual del lenguaje Solosis
 */
public class AnalizadorManual {

    // =========================================================
    // TIPOS DE TOKEN
    // =========================================================
    public enum TipoToken {

        GABITE,
        ESPEON,
        FALINK,
        MEOWL,
        SPINDA,
        LIZARD,
        PURPLE_LIZARD,

        IDENTIFICADOR,
        ENTERO,
        DECIMAL,
        STRING,

        ASIGNACION,
        PUNTO_COMA,

        OPERADOR_SUMA,
        OPERADOR_RESTA,
        OPERADOR_MULT,
        OPERADOR_DIV,

        PARENTESIS_IZQ,
        PARENTESIS_DER,

        LLAVE_IZQ,
        LLAVE_DER,

        MAYOR,
        MENOR,
        MAYOR_IGUAL,
        MENOR_IGUAL,
        IGUAL_IGUAL,
        DIFERENTE,

        COMENTARIO,
        DESCONOCIDO
    }

    // =========================================================
    // TOKEN
    // =========================================================
    public static class Token {

        public final TipoToken tipo;
        public final String lexema;
        public final String patron;
        public final int linea;
        public final int columna;

        public Token(
                TipoToken tipo,
                String lexema,
                String patron,
                int linea,
                int columna
        ) {

            this.tipo = tipo;
            this.lexema = lexema;
            this.patron = patron;
            this.linea = linea;
            this.columna = columna;
        }

        public String getValor() {
            return lexema;
        }

        @Override
        public String toString() {

            return tipo
                    + " | "
                    + lexema
                    + " | "
                    + patron;
        }
    }

    // =========================================================
    // PATRONES
    // =========================================================
    private static final String[][] PATRONES = {

        // PALABRAS RESERVADAS
        {"GABITE", "gabite(?![a-zA-Z0-9_])"},
        {"ESPEON", "espeon(?![a-zA-Z0-9_])"},
        {"FALINK", "falink(?![a-zA-Z0-9_])"},
        {"MEOWL", "meowl(?![a-zA-Z0-9_])"},
        {"SPINDA", "spinda(?![a-zA-Z0-9_])"},
        {"LIZARD", "LIZARD(?![a-zA-Z0-9_])"},
        {"PURPLE_LIZARD", "purple_lizard(?![a-zA-Z0-9_])"},

        // LITERALES
        {"STRING", "\"[^\"]*\"|'[^']*'"},
        {"DECIMAL", "[0-9]{1,10}\\.[0-9]+"},

        // ENTEROS DE 1 A 10 DÍGITOS
        {"ENTERO", "[0-9]{1,10}"},

        // OPERADORES COMPUESTOS
        {"MAYOR_IGUAL", ">="},
        {"MENOR_IGUAL", "<="},
        {"IGUAL_IGUAL", "=="},
        {"DIFERENTE", "!="},

        // OPERADORES
        {"ASIGNACION", "\\?"},
        {"OPERADOR_SUMA", "\\+"},
        {"OPERADOR_RESTA", "-"},
        {"OPERADOR_MULT", "\\*"},
        {"OPERADOR_DIV", "/"},

        // SIGNOS
        {"PARENTESIS_IZQ", "\\("},
        {"PARENTESIS_DER", "\\)"},
        {"LLAVE_IZQ", "\\{"},
        {"LLAVE_DER", "\\}"},

        {"MAYOR", ">"},
        {"MENOR", "<"},

        {"PUNTO_COMA", ";"},

        // IDENTIFICADORES
        {"IDENTIFICADOR", "[a-zA-Z_][a-zA-Z0-9_]*"}
    };

    // =========================================================
    // REGEX GENERAL
    // =========================================================
    private static final Pattern PATRON_TOKENS;

    static {

        StringBuilder sb = new StringBuilder();

        for (String[] p : PATRONES) {

            if (sb.length() > 0) {
                sb.append("|");
            }

            sb.append("(")
                    .append(p[1])
                    .append(")");
        }

        PATRON_TOKENS = Pattern.compile(sb.toString());
    }

    // =========================================================
    // PATRÓN INVÁLIDO
    // =========================================================
    private static final Pattern PATRON_INVALIDO =
            Pattern.compile(
                    "[^\\w\\s\"'\\?\\+\\-\\*/;#\\.\\(\\)\\{\\}<>!=]"
            );

    // =========================================================
    // ESCANEAR
    // =========================================================
    public List<Token> escanear(String codigo)
            throws ExcepcionLexica,
                   ExcepcionLimite {

        List<Token> tokens = new ArrayList<>();

        String[] lineas = codigo.split("\n", -1);

        int contadorDeclaraciones = 0;

        for (int i = 0; i < lineas.length; i++) {

            String lineaOriginal = lineas[i];

            // =============================================
            // ELIMINAR COMENTARIOS
            // =============================================
            String linea = lineaOriginal;

            int comentario = linea.indexOf("#");

            if (comentario >= 0) {

                linea = linea.substring(0, comentario);
            }

            // =============================================
            // VALIDAR CARACTERES INVÁLIDOS
            // =============================================
            Matcher invalido =
                    PATRON_INVALIDO.matcher(linea);

            if (invalido.find()) {

                char simbolo =
                        invalido.group().charAt(0);

                int columna =
                        invalido.start() + 1;

                throw new ExcepcionLexica(
                        simbolo,
                        i + 1,
                        columna
                );
            }

            // =============================================
            // TOKENIZAR
            // =============================================
            Matcher matcher =
                    PATRON_TOKENS.matcher(linea);

            while (matcher.find()) {

                String lexema =
                        matcher.group();

                int columna =
                        matcher.start() + 1;

                TipoToken tipo =
                        resolverTipo(lexema);

                String patron =
                        resolverPatron(lexema);

                // =========================================
                // LÍMITE DE DECLARACIONES
                // =========================================
                if (tipo == TipoToken.GABITE
                        || tipo == TipoToken.ESPEON
                        || tipo == TipoToken.FALINK) {

                    contadorDeclaraciones++;

                    if (contadorDeclaraciones
                            > ExcepcionLimite.LIMITE_MAX) {

                        throw new ExcepcionLimite(
                                i + 1
                        );
                    }
                }

                tokens.add(
                        new Token(
                                tipo,
                                lexema,
                                patron,
                                i + 1,
                                columna
                        )
                );
            }
        }

        return tokens;
    }

    // =========================================================
    // RESOLVER TIPO
    // =========================================================
    private TipoToken resolverTipo(String lexema) {

        switch (lexema) {

            case "gabite":
                return TipoToken.GABITE;

            case "espeon":
                return TipoToken.ESPEON;

            case "falink":
                return TipoToken.FALINK;

            case "meowl":
                return TipoToken.MEOWL;

            case "spinda":
                return TipoToken.SPINDA;

            case "LIZARD":
                return TipoToken.LIZARD;

            case "purple_lizard":
                return TipoToken.PURPLE_LIZARD;

            // OPERADORES
            case "?":
                return TipoToken.ASIGNACION;

            case "+":
                return TipoToken.OPERADOR_SUMA;

            case "-":
                return TipoToken.OPERADOR_RESTA;

            case "*":
                return TipoToken.OPERADOR_MULT;

            case "/":
                return TipoToken.OPERADOR_DIV;

            // SIGNOS
            case "(":
                return TipoToken.PARENTESIS_IZQ;

            case ")":
                return TipoToken.PARENTESIS_DER;

            case "{":
                return TipoToken.LLAVE_IZQ;

            case "}":
                return TipoToken.LLAVE_DER;

            case ";":
                return TipoToken.PUNTO_COMA;

            // COMPARADORES
            case ">":
                return TipoToken.MAYOR;

            case "<":
                return TipoToken.MENOR;

            case ">=":
                return TipoToken.MAYOR_IGUAL;

            case "<=":
                return TipoToken.MENOR_IGUAL;

            case "==":
                return TipoToken.IGUAL_IGUAL;

            case "!=":
                return TipoToken.DIFERENTE;
        }

        // =============================================
        // NÚMEROS
        // =============================================
        if (lexema.matches("[0-9]{1,10}\\.[0-9]+")) {

            return TipoToken.DECIMAL;
        }

        if (lexema.matches("[0-9]{1,10}")) {

            return TipoToken.ENTERO;
        }

        // =============================================
        // STRING
        // =============================================
        if (lexema.startsWith("\"")
                || lexema.startsWith("'")) {

            return TipoToken.STRING;
        }

        // =============================================
        // IDENTIFICADOR
        // =============================================
        if (lexema.matches(
                "[a-zA-Z_][a-zA-Z0-9_]*"
        )) {

            return TipoToken.IDENTIFICADOR;
        }

        return TipoToken.DESCONOCIDO;
    }

    // =========================================================
    // RESOLVER PATRÓN
    // =========================================================
    private String resolverPatron(String lexema) {

        // =============================================
        // DEVOLVER SÍMBOLOS DIRECTAMENTE
        // =============================================
        switch (lexema) {

            case "(":
            case ")":
            case "{":
            case "}":
            case ";":
            case "+":
            case "-":
            case "*":
            case "/":
            case "?":
            case ">":
            case "<":
            case ">=":
            case "<=":
            case "==":
            case "!=":
                return lexema;
        }

        // =============================================
        // ENTERO
        // =============================================
        if (lexema.matches("[0-9]{1,10}")) {

            return "[0-9]{1,10}";
        }

        // =============================================
        // DECIMAL
        // =============================================
        if (lexema.matches("[0-9]{1,10}\\.[0-9]+")) {

            return "[0-9]{1,10}\\.[0-9]+";
        }

        // =============================================
        // STRING
        // =============================================
        if (lexema.startsWith("\"")
                || lexema.startsWith("'")) {

            return "\"[^\"]*\"";
        }

        // =============================================
        // PALABRAS RESERVADAS
        // =============================================
        if (lexema.equals("gabite")
                || lexema.equals("espeon")
                || lexema.equals("falink")
                || lexema.equals("meowl")
                || lexema.equals("spinda")
                || lexema.equals("LIZARD")
                || lexema.equals("purple_lizard")) {

            return lexema;
        }

        // =============================================
        // IDENTIFICADOR
        // =============================================
        if (lexema.matches(
                "[a-zA-Z_][a-zA-Z0-9_]*"
        )) {

            return "[a-zA-Z_][a-zA-Z0-9_]*";
        }

        return "DESCONOCIDO";
    }
}