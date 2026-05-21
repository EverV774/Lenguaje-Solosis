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
    private final HashMap<String, TipoToken> tiposDeclarados = new HashMap<>();
    private final StringBuilder logEjecucion = new StringBuilder();
    // NUEVO: Lista para acumular todos los errores encontrados en el programa
    private final List<String> erroresAcumulados = new ArrayList<>();

    public void ejecutar(List<Token> tokens) {

        tablaSimbolos.clear();
        tiposDeclarados.clear();
        logEjecucion.setLength(0);
        erroresAcumulados.clear(); // Limpiar errores anteriores

        int i = 0;

        while (i < tokens.size()) {
            Token t = tokens.get(i);
            int lineaActual = t.linea;

            // Usamos un bloque try-catch INTERNO en el ciclo para que si una línea falla,
            // se guarde el error, pero el compilador continúe alegremente con la siguiente línea.
            try {

                // ─────────────────────────────────────────────
                // CASO 1: DECLARACIONES (Con o sin asignación inicial)
                // ─────────────────────────────────────────────
                if (i + 1 < tokens.size() &&
                    tokens.get(i).tipo == TipoToken.IDENTIFICADOR &&
                    (tokens.get(i + 1).tipo == TipoToken.GABITE ||
                     tokens.get(i + 1).tipo == TipoToken.ESPEON ||
                     tokens.get(i + 1).tipo == TipoToken.FALINK)) {

                    Token tokId = tokens.get(i);
                    Token tipoDato = tokens.get(i + 1);
                    String nombreVar = tokId.lexema;

                    if (tiposDeclarados.containsKey(nombreVar)) {
                        throw new ExcepcionSemantica("La variable '" + nombreVar + "' ya fue declarada", lineaActual);
                    }

                    tiposDeclarados.put(nombreVar, tipoDato.tipo);

                    List<Token> lineaActualTokens = new ArrayList<>();
                    lineaActualTokens.add(tokens.get(i));
                    lineaActualTokens.add(tokens.get(i + 1));

                    // Subcaso A: Declaración pura SIN asignación (ej: x gabite;)
                    if (i + 2 < tokens.size() && tokens.get(i + 2).tipo == TipoToken.PUNTO_COMA) {
                        lineaActualTokens.add(tokens.get(i + 2));
                        tablaSimbolos.put(nombreVar, null); // Nace vacía
                        generarReporteTokens(lineaActualTokens);
                        i += 3;
                        continue;
                    }

                    // Subcaso B: Declaración CON asignación inicial (ej: x gabite ? 5;)
                    lineaActualTokens.add(tokens.get(i + 2)); // El '?'
                    List<Token> tokensExpr = new ArrayList<>();
                    int j = i + 3;

                    while (j < tokens.size() && tokens.get(j).tipo != TipoToken.PUNTO_COMA) {
                        tokensExpr.add(tokens.get(j));
                        lineaActualTokens.add(tokens.get(j));
                        j++;
                    }

                    if (j < tokens.size() && tokens.get(j).tipo == TipoToken.PUNTO_COMA) {
                        lineaActualTokens.add(tokens.get(j));
                    }

                    Operacion resultado = evaluarExpresion(tokensExpr, lineaActual);
                    validarCompatibilidadTipo(tipoDato.tipo, resultado, lineaActual);
                    
                    tablaSimbolos.put(nombreVar, resultado);
                    generarReporteTokens(lineaActualTokens);

                    i = j + 1;
                }

                // ─────────────────────────────────────────────
                // CASO 2: ASIGNACIÓN POSTERIOR O REASIGNACIÓN
                // ─────────────────────────────────────────────
                else if (i + 1 < tokens.size() &&
                         tokens.get(i).tipo == TipoToken.IDENTIFICADOR &&
                         tokens.get(i + 1).tipo == TipoToken.ASIGNACION) {

                    Token tokId = tokens.get(i);
                    String nombreVar = tokId.lexema;

                    if (!tiposDeclarados.containsKey(nombreVar)) {
                        throw new ExcepcionSemantica("La variable '" + nombreVar + "' no ha sido declarada.", lineaActual);
                    }

                    List<Token> lineaActualTokens = new ArrayList<>();
                    lineaActualTokens.add(tokens.get(i));   // ID
                    lineaActualTokens.add(tokens.get(i + 1)); // ?

                    List<Token> tokensExpr = new ArrayList<>();
                    int j = i + 2;

                    while (j < tokens.size() && tokens.get(j).tipo != TipoToken.PUNTO_COMA) {
                        tokensExpr.add(tokens.get(j));
                        lineaActualTokens.add(tokens.get(j));
                        j++;
                    }

                    if (j < tokens.size() && tokens.get(j).tipo == TipoToken.PUNTO_COMA) {
                        lineaActualTokens.add(tokens.get(j));
                    }

                    Operacion resultado = evaluarExpresion(tokensExpr, lineaActual);

                    TipoToken tipoOriginal = tiposDeclarados.get(nombreVar);
                    validarCompatibilidadTipo(tipoOriginal, resultado, lineaActual);

                    tablaSimbolos.put(nombreVar, resultado);
                    generarReporteTokens(lineaActualTokens);

                    i = j + 1;
                }

                // ─────────────────────────────────────────────
                // CASO 3: SINTAXIS MEOWL FLEXIBLE
                // ─────────────────────────────────────────────
                else if (lineaContieneMeowl(tokens, i)) {
                    
                    List<Token> tokensExpr = new ArrayList<>();
                    List<Token> lineaActualTokens = new ArrayList<>();
                    
                    int j = i;
                    while (j < tokens.size() && tokens.get(j).tipo != TipoToken.MEOWL) {
                        tokensExpr.add(tokens.get(j));
                        lineaActualTokens.add(tokens.get(j));
                        j++;
                    }
                    
                    Token tokMeowl = tokens.get(j);
                    lineaActualTokens.add(tokMeowl);
                    
                    j++; 
                    if (j < tokens.size() && tokens.get(j).tipo == TipoToken.PUNTO_COMA) {
                        lineaActualTokens.add(tokens.get(j));
                    }
                    
                    Operacion resultado = evaluarExpresion(tokensExpr, lineaActual);
                    
                    if (resultado == null) {
                        throw new ExcepcionSemantica("Error: No hay nada que imprimir antes de 'meowl'.", lineaActual);
                    }

                    generarReporteTokens(lineaActualTokens);

                    logEjecucion.append("comando de salida: ").append(tokMeowl.lexema).append("\n")
                                .append("lo que sale: ").append(resultado.getValor()).append("\n");
                    logEjecucion.append("─────────────────────────────────────────────\n");

                    i = j + 1;
                }

                else {
                    i++;
                }

            } catch (ExcepcionSemantica e) {
                // ¡AQUÍ ESTÁ EL TRUCO! En lugar de romper el programa, guardamos el error
                erroresAcumulados.add("Error Línea " + lineaActual + ": " + e.getMessage());
                
                // Avanzamos el puntero 'i' hasta encontrar el próximo ';' para saltarnos esta línea rota
                while (i < tokens.size() && tokens.get(i).tipo != TipoToken.PUNTO_COMA) {
                    i++;
                }
                i++; // pasamos el punto y coma
            }
        }

        // ── REPORTE FINAL DE RESULTADOS O ERRORES ──
        if (!erroresAcumulados.isEmpty()) {
            logEjecucion.setLength(0); // Limpiamos éxitos parciales
            logEjecucion.append("❌ SE ENCONTRARON ERRORES SEMÁNTICOS EN EL PROGRAMA:\n");
            logEjecucion.append("───────────────────────────────────────────────────\n");
            for (String error : erroresAcumulados) {
                logEjecucion.append(error).append("\n");
            }
            logEjecucion.append("───────────────────────────────────────────────────\n");
            logEjecucion.append("\n>>> EJECUCIÓN ABORTADA POR ERRORES <<<");
        } else {
            logEjecucion.append("\n>>> FIN DE LA EJECUCIÓN <<<\n");
            logEjecucion.append("[PROCESO TERMINADO CON ÉXITO]");
        }
    }

    private Operacion evaluarExpresion(List<Token> tokensExpr, int linea) throws ExcepcionSemantica {
        if (tokensExpr.isEmpty()) return null;

        Operacion resultado = buscarOConvertir(tokensExpr.get(0).lexema, linea);

        int i = 1;
        while (i < tokensExpr.size()) {
            String op = tokensExpr.get(i).lexema;
            Operacion sig = buscarOConvertir(tokensExpr.get(i + 1).lexema, linea);

            String tipoRes = resultado.getClass().getSimpleName().toLowerCase();
            String tipoSig = sig.getClass().getSimpleName().toLowerCase();

            if (tipoRes.equals("gabite") && !tipoSig.equals("gabite")) {
                throw new ExcepcionSemantica("gabite solo puede realizar operaciones con otros enteros.", linea);
            }
            
            if (tipoRes.equals("espeon") && tipoSig.equals("gabite")) {
                double valorEnteroComoDecimal = Double.parseDouble(sig.getValor().toString());
                sig = new Espeon(valorEnteroComoDecimal);
            } else if (tipoRes.equals("espeon") && tipoSig.equals("falink")) {
                throw new ExcepcionSemantica("No se puede operar un decimal (espeon) con un texto (falink).", linea);
            }

            if (tipoRes.equals("falink")) {
                if (!op.equals("+")) {
                    throw new ExcepcionSemantica("falink solo admite el operador '+' para unir textos.", linea);
                }
                String cadenaUnida = resultado.getValor().toString() + sig.getValor().toString();
                resultado = new Falink(cadenaUnida);
                i += 2;
                continue;
            }

            if (tipoSig.equals("falink") && !tipoRes.equals("falink")) {
                throw new ExcepcionSemantica("No se puede sumar un texto a un tipo numérico de forma directa.", linea);
            }

            switch (op) {
                case "+": resultado = resultado.suma(sig); break;
                case "-": resultado = resultado.resta(sig); break;
                case "*": resultado = resultado.multiplicacion(sig); break;
                case "/": resultado = resultado.division(sig); break;
                default:
                    throw new ExcepcionSemantica("Operador desconocido: '" + op + "'", linea);
            }
            i += 2;
        }

        return resultado;
    }

    private Operacion buscarOConvertir(String lexema, int linea) throws ExcepcionSemantica {
        if (tablaSimbolos.containsKey(lexema)) {
            Operacion op = tablaSimbolos.get(lexema);
            if (op == null) {
                throw new ExcepcionSemantica("La variable '" + lexema + "' no ha sido inicializada.", linea);
            }
            return op;
        }

        if (lexema.matches("\\d+")) {
            if (lexema.length() > 10) {
                throw new ExcepcionSemantica("gabite solo acepta un máximo de 10 dígitos.", linea);
            }
            long valorLong = Long.parseLong(lexema);
            return new gabite((int) valorLong); 
        }

        if (lexema.matches("\\d+\\.\\d+")) {
            String[] partes = lexema.split("\\.");
            if (partes[0].length() > 10 || partes[1].length() > 10) {
                throw new ExcepcionSemantica("espeon acepta máximo 10 dígitos antes/después del punto.", linea);
            }
            return new Espeon(Double.parseDouble(lexema));
        }

        if (lexema.startsWith("\"") || lexema.startsWith("'")) {
            return new Falink(lexema.replaceAll("^[\"']|[\"']$", ""));
        }

        throw new ExcepcionSemantica("El identificador o componente '" + lexema + "' no está definido.", linea);
    }

    private void validarCompatibilidadTipo(TipoToken tipoEsperado, Operacion resultado, int linea) throws ExcepcionSemantica {
        if (resultado == null) return;
        String nombreClase = resultado.getClass().getSimpleName().toLowerCase();

        if (tipoEsperado == TipoToken.GABITE && !nombreClase.equals("gabite")) {
            throw new ExcepcionSemantica("No se puede asignar este resultado a un gabite (requiere entero).", linea);
        }
        if (tipoEsperado == TipoToken.ESPEON && !nombreClase.equals("espeon")) {
            throw new ExcepcionSemantica("No se puede asignar este resultado a un espeon (requiere decimal).", linea);
        }
        if (tipoEsperado == TipoToken.FALINK && !nombreClase.equals("falink")) {
            throw new ExcepcionSemantica("No se puede asignar este resultado a un falink (requiere cadena).", linea);
        }
    }

    private boolean lineaContieneMeowl(List<Token> tokens, int indiceActual) {
        int k = indiceActual;
        while (k < tokens.size()) {
            if (tokens.get(k).tipo == TipoToken.PUNTO_COMA) return false;
            if (tokens.get(k).tipo == TipoToken.MEOWL) return true;
            k++;
        }
        return false;
    }

    private void generarReporteTokens(List<Token> tokensLinea) {
        StringBuilder ids = new StringBuilder();
        StringBuilder palabras = new StringBuilder();
        StringBuilder operadores = new StringBuilder();
        StringBuilder constantes = new StringBuilder();
        StringBuilder cierres = new StringBuilder();

        for (Token tok : tokensLinea) {
            switch (tok.tipo) {
                case IDENTIFICADOR: ids.append(tok.lexema).append(" "); break;
                case GABITE:
                case ESPEON:
                case FALINK:
                case MEOWL:       palabras.append(tok.lexema).append(" "); break;
                case OPERADOR_SUMA:
                case OPERADOR_RESTA:
                case OPERADOR_MULT:
                case OPERADOR_DIV:
                case ASIGNACION:  operadores.append(tok.lexema).append(" "); break;
                case ENTERO:
                case DECIMAL:
                case STRING:      constantes.append(tok.lexema).append(" "); break;
                case PUNTO_COMA:  cierres.append(tok.lexema).append(" "); break;
                default: break;
            }
        }

        logEjecucion.append("identificador: ").append(ids.toString().trim()).append("\n");
        logEjecucion.append("palabra reservada: ").append(palabras.toString().trim()).append("\n");
        logEjecucion.append("operador: ").append(operadores.toString().trim()).append("\n");
        logEjecucion.append("constante: ").append(constantes.toString().trim()).append("\n");
        logEjecucion.append("signo de cierre: ").append(cierres.toString().trim()).append("\n");
        logEjecucion.append("─────────────────────────────────────────────\n");
    }

    public String obtenerLogEjecucion() { return logEjecucion.toString(); }
}