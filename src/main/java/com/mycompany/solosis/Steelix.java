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

        // Eliminar comentarios
        if (linea.contains("#")) {
            linea = linea.substring(0, linea.indexOf("#"));
        }
        linea = linea.trim();
        if (linea.isEmpty()) return null;

        // Toda instrucción debe terminar en ';'
        if (!linea.endsWith(";")) {
            throw new ExcepcionSintactica("Falta el signo de cierre ';' al final", numLinea);
        }

        // No se permite '=' como asignación
        if (linea.contains("=")) {
            throw new ExcepcionSintactica("Operador inválido '='. Use '?' para asignación", numLinea);
        }

        // Normalizar: insertar espacio entre palabra reservada y lo que le sigue pegado
        // Ej: "gabitea?3;" -> "gabite a?3;"   "meowla;" -> "meowl a;"
        linea = linea.replaceAll("(gabite|espeon|falink|meowl)([a-zA-Z0-9_?])", "$1 $2");

        // Tokenizar con AnalizadorManual (funciona con o sin espacios)
        List<Token> tokens;
        try {
            AnalizadorManual analizador = new AnalizadorManual();
            tokens = analizador.escanear(linea);
        } catch (ExcepcionLexica | ExcepcionLimite e) {
            throw new ExcepcionSintactica("Error léxico: " + e.getMessage(), numLinea);
        }

        if (tokens.isEmpty()) return null;

        Token primero = tokens.get(0);

        // ── Validar meowl ──────────────────────────────────────────────
        if (primero.tipo == TipoToken.MEOWL) {
            if (tokens.size() < 3) {
                throw new ExcepcionSintactica(
                    "Instrucción meowl incompleta. Estructura esperada: meowl <id>;", numLinea);
            }
            return null;
        }

        // ── Validar declaraciones gabite / espeon / falink ─────────────
        if (primero.tipo == TipoToken.GABITE ||
            primero.tipo == TipoToken.ESPEON ||
            primero.tipo == TipoToken.FALINK) {

            // Mínimo: TIPO  ID  ?  VALOR  ;  = 5 tokens
            if (tokens.size() < 5) {
                throw new ExcepcionSintactica(
                    "Declaración incompleta. Estructura esperada: <tipo> <id> ? <valor>", numLinea);
            }

            Token tokId  = tokens.get(1);
            Token tokAsg = tokens.get(2);

            if (tokId.tipo != TipoToken.IDENTIFICADOR) {
                throw new ExcepcionSintactica(
                    "Se esperaba un identificador después de '" + primero.lexema + "'", numLinea);
            }
            if (tokAsg.tipo != TipoToken.ASIGNACION) {
                throw new ExcepcionSintactica(
                    "Se esperaba '?' después del identificador '" + tokId.lexema + "'", numLinea);
            }

            // Reconstruir valor (tokens entre '?' y ';')
            StringBuilder expresionSB = new StringBuilder();
            for (int k = 3; k < tokens.size() - 1; k++) {
                expresionSB.append(tokens.get(k).lexema);
            }
            String valor = expresionSB.toString();

            // ── Validaciones semánticas por tipo ──────────────────────
            switch (primero.tipo) {

                case GABITE:
                    if (valor.contains(".")) {
                        throw new ExcepcionSemantica(
                            "gabite solo acepta valores enteros (sin punto decimal)", numLinea);
                    }
                    for (int k = 3; k < tokens.size() - 1; k++) {
                        Token tok = tokens.get(k);
                        if (tok.tipo != TipoToken.ENTERO &&
                            tok.tipo != TipoToken.IDENTIFICADOR &&
                            tok.tipo != TipoToken.OPERADOR_SUMA &&
                            tok.tipo != TipoToken.OPERADOR_RESTA &&
                            tok.tipo != TipoToken.OPERADOR_MULT &&
                            tok.tipo != TipoToken.OPERADOR_DIV) {
                            throw new ExcepcionSemantica(
                                "Valor inválido para gabite: '" + tok.lexema + "'", numLinea);
                        }
                    }
                    break;

                case ESPEON:
                    for (int k = 3; k < tokens.size() - 1; k++) {
                        Token tok = tokens.get(k);
                        if (tok.tipo != TipoToken.DECIMAL &&
                            tok.tipo != TipoToken.ENTERO &&
                            tok.tipo != TipoToken.IDENTIFICADOR &&
                            tok.tipo != TipoToken.OPERADOR_SUMA &&
                            tok.tipo != TipoToken.OPERADOR_RESTA &&
                            tok.tipo != TipoToken.OPERADOR_MULT &&
                            tok.tipo != TipoToken.OPERADOR_DIV) {
                            throw new ExcepcionSemantica(
                                "Valor inválido para espeon: '" + tok.lexema + "'", numLinea);
                        }
                    }
                    // Validar límites si es literal decimal puro
                    if (valor.matches("[0-9.]+") && valor.contains(".")) {
                        String[] partesDec = valor.split("\\.");
                        if (partesDec[0].length() > 10) {
                            throw new ExcepcionSemantica(
                                "espeon excede el límite de 10 dígitos enteros", numLinea);
                        }
                        if (partesDec.length > 1 && partesDec[1].length() > 7) {
                            throw new ExcepcionSemantica(
                                "espeon excede el límite de 7 dígitos decimales", numLinea);
                        }
                    }
                    break;

                case FALINK:
                    for (int k = 3; k < tokens.size() - 1; k++) {
                        Token tok = tokens.get(k);
                        if (tok.tipo != TipoToken.STRING &&
                            tok.tipo != TipoToken.IDENTIFICADOR &&
                            tok.tipo != TipoToken.OPERADOR_SUMA) {
                            throw new ExcepcionSemantica(
                                "Valor inválido para falink: '" + tok.lexema + "'", numLinea);
                        }
                    }
                    break;
            }

            return null;
        }

        // Si no empieza con palabra reservada conocida
        throw new ExcepcionSintactica(
            "Instrucción no reconocida: '" + primero.lexema + "'", numLinea);
    }
}