/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.solosis;

import com.mycompany.solosis.AnalizadorManual.TipoToken;
import com.mycompany.solosis.AnalizadorManual.Token;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Heber
 */

public class Interprete {
    private final HashMap<String, Operacion> tablaSimbolos = new HashMap<>();
    private final StringBuilder logEjecucion = new StringBuilder();

    public void ejecutar(List<Token> tokens) throws ExcepcionSemantica {
        tablaSimbolos.clear();
        logEjecucion.setLength(0);
        boolean usomeowl = false;

        int i = 0;
        while (i < tokens.size()) {                                          // WHILE ABRE
            Token t = tokens.get(i);

            if (t.tipo == TipoToken.GABITE || t.tipo == TipoToken.ESPEON || t.tipo == TipoToken.FALINK) {

                if (i + 3 >= tokens.size()) {
                    throw new ExcepcionSemantica("Declaración incompleta cerca de '" + t.lexema + "'", t.linea);
                }

                Token tokId = tokens.get(i + 1);
                String nombreVar = tokId.lexema;

                if (tablaSimbolos.containsKey(nombreVar)) {
                    throw new ExcepcionSemantica("La variable '" + nombreVar + "' ya fue declarada", t.linea);
                }

                List<Token> tokensExpr = new ArrayList<>();
                int j = i + 3;
                while (j < tokens.size() && tokens.get(j).tipo != TipoToken.PUNTO_COMA) {
                    tokensExpr.add(tokens.get(j));
                    j++;
                }

                Operacion resultado = evaluarExpresion(tokensExpr, t.linea);
                tablaSimbolos.put(nombreVar, resultado);
                i = j + 1;

            } else if (t.tipo == TipoToken.MEOWL) {

                List<Token> tokensExpr = new ArrayList<>();
                int j = i + 1;
                while (j < tokens.size() && tokens.get(j).tipo != TipoToken.PUNTO_COMA) {
                    tokensExpr.add(tokens.get(j));
                    j++;
                }

                Operacion resultado = evaluarExpresion(tokensExpr, t.linea);
                if (resultado != null) {
                    logEjecucion.append("meowl >> ").append(resultado.getValor()).append("\n");
                    usomeowl = true;
                }
                i = j + 1;

            } else {
                i++;
            }
        }                                                                     // WHILE CIERRA

        if (!usomeowl) {
            throw new ExcepcionSemantica(
                "[ERROR SEMÁNTICO] No se usó 'meowl'. Sin 'meowl' no hay salida. Ejemplo: meowl x;", 0);
        }

        logEjecucion.append("\n>>> FIN DE LA EJECUCIÓN <<<\n");
        logEjecucion.append("[PROCESO TERMINADO CON ÉXITO]");

    }                                                                         // MÉTODO CIERRA

    private Operacion buscarOConvertir(String lexema, int linea) throws ExcepcionSemantica {
        if (tablaSimbolos.containsKey(lexema)) return tablaSimbolos.get(lexema);
        if (lexema.matches("\\d+"))           return new gabite(Integer.parseInt(lexema));
        if (lexema.matches("\\d+\\.\\d+"))    return new Espeon(Double.parseDouble(lexema));
        if (lexema.startsWith("\""))          return new Falink(lexema.replace("\"", ""));
        throw new ExcepcionSemantica("El valor '" + lexema + "' no existe ni es reconocido", linea);
    }

    private Operacion evaluarExpresion(List<Token> tokensExpr, int linea) throws ExcepcionSemantica {
        if (tokensExpr.isEmpty()) return null;
        Operacion resultado = buscarOConvertir(tokensExpr.get(0).lexema, linea);
        int i = 1;
        while (i < tokensExpr.size()) {
            String op     = tokensExpr.get(i).lexema;
            Operacion sig = buscarOConvertir(tokensExpr.get(i + 1).lexema, linea);
            switch (op) {
                case "+": resultado = resultado.suma(sig);           break;
                case "-": resultado = resultado.resta(sig);          break;
                case "*": resultado = resultado.multiplicacion(sig); break;
                case "/": resultado = resultado.division(sig);       break;
                default:
                    throw new ExcepcionSemantica("Operador desconocido: '" + op + "'", linea);
            }
            i += 2;
        }
        return resultado;
    }

    public String obtenerLogEjecucion() { return logEjecucion.toString(); }
}                                                                             // CLASE CIERRA