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
 * Analizador léxico manual del lenguaje Solosis.
 *
 * Mejoras respecto a la versión anterior:
 *  - Cada Token registra su LEXEMA (texto original) y el PATRÓN que lo reconoció.
 *  - Se detectan símbolos no reconocidos y se lanza ExcepcionLexica.
 *  - Se aplica el límite ExcepcionLimite.LIMITE_MAX de declaraciones por programa.
 */
public class AnalizadorManual {

    // ------------------------------------------------------------------ //
    //  Tipos de token
    // ------------------------------------------------------------------ //
    public enum TipoToken {
    GABITE, ESPEON, FALINK, MEOWL, SPINDA,
    IDENTIFICADOR, ENTERO, DECIMAL, STRING,
    ASIGNACION, PUNTO_COMA,
    OPERADOR_SUMA, OPERADOR_RESTA, OPERADOR_MULT, OPERADOR_DIV,
    PARENTESIS_IZQ, PARENTESIS_DER,
    LLAVE_IZQ, LLAVE_DER,
    MAYOR, MENOR, MAYOR_IGUAL, MENOR_IGUAL,
    IGUAL_IGUAL, DIFERENTE,
    COMENTARIO, DESCONOCIDO
}

    // ------------------------------------------------------------------ //
    //  Clase Token: ahora incluye lexema y patron
    // ------------------------------------------------------------------ //
    public static class Token {
        public final TipoToken tipo;
        /** Texto exacto tal como aparece en el código fuente */
        public final String lexema;
        /** Nombre del patrón/regex que reconoció este token */
        public final String patron;
        public final int linea;
        public final int columna;

        /** Constructor legacy para compatibilidad */
        public Token(TipoToken tipo, String valor) {
            this(tipo, valor, "PATRON_MANUAL", 0, 0);
        }

        public Token(TipoToken tipo, String lexema, String patron, int linea, int columna) {
            this.tipo    = tipo;
            this.lexema  = lexema;
            this.patron  = patron;
            this.linea   = linea;
            this.columna = columna;
        }

        /** Alias de compatibilidad */
        /** Alias de compatibilidad */
        public String getValor() { return lexema; }

        @Override
        public String toString() {
            return tipo + "  |  lexema='" + lexema + "'  |  patron=" + patron;
        }
    }

    // ------------------------------------------------------------------ //
    //  Definición de patrones nombrados (orden = precedencia)
    // ------------------------------------------------------------------ //
    private static final String[][] PATRONES = {
    { "PALABRA_RESERVADA_GABITE",  "gabite(?![a-zA-Z0-9_])"         },
    { "PALABRA_RESERVADA_ESPEON",  "espeon(?![a-zA-Z0-9_])"         },
    { "PALABRA_RESERVADA_FALINK",  "falink(?![a-zA-Z0-9_])"         },
    { "PALABRA_RESERVADA_MEOWL",   "meowl(?![a-zA-Z0-9_])"          },
    { "PALABRA_RESERVADA_SPINDA",  "spinda(?![a-zA-Z0-9_])"         },

    { "LITERAL_STRING",            "\"[^\"]*\"|'[^']*'"             },
    { "LITERAL_DECIMAL",           "\\d+\\.\\d+"                    },
    { "LITERAL_ENTERO",            "\\d+(?![a-zA-Z_])"              },

    // Comparadores compuestos primero
    { "MAYOR_IGUAL",               ">="                             },
    { "MENOR_IGUAL",               "<="                             },
    { "IGUAL_IGUAL",               "=="                             },
    { "DIFERENTE",                 "!="                             },

    { "IDENTIFICADOR",             "[a-zA-Z_][a-zA-Z0-9_]*"         },
    { "ASIGNACION",                "\\?"                            },
    { "OPERADOR_SUMA",             "\\+"                            },
    { "OPERADOR_RESTA",            "-"                              },
    { "OPERADOR_MULT",             "\\*"                            },
    { "OPERADOR_DIV",              "/"                              },
    { "PARENTESIS_IZQ",            "\\("                            },
    { "PARENTESIS_DER",            "\\)"                            },
    { "LLAVE_IZQ",                 "\\{"                            },
    { "LLAVE_DER",                 "\\}"                            },
    { "MAYOR",                     ">"                              },
    { "MENOR",                     "<"                              },
    { "PUNTO_COMA",                ";"                              },
    };

    private static final Pattern PATRON_TOKENS;

    /** Detecta cualquier carácter que no sea parte del alfabeto válido de Solosis */
    // CORRECCIÓN: Agregada la comilla simple (\') al grupo de caracteres permitidos
    private static final Pattern PATRON_INVALIDO =
        Pattern.compile("[^\\w\\s\"'\\?\\+\\-\\*/;#\\.\\(\\)\\{\\}<>!=]");

    static {
        StringBuilder sb = new StringBuilder();
        for (String[] p : PATRONES) {
            if (sb.length() > 0) sb.append("|");
            sb.append("(").append(p[1]).append(")");
        }
        PATRON_TOKENS = Pattern.compile(sb.toString());
    }

