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
public class AnalizadorManual {
    public enum TipoToken {
        GABITE, ESPEON, FALINK, MEOWL,
        IDENTIFICADOR, NUMERO, ASIGNACION, PUNTO_COMA, OPERADOR, COMENTARIO, DESCONOCIDO
    }

    public static class Token {
        public TipoToken tipo;
        public String valor;
        public Token(TipoToken tipo, String valor) { this.tipo = tipo; this.valor = valor; }
    }

    public List<Token> escanear(String codigo) {
    List<Token> tokens = new ArrayList<>();
    String[] lineas = codigo.split("\n");
    
    String patronG = "\\b(gabite|espeon|falink|meowl)\\b";
    String patronID = "\\b([a-zA-Z_][a-zA-Z0-9_]*)\\b";
    String patronNum = "\\b(\\d+(\\.\\d+)?)\\b";
    String patronString = "(\"[^\"]*\")";
    String patronOps = "(\\+|-|\\*|/|\\?|;)";

    Pattern p = Pattern.compile(patronG + "|" + patronID + "|" + patronNum + "|" + patronString + "|" + patronOps);

    for (String l : lineas) {
        String lineaLimpia = l;
        
        if (lineaLimpia.contains("#")) {
            lineaLimpia = lineaLimpia.substring(0, lineaLimpia.indexOf("#"));
        }
        
        Matcher m = p.matcher(lineaLimpia); 
        
        while (m.find()) {
            String lexema = m.group();

            if (lexema.equals("gabite")) tokens.add(new Token(TipoToken.GABITE, lexema));
            else if (lexema.equals("espeon")) tokens.add(new Token(TipoToken.ESPEON, lexema));
            else if (lexema.equals("falink")) tokens.add(new Token(TipoToken.FALINK, lexema));
            else if (lexema.equals("meowl")) tokens.add(new Token(TipoToken.MEOWL, lexema));
            else if (lexema.startsWith("\"")) tokens.add(new Token(TipoToken.IDENTIFICADOR, lexema)); 
            else if (lexema.matches("[\\+\\-\\*/]")) tokens.add(new Token(TipoToken.OPERADOR, lexema));
            else if (lexema.equals("?")) tokens.add(new Token(TipoToken.ASIGNACION, lexema));
            else if (lexema.equals(";")) tokens.add(new Token(TipoToken.PUNTO_COMA, lexema));
            else if (lexema.matches("\\d+(\\.\\d+)?")) tokens.add(new Token(TipoToken.NUMERO, lexema));
            else tokens.add(new Token(TipoToken.IDENTIFICADOR, lexema));
        }
    }
    
    return tokens;
}
}
