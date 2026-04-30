/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.solosis;

/**
 *
 * @author Heber
 */
public class AnalizadorLexico {
    public enum TipoToken {
        GABITE, ESPEON, FALINK, IDENTIFICADOR, NUMERO, ASIGNACION, PUNTO_COMA, OPERADOR, DESCONOCIDO
    }

    public class Token {
        TipoToken tipo;
        String valor;

        public Token(TipoToken tipo, String valor) {
            this.tipo = tipo;
            this.valor = valor;
        }
        @Override
        public String toString() { return tipo + ": " + valor; }
    }
}