    // ------------------------------------------------------------------ //
    //  Método principal de escaneo
    // ------------------------------------------------------------------ //
    public List<Token> escanear(String codigo) throws ExcepcionLexica, ExcepcionLimite {
        List<Token> tokens = new ArrayList<>();
        String[] lineas = codigo.split("\n", -1);
        int contadorDeclaraciones = 0;

        for (int numLinea = 0; numLinea < lineas.length; numLinea++) {
            String lineaOriginal = lineas[numLinea];

            // Eliminar comentario de línea (# ...)
            String lineaLimpia = lineaOriginal;
            int posComentario  = lineaOriginal.indexOf('#');
            if (posComentario >= 0) {
                lineaLimpia = lineaOriginal.substring(0, posComentario);
            }

            // Validar símbolos no reconocidos ANTES de tokenizar
            Matcher mInv = PATRON_INVALIDO.matcher(lineaLimpia);
            if (mInv.find()) {
                char simboloBad = mInv.group().charAt(0);
                int col = mInv.start() + 1;
                throw new ExcepcionLexica(simboloBad, numLinea + 1, col);
            }

            // Tokenizar
            Matcher m = PATRON_TOKENS.matcher(lineaLimpia);
            while (m.find()) {
                String lexema = m.group();
                int col       = m.start() + 1;
                String patron = resolverPatron(lexema);
                TipoToken tipo = resolverTipo(lexema);

                // Aplicar límite de declaraciones
                if (tipo == TipoToken.GABITE || tipo == TipoToken.ESPEON || tipo == TipoToken.FALINK) {
                    contadorDeclaraciones++;
                    if (contadorDeclaraciones > ExcepcionLimite.LIMITE_MAX) {
                        throw new ExcepcionLimite(numLinea + 1);
                    }
                }

                tokens.add(new Token(tipo, lexema, patron, numLinea + 1, col));
            }
        }

        return tokens;
    }

    // ------------------------------------------------------------------ //
    //  Helpers privados
    // ------------------------------------------------------------------ //
    private String resolverPatron(String lexema) {
        // Resolución directa por valor (compatible con patrones lookahead)
        switch (lexema) {
            case "spinda": return "PALABRA_RESERVADA_SPINDA";
            case "(":      return "PARENTESIS_IZQ";
            case ")":      return "PARENTESIS_DER";
            case "{":      return "LLAVE_IZQ";
            case "}":      return "LLAVE_DER";
            case ">":      return "MAYOR";
            case "<":      return "MENOR";
            case ">=":     return "MAYOR_IGUAL";
            case "<=":     return "MENOR_IGUAL";
            case "==":     return "IGUAL_IGUAL";
            case "!=":     return "DIFERENTE";
        }
        // CORRECCIÓN: Ahora identifica el nombre del patrón si empieza con comilla simple
        if (lexema.startsWith("\"") || lexema.startsWith("'")) return "LITERAL_STRING";
        if (lexema.matches("\\d+\\.\\d+")) return "LITERAL_DECIMAL";
        if (lexema.matches("\\d+"))          return "LITERAL_ENTERO";
        if (lexema.matches("[a-zA-Z_][a-zA-Z0-9_]*")) return "IDENTIFICADOR";
        return "DESCONOCIDO";
    }

    private TipoToken resolverTipo(String lexema) {
    switch (lexema) {
        case "gabite": return TipoToken.GABITE;
        case "espeon": return TipoToken.ESPEON;
        case "falink": return TipoToken.FALINK;
        case "meowl":  return TipoToken.MEOWL;
        case "spinda": return TipoToken.SPINDA;

        case "?":      return TipoToken.ASIGNACION;
        case "+":      return TipoToken.OPERADOR_SUMA;
        case "-":      return TipoToken.OPERADOR_RESTA;
        case "*":      return TipoToken.OPERADOR_MULT;
        case "/":      return TipoToken.OPERADOR_DIV;
        case ";":      return TipoToken.PUNTO_COMA;

        case "(":      return TipoToken.PARENTESIS_IZQ;
        case ")":      return TipoToken.PARENTESIS_DER;
        case "{":      return TipoToken.LLAVE_IZQ;
        case "}":      return TipoToken.LLAVE_DER;
        case ">":      return TipoToken.MAYOR;
        case "<":      return TipoToken.MENOR;
        case ">=":     return TipoToken.MAYOR_IGUAL;
        case "<=":     return TipoToken.MENOR_IGUAL;
        case "==":     return TipoToken.IGUAL_IGUAL;
        case "!=":     return TipoToken.DIFERENTE;

        default:
            if (lexema.startsWith("\"") || lexema.startsWith("'")) return TipoToken.STRING;
            if (lexema.matches("\\d+\\.\\d+")) return TipoToken.DECIMAL;
            if (lexema.matches("\\d+")) return TipoToken.ENTERO;
            return TipoToken.IDENTIFICADOR;
    }
}
}