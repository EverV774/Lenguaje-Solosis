/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.solosis;

import com.mycompany.solosis.AnalizadorLexico.TipoToken;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author Heber
 */
public class AnaluzadorManual {
    public enum TipoToken {
        GABITE, ESPEON, FALINK, IDENTIFICADOR, NUMERO, ASIGNACION, PUNTO_COMA, OPERADOR, DESCONOCIDO
    }

    // DEBE SER PUBLIC Y STATIC
    public static class Token {
        public TipoToken tipo;
        public String valor;

        public Token(TipoToken tipo, String valor) {
            this.tipo = tipo;
            this.valor = valor;
        }
        @Override
        public String toString() { return tipo + ": " + valor; }
    }

    public List<Token> escanear(String codigo) {
        List<Token> tokens = new ArrayList<>();
        String patronG = "\\b(GABITE|ESPEON|FALINK)\\b";
        String patronID = "\\b([a-zA-Z_][a-zA-Z0-9_]*)\\b";
        String patronNum = "\\b(\\d+(\\.\\d+)?)\\b";
        String patronOps = "(\\+|-|\\*|/|=|;)";

        Pattern p = Pattern.compile(patronG + "|" + patronID + "|" + patronNum + "|" + patronOps);
        Matcher m = p.matcher(codigo);

        while (m.find()) {
            String lexema = m.group();
            if (lexema.equals("GABITE")) tokens.add(new Token(TipoToken.GABITE, lexema));
            else if (lexema.equals("ESPEON")) tokens.add(new Token(TipoToken.ESPEON, lexema));
            else if (lexema.equals("FALINK")) tokens.add(new Token(TipoToken.FALINK, lexema));
            else if (lexema.matches("[\\+\\-\\*/]")) tokens.add(new Token(TipoToken.OPERADOR, lexema));
            else if (lexema.equals("=")) tokens.add(new Token(TipoToken.ASIGNACION, lexema));
            else if (lexema.equals(";")) tokens.add(new Token(TipoToken.PUNTO_COMA, lexema));
            else if (lexema.matches("\\d+(\\.\\d+)?")) tokens.add(new Token(TipoToken.NUMERO, lexema));
            else tokens.add(new Token(TipoToken.IDENTIFICADOR, lexema));
        }
        return tokens;
    }
}
