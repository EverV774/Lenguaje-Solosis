/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.solosis;

import com.mycompany.solosis.AnalizadorManual.TipoToken;
import com.mycompany.solosis.AnalizadorManual.Token;
import java.util.HashMap;
import java.util.List;

/**
 * @author Heber
 */
public class Interprete {
    private HashMap<String, Object> tablaSimbolos = new HashMap<>();
    private StringBuilder logEjecucion = new StringBuilder();

    public void ejecutar(List<Token> tokens) {
        logEjecucion.setLength(0);
        logEjecucion.append(">>> INICIANDO INTERPRETACIÓN <<<\n\n");

        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);

            // Si es una palabra reservada (ahora en minúsculas)
            if (t.tipo == TipoToken.GABITE || t.tipo == TipoToken.ESPEON || t.tipo == TipoToken.FALINK) {
                logEjecucion.append("Palabra reservada: ").append(t.valor).append("\n");

                // En lugar de i+1, i+2... vamos a buscar los tokens reales
                if (i + 4 < tokens.size()) {
                    logEjecucion.append("Identificador: ").append(tokens.get(i + 1).valor).append("\n");
                    logEjecucion.append("Operador asignación: ").append(tokens.get(i + 2).valor).append("\n");
                    logEjecucion.append("Constante/Valor: ").append(tokens.get(i + 3).valor).append("\n");
                    logEjecucion.append("Signo de cierre: ").append(tokens.get(i + 4).valor).append("\n");
                    logEjecucion.append("-----------------------------------\n");
                    i += 4;
                }
            }
        }
        logEjecucion.append("\n>>> FIN DE LA EJECUCIÓN <<<");
    }

    public String obtenerLogEjecucion() { return logEjecucion.toString(); }
}
