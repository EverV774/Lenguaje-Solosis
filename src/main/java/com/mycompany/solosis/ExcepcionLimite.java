package com.mycompany.solosis;

/**
 * Se lanza cuando el programa supera el número máximo de declaraciones
 * permitidas en el lenguaje Solosis.
 *
 * Límite actual: {@value #LIMITE_MAX} declaraciones por programa.
 */
public class ExcepcionLimite extends ExcepcionSolosis {
    public static final int LIMITE_MAX = 50;

    public ExcepcionLimite(int linea) {
        super("Error de Límite: Se superó el máximo de " + LIMITE_MAX +
              " declaraciones permitidas en un programa Solosis", linea);
    }
}
