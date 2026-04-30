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
    private HashMap<String, Operacion> tablaSimbolos = new HashMap<>();
    private StringBuilder logEjecucion = new StringBuilder();
    
    public void ejecutar(List<AnalizadorManual.Token> tokens) throws Exception {
        this.tablaSimbolos.clear();
        logEjecucion.setLength(0);

        int i = 0;
        while (i < tokens.size()) {
            AnalizadorManual.Token t = tokens.get(i);
            
            if (t.tipo == TipoToken.GABITE || t.tipo == TipoToken.ESPEON || t.tipo == TipoToken.FALINK) {
                logEjecucion.append("Palabra reservada: ").append(t.valor).append("\n");
                
                String nombreVar = tokens.get(i + 1).valor;
                logEjecucion.append("Identificador: ").append(nombreVar).append("\n");
                
                logEjecucion.append("Operador asignación: ").append(tokens.get(i + 2).valor).append("\n");
                
                List<AnalizadorManual.Token> tokensExpresion = new ArrayList<>();
                int j = i + 3;
                while (j < tokens.size() && tokens.get(j).tipo != TipoToken.PUNTO_COMA) {
                    tokensExpresion.add(tokens.get(j));
                    j++;
                }
                
                StringBuilder expresionStr = new StringBuilder();
                for(AnalizadorManual.Token te : tokensExpresion) expresionStr.append(te.valor).append(" ");
                logEjecucion.append("Constante/Valor: ").append(expresionStr.toString().trim()).append("\n");

                logEjecucion.append("Signo de cierre: ;\n");
                logEjecucion.append("-------------------------------------------\n");

                Operacion resultadoFinal = evaluarExpresion(tokensExpresion);
                tablaSimbolos.put(nombreVar, resultadoFinal);

                i = j + 1; 
            } 

            else if (t.tipo == AnalizadorManual.TipoToken.MEOWL) {
                logEjecucion.append("Comando de salida: ").append(t.valor).append("\n");

                int j = i + 1;
                List<AnalizadorManual.Token> tokensExpresion = new ArrayList<>();
                while (j < tokens.size() && tokens.get(j).tipo != TipoToken.PUNTO_COMA) {
                    tokensExpresion.add(tokens.get(j));
                    j++;
                }

                Operacion resultado = evaluarExpresion(tokensExpresion);
                if (resultado != null) {
                    logEjecucion.append("> ").append(resultado.getValor().toString()).append("\n");
                }

                logEjecucion.append("Signo de cierre: ;\n");
                logEjecucion.append("-------------------------------------------\n");
                i = j + 1;
            }
            else {
                i++;
            }
        }

        logEjecucion.append("\n>>> FIN DE LA EJECUCIÓN <<<\n");
        logEjecucion.append("[PROCESO TERMINADO CON ÉXITO]");
    }
    
    private Operacion buscarOConvertir(String lexema) throws Exception {
        if (tablaSimbolos.containsKey(lexema)) {
            return tablaSimbolos.get(lexema);
        }
        
        if (lexema.matches("\\d+")) {
            return new gabite(Integer.parseInt(lexema));
        }
        
        if (lexema.matches("\\d+\\.\\d+")) {
            return new Espeon(Double.parseDouble(lexema));
        }
        
        if (lexema.startsWith("\"")) {
            return new Falink(lexema.replace("\"", ""));
        }

        throw new Exception("Error: El valor '" + lexema + "' no es reconocido o no existe.");
    }

    private Operacion evaluarExpresion(List<AnalizadorManual.Token> tokensExpresion) throws Exception {
        if (tokensExpresion.isEmpty()) return null;
        
        Operacion resultado = buscarOConvertir(tokensExpresion.get(0).valor);

        int i = 1;
        while (i < tokensExpresion.size()) {
            String operador = tokensExpresion.get(i).valor; 
            Operacion siguiente = buscarOConvertir(tokensExpresion.get(i + 1).valor);

            switch (operador) {
                case "+": resultado = resultado.suma(siguiente); break;
                case "-": resultado = resultado.resta(siguiente); break;
                case "*": resultado = resultado.multiplicacion(siguiente); break;
                case "/": resultado = resultado.division(siguiente); break;
            }
            i += 2;
        }
        return resultado;
    }
    
    public String obtenerLogEjecucion() { return logEjecucion.toString(); }
}