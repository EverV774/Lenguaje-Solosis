/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.solosis;

import com.mycompany.solosis.AnalizadorManual.TipoToken;
import com.mycompany.solosis.AnalizadorManual.Token;
import java.util.List;

/**
 *
 * @author crack
 */
public class Steelix {

    public String validarLinea(String linea, int numLinea)
            throws ExcepcionSintactica, ExcepcionSemantica {

        // 1. Eliminar comentarios
        if (linea.contains("#")) {
            linea = linea.substring(0, linea.indexOf("#"));
        }

        String lineaTrim = linea.trim();

        // Línea vacía válida
        if (lineaTrim.isEmpty()) {
            return null;
        }

        // 2. Detectar estructuras especiales de spinda
        boolean esSpinda = lineaTrim.matches("^spinda\\s*\\(.+\\)\\s*\\{$");
        boolean esCierreBloque = lineaTrim.matches("^\\}$");

        // 3. Toda instrucción normal debe terminar en ';'
        // spinda(...) { y } NO deben llevar ;
        if (!esSpinda && !esCierreBloque && !lineaTrim.endsWith(";")) {
            throw new ExcepcionSintactica("Falta el signo de cierre ';' al final", numLinea);
        }

        // 4. No se permite '=' como asignación normal.
        // Pero sí se permiten comparadores ==, >=, <=, != dentro de spinda.
        if (!esSpinda && lineaTrim.contains("=")) {
            throw new ExcepcionSintactica("Operador inválido '='. Use '?' para asignación", numLinea);
        }

        if (esSpinda) {
            validarEstructuraSpinda(lineaTrim, numLinea);
            return null;
        }

        if (esCierreBloque) {
            return null;
        }

        // 5. Normalizar espacios alrededor de palabras clave para no romper el tokenizador
        lineaTrim = lineaTrim.replaceAll("(gabite|espeon|falink|meowl)([a-zA-Z0-9_?])", "$1 $2");

        // 6. Tokenizar con AnalizadorManual
        List<Token> tokens;
        try {
            AnalizadorManual analizador = new AnalizadorManual();
            tokens = analizador.escanear(lineaTrim);
        } catch (ExcepcionLexica | ExcepcionLimite e) {
            throw new ExcepcionSintactica("Error léxico: " + e.getMessage(), numLinea);
        }

        if (tokens.isEmpty()) {
            return null;
        }

        // ─────────────────────────────────────────────
        // CASO 1: Impresión flexible: <expresión> meowl ;
        // Ejemplo: "Hola"meowl;
        // Ejemplo: nombre meowl;
        // ─────────────────────────────────────────────
        int indiceMeowl = -1;

        for (int k = 0; k < tokens.size(); k++) {
            if (tokens.get(k).tipo == TipoToken.MEOWL) {
                indiceMeowl = k;
                break;
            }
        }

        if (indiceMeowl != -1) {

            if (indiceMeowl == 0) {
                throw new ExcepcionSintactica("Se esperaba una expresión o variable antes de 'meowl'", numLinea);
            }

            if (indiceMeowl + 1 >= tokens.size()
                    || tokens.get(indiceMeowl + 1).tipo != TipoToken.PUNTO_COMA) {
                throw new ExcepcionSintactica("Falta el signo de cierre ';' después de 'meowl'", numLinea);
            }

            if (indiceMeowl + 2 < tokens.size()) {
                throw new ExcepcionSintactica("No debe existir código después de ';'", numLinea);
            }

            return null;
        }

        // ─────────────────────────────────────────────
        // CASO 2: Declaración SIN asignación inicial
        // Ejemplo: edad gabite;
        // Ejemplo: nombre falink;
        // ─────────────────────────────────────────────
        if (tokens.size() == 3
                && tokens.get(0).tipo == TipoToken.IDENTIFICADOR
                && esTipoDato(tokens.get(1).tipo)
                && tokens.get(2).tipo == TipoToken.PUNTO_COMA) {
            return null;
        }

        // ─────────────────────────────────────────────
        // CASO 3: Declaración CON asignación
        // Ejemplo: edad gabite?18;
        // Ejemplo: nombre falink?"Ash";
        // ─────────────────────────────────────────────
        if (tokens.size() >= 5
                && tokens.get(0).tipo == TipoToken.IDENTIFICADOR
                && esTipoDato(tokens.get(1).tipo)
                && tokens.get(2).tipo == TipoToken.ASIGNACION
                && tokens.get(tokens.size() - 1).tipo == TipoToken.PUNTO_COMA) {

            // Puedes activar esta validación si quieres que Steelix revise tipos desde sintaxis.
            // validarTiposExpresion(tokens.get(1), tokens, 3, numLinea);

            return null;
        }

        // ─────────────────────────────────────────────
        // CASO 4: Asignación posterior
        // Ejemplo: edad?20;
        // Ejemplo: nombre?"Brock";
        // ─────────────────────────────────────────────
        if (tokens.size() >= 4
                && tokens.get(0).tipo == TipoToken.IDENTIFICADOR
                && tokens.get(1).tipo == TipoToken.ASIGNACION
                && tokens.get(tokens.size() - 1).tipo == TipoToken.PUNTO_COMA) {
            return null;
        }

        // Si no encaja con ninguna estructura válida
        throw new ExcepcionSintactica("Formato no reconocido o inválido para Solosis.", numLinea);
    }

