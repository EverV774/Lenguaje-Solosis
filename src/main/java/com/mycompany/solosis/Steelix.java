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
        linea = linea.trim();
        if (linea.isEmpty()) return null;

        // 2. Toda instrucción debe terminar en ';'
        if (!linea.endsWith(";")) {
            throw new ExcepcionSintactica("Falta el signo de cierre ';' al final", numLinea);
        }

        // 3. No se permite '=' como asignación en tu lenguaje
        if (linea.contains("=")) {
            throw new ExcepcionSintactica("Operador inválido '='. Use '?' para asignación", numLinea);
        }

        // Normalizar espacios alrededor de tus palabras clave para no romper el tokenizador
        linea = linea.replaceAll("(gabite|espeon|falink|meowl)([a-zA-Z0-9_?])", "$1 $2");

        // 4. Tokenizar con tu AnalizadorManual
        List<Token> tokens;
        try {
            AnalizadorManual analizador = new AnalizadorManual();
            tokens = analizador.escanear(linea);
        } catch (ExcepcionLexica | ExcepcionLimite e) {
            throw new ExcepcionSintactica("Error léxico: " + e.getMessage(), numLinea);
        }

        if (tokens.isEmpty()) return null;

        // ── CASO 1: Validar impresión con orden: <id> meowl ; ──
        // ── CASO 1: Validar impresión con orden flexible: <expresión> meowl ; (ACTUALIZADO) ──
        // Buscamos si el token "meowl" está en la línea (usualmente penúltimo antes del ';')
        int indiceMeowl = -1;
        for (int k = 0; k < tokens.size(); k++) {
            if (tokens.get(k).tipo == TipoToken.MEOWL) {
                indiceMeowl = k;
                break;
            }
        }

        if (indiceMeowl != -1) {
            // El token meowl no puede ser el primero de la línea (debe haber algo que imprimir)
            if (indiceMeowl == 0) {
                throw new ExcepcionSintactica("Se esperaba una expresión o variable antes de 'meowl'", numLinea);
            }
            // Asegurar que termine en punto y coma justo después de meowl
            if (indiceMeowl + 1 >= tokens.size() || tokens.get(indiceMeowl + 1).tipo != TipoToken.PUNTO_COMA) {
                throw new ExcepcionSintactica("Falta el signo de cierre ';' después de 'meowl'", numLinea);
            }
            return null; // Sintaxis estructural básica correcta (el Interprete validará la semántica interna)
        }

        // ── CASO 2: Declaración SIN asignación inicial (NUEVO): ID TIPO ; ──
        // Ejemplo: numero1 gabite; (Tiene exactamente 3 tokens: ID, TIPO, ;)
        if (tokens.size() == 3 &&
            tokens.get(0).tipo == TipoToken.IDENTIFICADOR &&
            (tokens.get(1).tipo == TipoToken.GABITE ||
             tokens.get(1).tipo == TipoToken.ESPEON ||
             tokens.get(1).tipo == TipoToken.FALINK)) {
            return null; // Es completamente válida
        }

        // ── CASO 3: Declaración CON asignación completa: ID TIPO ? VALOR ; ──
        if (tokens.size() >= 5 &&
            tokens.get(0).tipo == TipoToken.IDENTIFICADOR &&
            (tokens.get(1).tipo == TipoToken.GABITE ||
             tokens.get(1).tipo == TipoToken.ESPEON ||
             tokens.get(1).tipo == TipoToken.FALINK) &&
            tokens.get(2).tipo == TipoToken.ASIGNACION) {
            
            //validarTiposExpresion(tokens.get(1), tokens, 3, numLinea);
            return null; 
        }

        // ── CASO 4: Asignación pura posterior (NUEVO): ID ? VALOR ; ──
        // Ejemplo: numero1 ? 47 + 3;
        if (tokens.size() >= 4 &&
            tokens.get(0).tipo == TipoToken.IDENTIFICADOR &&
            tokens.get(1).tipo == TipoToken.ASIGNACION) {
            return null; // La validación de tipos exacta la hará el Interprete en ejecución
        }

        // Si la línea no encaja con ninguna estructura válida
        throw new ExcepcionSintactica(
            "Formato no reconocido o inválido para Solosis.", numLinea);
    }

    // Moví la lógica de validar tipos a este método auxiliar para mantener limpio el código
    private void validarTiposExpresion(Token tipoDato, List<Token> tokens, int indiceInicio, int numLinea) 
            throws ExcepcionSemantica {
        for (int k = indiceInicio; k < tokens.size() - 1; k++) {
            Token tok = tokens.get(k);
            String lexema = tok.lexema;

            switch (tipoDato.tipo) {
                case GABITE: 
                    if (tok.tipo == TipoToken.DECIMAL || lexema.contains(".") || 
                        lexema.startsWith("\"") || lexema.startsWith("'")) {
                        throw new ExcepcionSemantica("Error de Tipo: gabite solo acepta números enteros.", numLinea);
                    }
                    if (lexema.matches("\\d+")) {
                        if (lexema.length() > 10) {
                            throw new ExcepcionSemantica("Error de rango: El valor para gabite no puede superar los 10 dígitos.", numLinea);
                        }
                    }
                    break;

                case ESPEON: 
                    if (lexema.startsWith("\"") || lexema.startsWith("'")) {
                        throw new ExcepcionSemantica("Error de Tipo: espeon solo acepta números decimales.", numLinea);
                    }
                    if (lexema.contains(".")) {
                        String[] partes = lexema.split("\\.");
                        if (partes[0].length() > 10 || (partes.length > 1 && partes[1].length() > 10)) {
                            throw new ExcepcionSemantica("Error de rango: espeon acepta máximo 10 dígitos antes/después del punto.", numLinea);
                        }
                    } else if (tok.tipo == TipoToken.ENTERO) {
                        throw new ExcepcionSemantica("Error de Tipo: espeon requiere punto decimal (ej: " + lexema + ".0).", numLinea);
                    }
                    break;

                case FALINK: 
                    boolean esCadenaValida = (lexema.startsWith("\"") && lexema.endsWith("\"")) || 
                                             (lexema.startsWith("'") && lexema.endsWith("'"));
                    if (!esCadenaValida && tok.tipo != TipoToken.IDENTIFICADOR && tok.tipo != TipoToken.OPERADOR_SUMA) {
                        throw new ExcepcionSemantica("Error de Tipo: falink solo acepta texto delimitado por comillas \"\" o ''.", numLinea);
                    }
                    break;
            }
        }
    }
}