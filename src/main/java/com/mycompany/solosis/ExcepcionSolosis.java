package com.mycompany.solosis;

/**
 * Clase base para todas las excepciones personalizadas del lenguaje Solosis.
 * Incluye información de línea y columna para mejor diagnóstico.
 */
public class ExcepcionSolosis extends Exception {
    protected final int linea;
    protected final int columna;

    public ExcepcionSolosis(String mensaje, int linea, int columna) {
        super("[Línea " + linea + ", Col " + columna + "] " + mensaje);
        this.linea   = linea;
        this.columna = columna;
    }

    public ExcepcionSolosis(String mensaje, int linea) {
        this(mensaje, linea, 0);
    }

    public int getLinea()   { return linea;   }
    public int getColumna() { return columna; }
}
