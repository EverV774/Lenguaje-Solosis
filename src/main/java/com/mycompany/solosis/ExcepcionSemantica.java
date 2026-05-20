package com.mycompany.solosis;

/**
 * Se lanza cuando se detecta un error semántico durante la ejecución,
 * por ejemplo: variable ya declarada, tipo de dato incompatible,
 * límite de dígitos excedido, o valor inválido para el tipo.
 */
public class ExcepcionSemantica extends ExcepcionSolosis {

    public ExcepcionSemantica(String mensaje, int linea, int columna) {
        super("Error Semántico: " + mensaje, linea, columna);
    }

    public ExcepcionSemantica(String mensaje, int linea) {
        super("Error Semántico: " + mensaje, linea);
    }
}
