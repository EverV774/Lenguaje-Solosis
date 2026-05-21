package com.mycompany.solosis;

//----------------------------------------------------
// Constantes de símbolos para el parser CUP - Solosis
//----------------------------------------------------
public class sym {
    /* terminals */
    public static final int EOF         = 0;
    public static final int error       = 1;
    public static final int GABITE      = 2;
    public static final int FALINK      = 3;
    public static final int ESPEON      = 4;
    public static final int SUMA        = 5;
    public static final int RESTA       = 6;
    public static final int MULT        = 7;
    public static final int DIV         = 8;
    public static final int ASIGNACION  = 9;
    public static final int ENTERO      = 10;
    public static final int ID          = 11;
    public static final int STRING      = 12;
    public static final int DECIMAL     = 13;
    public static final int PUNTO_COMA  = 14;
    
    public static final int LIZARD = 50;
  public static final int PURPLE_LIZARD = 51;
  public static final int PARENTESIS_A = 52;
  public static final int PARENTESIS_C = 53;
  public static final int LLAVE_A = 54;
  public static final int LLAVE_C = 55;
  public static final int MENOR = 56;
  public static final int MAYOR = 57;
  public static final int IGUAL = 58;

    public static final String[] terminalNames = new String[] {
        "EOF", "error", "GABITE", "FALINK", "ESPEON",
        "SUMA", "RESTA", "MULT", "DIV", "ASIGNACION",
        "ENTERO", "ID", "STRING", "DECIMAL", "PUNTO_COMA"
    };
}

