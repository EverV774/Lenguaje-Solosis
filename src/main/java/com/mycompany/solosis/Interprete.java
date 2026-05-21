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
                else if (tokens.get(i).tipo == TipoToken.SPINDA) {
                    i = ejecutarSpinda(tokens, i);
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

            } catch (ExcepcionSolosis e) {
                erroresAcumulados.add("Error Línea " + lineaActual + ": " + e.getMessage());

            while (i < tokens.size()
                    && tokens.get(i).tipo != TipoToken.PUNTO_COMA
                    && tokens.get(i).tipo != TipoToken.LLAVE_DER) {
                i++;
                }

    if (i < tokens.size()) {
        i++;
    }
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
                case SPINDA:      palabras.append(tok.lexema).append(" "); break;
                case OPERADOR_SUMA:
                case OPERADOR_RESTA:
                case OPERADOR_MULT:
                case OPERADOR_DIV:
                case ASIGNACION:  operadores.append(tok.lexema).append(" "); break;
                case MAYOR:
                case MENOR:
                case MAYOR_IGUAL:
                case MENOR_IGUAL:
                case IGUAL_IGUAL:
                case DIFERENTE:   operadores.append(tok.lexema).append(" "); break;
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
    
    private int ejecutarSpinda(List<Token> tokens, int i)
        throws ExcepcionSintactica, ExcepcionSemantica {

    int linea = tokens.get(i).linea;

    if (i + 1 >= tokens.size() || tokens.get(i + 1).tipo != TipoToken.PARENTESIS_IZQ) {
        throw new ExcepcionSintactica("Falta '(' después de spinda.", linea);
    }

    int inicioCondicion = i + 2;
    int finCondicion = buscarCierreSimple(tokens, inicioCondicion, TipoToken.PARENTESIS_DER, linea,
            "Falta ')' para cerrar la condición de spinda.");

    List<Token> condicion = new ArrayList<>(tokens.subList(inicioCondicion, finCondicion));

    if (condicion.isEmpty()) {
        throw new ExcepcionSintactica("La condición de spinda está vacía.", linea);
    }

    if (finCondicion + 1 >= tokens.size() || tokens.get(finCondicion + 1).tipo != TipoToken.LLAVE_IZQ) {
        throw new ExcepcionSintactica("Falta '{' después de la condición de spinda.", linea);
    }

    int inicioBloque = finCondicion + 2;
    int finBloque = buscarLlaveCierre(tokens, inicioBloque, linea);

    boolean condicionVerdadera = evaluarCondicionSpinda(condicion, linea);

    if (condicionVerdadera) {
        List<Token> bloque = new ArrayList<>(tokens.subList(inicioBloque, finBloque));
        ejecutarBloqueSpinda(bloque);
    } else {
        logEjecucion.append("spinda: condición falsa, bloque omitido.\n");
        logEjecucion.append("─────────────────────────────────────────────\n");
    }

    return finBloque + 1;
}

private int buscarCierreSimple(List<Token> tokens, int inicio, TipoToken cierre, int linea, String mensajeError)
        throws ExcepcionSintactica {

    int j = inicio;

    while (j < tokens.size()) {
        if (tokens.get(j).tipo == cierre) {
            return j;
        }
        j++;
    }

    throw new ExcepcionSintactica(mensajeError, linea);
}

private int buscarLlaveCierre(List<Token> tokens, int inicio, int linea)
        throws ExcepcionSintactica {

    int nivel = 1;
    int j = inicio;

    while (j < tokens.size()) {
        if (tokens.get(j).tipo == TipoToken.LLAVE_IZQ) {
            nivel++;
        } else if (tokens.get(j).tipo == TipoToken.LLAVE_DER) {
            nivel--;
            if (nivel == 0) {
                return j;
            }
        }
        j++;
    }

    throw new ExcepcionSintactica("Falta '}' para cerrar el bloque de spinda.", linea);
}

private boolean evaluarCondicionSpinda(List<Token> condicion, int linea)
        throws ExcepcionSemantica, ExcepcionSintactica {

    if (condicion.size() != 3) {
        throw new ExcepcionSintactica(
                "La condición de spinda debe tener la forma: valor comparador valor. Ejemplo: spinda (edad >= 18)",
                linea
        );
    }

    Token izquierdaToken = condicion.get(0);
    Token operadorToken = condicion.get(1);
    Token derechaToken = condicion.get(2);

    Operacion izquierda = buscarOConvertir(izquierdaToken.lexema, linea);
    Operacion derecha = buscarOConvertir(derechaToken.lexema, linea);

    Object valorIzq = izquierda.getValor();
    Object valorDer = derecha.getValor();

    TipoToken operador = operadorToken.tipo;

    boolean izqNumero = valorIzq instanceof Number;
    boolean derNumero = valorDer instanceof Number;

    if (izqNumero && derNumero) {
        double a = Double.parseDouble(valorIzq.toString());
        double b = Double.parseDouble(valorDer.toString());

        switch (operador) {
            case MAYOR:       return a > b;
            case MENOR:       return a < b;
            case MAYOR_IGUAL: return a >= b;
            case MENOR_IGUAL: return a <= b;
            case IGUAL_IGUAL: return a == b;
            case DIFERENTE:   return a != b;
            default:
                throw new ExcepcionSemantica("Operador inválido en condición spinda: " + operadorToken.lexema, linea);
        }
    }

    if (valorIzq instanceof String && valorDer instanceof String) {
        String a = valorIzq.toString();
        String b = valorDer.toString();

        switch (operador) {
            case IGUAL_IGUAL: return a.equals(b);
            case DIFERENTE:   return !a.equals(b);
            default:
                throw new ExcepcionSemantica("Los textos falink solo pueden compararse con == o !=.", linea);
        }
    }

    throw new ExcepcionSemantica("No se pueden comparar valores de tipos incompatibles en spinda.", linea);
}

    private void ejecutarBloqueSpinda(List<Token> bloque) {
        int i = 0;

        while (i < bloque.size()) {
            Token t = bloque.get(i);
            int lineaActual = t.linea;

        try {
            if (i + 1 < bloque.size() &&
                bloque.get(i).tipo == TipoToken.IDENTIFICADOR &&
                (bloque.get(i + 1).tipo == TipoToken.GABITE ||
                 bloque.get(i + 1).tipo == TipoToken.ESPEON ||
                 bloque.get(i + 1).tipo == TipoToken.FALINK)) {

                Token tokId = bloque.get(i);
                Token tipoDato = bloque.get(i + 1);
                String nombreVar = tokId.lexema;

                if (tiposDeclarados.containsKey(nombreVar)) {
                    throw new ExcepcionSemantica("La variable '" + nombreVar + "' ya fue declarada", lineaActual);
                }

                tiposDeclarados.put(nombreVar, tipoDato.tipo);

                List<Token> lineaActualTokens = new ArrayList<>();
                lineaActualTokens.add(bloque.get(i));
                lineaActualTokens.add(bloque.get(i + 1));

                if (i + 2 < bloque.size() && bloque.get(i + 2).tipo == TipoToken.PUNTO_COMA) {
                    lineaActualTokens.add(bloque.get(i + 2));
                    tablaSimbolos.put(nombreVar, null);
                    generarReporteTokens(lineaActualTokens);
                    i += 3;
                    continue;
                }

                lineaActualTokens.add(bloque.get(i + 2));

                List<Token> tokensExpr = new ArrayList<>();
                int j = i + 3;

                while (j < bloque.size() && bloque.get(j).tipo != TipoToken.PUNTO_COMA) {
                    tokensExpr.add(bloque.get(j));
                    lineaActualTokens.add(bloque.get(j));
                    j++;
                }

                if (j < bloque.size() && bloque.get(j).tipo == TipoToken.PUNTO_COMA) {
                    lineaActualTokens.add(bloque.get(j));
                }

                Operacion resultado = evaluarExpresion(tokensExpr, lineaActual);
                validarCompatibilidadTipo(tipoDato.tipo, resultado, lineaActual);

                tablaSimbolos.put(nombreVar, resultado);
                generarReporteTokens(lineaActualTokens);

                i = j + 1;
            }

            else if (i + 1 < bloque.size() &&
                     bloque.get(i).tipo == TipoToken.IDENTIFICADOR &&
                     bloque.get(i + 1).tipo == TipoToken.ASIGNACION) {

                Token tokId = bloque.get(i);
                String nombreVar = tokId.lexema;

                if (!tiposDeclarados.containsKey(nombreVar)) {
                    throw new ExcepcionSemantica("La variable '" + nombreVar + "' no ha sido declarada.", lineaActual);
                }

                List<Token> lineaActualTokens = new ArrayList<>();
                lineaActualTokens.add(bloque.get(i));
                lineaActualTokens.add(bloque.get(i + 1));

                List<Token> tokensExpr = new ArrayList<>();
                int j = i + 2;

                while (j < bloque.size() && bloque.get(j).tipo != TipoToken.PUNTO_COMA) {
                    tokensExpr.add(bloque.get(j));
                    lineaActualTokens.add(bloque.get(j));
                    j++;
                }

                if (j < bloque.size() && bloque.get(j).tipo == TipoToken.PUNTO_COMA) {
                    lineaActualTokens.add(bloque.get(j));
                }

                Operacion resultado = evaluarExpresion(tokensExpr, lineaActual);
                TipoToken tipoOriginal = tiposDeclarados.get(nombreVar);
                validarCompatibilidadTipo(tipoOriginal, resultado, lineaActual);

                tablaSimbolos.put(nombreVar, resultado);
                generarReporteTokens(lineaActualTokens);

                i = j + 1;
            }

            else if (bloque.get(i).tipo == TipoToken.SPINDA) {
                i = ejecutarSpinda(bloque, i);
            }

            else if (lineaContieneMeowl(bloque, i)) {
                List<Token> tokensExpr = new ArrayList<>();
                List<Token> lineaActualTokens = new ArrayList<>();

                int j = i;

                while (j < bloque.size() && bloque.get(j).tipo != TipoToken.MEOWL) {
                    tokensExpr.add(bloque.get(j));
                    lineaActualTokens.add(bloque.get(j));
                    j++;
                }

                Token tokMeowl = bloque.get(j);
                lineaActualTokens.add(tokMeowl);

                j++;

                if (j < bloque.size() && bloque.get(j).tipo == TipoToken.PUNTO_COMA) {
                    lineaActualTokens.add(bloque.get(j));
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

        } catch (ExcepcionSolosis e) {
            erroresAcumulados.add("Error Línea " + lineaActual + ": " + e.getMessage());

            while (i < bloque.size()
                    && bloque.get(i).tipo != TipoToken.PUNTO_COMA
                    && bloque.get(i).tipo != TipoToken.LLAVE_DER) {
                i++;
            }

            if (i < bloque.size()) {
                i++;
            }
        }
        }
    }
    public String obtenerLogEjecucion() { return logEjecucion.toString(); }
}