    private boolean esTipoDato(TipoToken tipo) {
        return tipo == TipoToken.GABITE
                || tipo == TipoToken.ESPEON
                || tipo == TipoToken.FALINK;
    }

    private void validarEstructuraSpinda(String lineaTrim, int numLinea)
            throws ExcepcionSintactica {

        if (!lineaTrim.startsWith("spinda")) {
            throw new ExcepcionSintactica("La estructura condicional debe iniciar con 'spinda'", numLinea);
        }

        if (!lineaTrim.contains("(")) {
            throw new ExcepcionSintactica("Falta '(' después de spinda", numLinea);
        }

        if (!lineaTrim.contains(")")) {
            throw new ExcepcionSintactica("Falta ')' para cerrar la condición de spinda", numLinea);
        }

        if (!lineaTrim.endsWith("{")) {
            throw new ExcepcionSintactica("Falta '{' para abrir el bloque de spinda", numLinea);
        }

        int posAbre = lineaTrim.indexOf("(");
        int posCierra = lineaTrim.lastIndexOf(")");

        if (posCierra <= posAbre) {
            throw new ExcepcionSintactica("Los paréntesis de spinda están mal ordenados", numLinea);
        }

        String condicion = lineaTrim.substring(posAbre + 1, posCierra).trim();

        if (condicion.isEmpty()) {
            throw new ExcepcionSintactica("La condición de spinda está vacía", numLinea);
        }

        boolean condicionValida = condicion.matches(
                "^[a-zA-Z_][a-zA-Z0-9_]*\\s*(>=|<=|==|!=|>|<)\\s*([a-zA-Z_][a-zA-Z0-9_]*|\\d+|\\d+\\.\\d+|\"[^\"]*\"|'[^']*')$"
        );

        if (!condicionValida) {
            throw new ExcepcionSintactica(
                    "Condición inválida en spinda. Use: spinda(variable > valor){",
                    numLinea
            );
        }
    }

    // Método auxiliar opcional para validar tipos
    private void validarTiposExpresion(Token tipoDato, List<Token> tokens, int indiceInicio, int numLinea)
            throws ExcepcionSemantica {

        for (int k = indiceInicio; k < tokens.size() - 1; k++) {
            Token tok = tokens.get(k);
            String lexema = tok.lexema;

            switch (tipoDato.tipo) {

                case GABITE:
                    if (tok.tipo == TipoToken.DECIMAL
                            || lexema.contains(".")
                            || lexema.startsWith("\"")
                            || lexema.startsWith("'")) {
                        throw new ExcepcionSemantica(
                                "Error de Tipo: gabite solo acepta números enteros.",
                                numLinea
                        );
                    }

                    if (lexema.matches("\\d+")) {
                        if (lexema.length() > 10) {
                            throw new ExcepcionSemantica(
                                    "Error de rango: El valor para gabite no puede superar los 10 dígitos.",
                                    numLinea
                            );
                        }
                    }
                    break;

                case ESPEON:
                    if (lexema.startsWith("\"") || lexema.startsWith("'")) {
                        throw new ExcepcionSemantica(
                                "Error de Tipo: espeon solo acepta números decimales.",
                                numLinea
                        );
                    }

                    if (lexema.contains(".")) {
                        String[] partes = lexema.split("\\.");

                        if (partes[0].length() > 10
                                || (partes.length > 1 && partes[1].length() > 10)) {
                            throw new ExcepcionSemantica(
                                    "Error de rango: espeon acepta máximo 10 dígitos antes/después del punto.",
                                    numLinea
                            );
                        }

                    } else if (tok.tipo == TipoToken.ENTERO) {
                        throw new ExcepcionSemantica(
                                "Error de Tipo: espeon requiere punto decimal. Ejemplo: " + lexema + ".0",
                                numLinea
                        );
                    }
                    break;

                case FALINK:
                    boolean esCadenaValida =
                            (lexema.startsWith("\"") && lexema.endsWith("\""))
                            || (lexema.startsWith("'") && lexema.endsWith("'"));

                    if (!esCadenaValida
                            && tok.tipo != TipoToken.IDENTIFICADOR
                            && tok.tipo != TipoToken.OPERADOR_SUMA) {
                        throw new ExcepcionSemantica(
                                "Error de Tipo: falink solo acepta texto delimitado por comillas \"\" o ''.",
                                numLinea
                        );
                    }
                    break;

                default:
                    break;
            }
        }
    }
}