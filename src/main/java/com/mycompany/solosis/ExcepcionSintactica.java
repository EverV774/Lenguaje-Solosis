package com.mycompany.solosis;

/**
 * Se lanza cuando una instrucción viola las reglas sintácticas
 * del lenguaje Solosis (falta ';', uso de '=' en lugar de '?', etc.).
 */
public class ExcepcionSintactica extends ExcepcionSolosis {

    public ExcepcionSintactica(String mensaje, int linea, int columna) {
        super("Error Sintáctico: " + mensaje, linea, columna);
    }

    public ExcepcionSintactica(String mensaje, int linea) {
        super("Error Sintáctico: " + mensaje, linea);
    }
}
