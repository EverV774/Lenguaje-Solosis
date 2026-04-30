/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.solosis;



import java.util.Arrays;
import java.util.List;


/**
 *
 * @author crack
 */
public class Steelix {
    public void validarLinea(String linea, int numLinea) throws Exception {
        linea = linea.trim();
        if (linea.isEmpty() || linea.startsWith("#")) return;

        // 1. Validar signo de cierre
        if (!linea.endsWith(";")) {
            throw new Exception("Excepción: Falta signo de cierre ';' al final. Línea " + numLinea);
        }

        // 2. Validar operador de asignación correcto
        if (linea.contains("=")) {
            throw new Exception("Excepción: Operador inválido. Use '?' para asignación. Línea " + numLinea);
        }

        String sinPuntoComa = linea.replace(";", "");
        String[] partes = sinPuntoComa.split("\\s+");

        // Estructura esperada: [0]tipo [1]id [2]? [3]valor
        if (partes.length >= 4) {
            String tipo = partes[0];
            String valor = partes[3];

            // --- REGLA 1: falink debe llevar comillas ---
            if (tipo.equals("falink")) {
                if (!valor.startsWith("\"") || !valor.endsWith("\"")) {
                    throw new Exception("Excepción: No se tienen definidos las variables correctamente. falink requiere comillas. Línea " + numLinea);
                }
            }

            // --- REGLA 2: gabite NO acepta decimales ni letras ---
            if (tipo.equals("gabite")) {
                if (valor.contains(".") || valor.matches(".*[a-zA-Z].*")) {
                    throw new Exception("Excepción: No se tienen definidos las variables correctamente. gabite solo acepta enteros. Línea " + numLinea);
                }
            }

            // --- REGLA 3: espeon DEBE tener punto decimal y no letras ---
            if (tipo.equals("espeon")) {
                if (!valor.contains(".") || valor.matches(".*[a-zA-Z].*")) {
                    throw new Exception("Excepción: No se tienen definidos las variables correctamente. espeon requiere punto decimal. Línea " + numLinea);
                }
                // Validar que realmente sea un número decimal
                try {
                    Double.parseDouble(valor);
                } catch (NumberFormatException e) {
                    throw new Exception("Excepción: Valor numérico inválido para espeon. Línea " + numLinea);
                }
            }
        }
    }
}
