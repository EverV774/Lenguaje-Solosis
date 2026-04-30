/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.solosis;

/**
 *
 * @author crack
 */
public class Steelix {
    public String validarLinea(String linea, int numLinea) {
        if (linea.contains("#")) {
            linea = linea.substring(0, linea.indexOf("#"));
        }

        linea = linea.trim();
        if (linea.isEmpty()) return null; 
        
        if (linea.startsWith("meowl")) {
            if (!linea.endsWith(";")) {
                return "Excepción: Falta ';' en la instrucción meowl. Línea " + numLinea;
            }
            return null; 
        }

        if (!linea.endsWith(";")) {
            return "Excepción: Falta signo de cierre ';' al final. Línea " + numLinea;
        }

        if (linea.contains("=")) {
            return "Excepción: Operador inválido. Use '?' para asignación. Línea " + numLinea;
        }

        if (!linea.startsWith("gabite") && !linea.startsWith("espeon") && 
            !linea.startsWith("falink")) {
            return "Excepción: Instrucción no reconocida '" + linea + "'. Línea " + numLinea;
        }

        String sinPuntoComa = linea.replace(";", "");
        String[] partes = sinPuntoComa.split("\\s+");

        if (partes.length >= 4) {
            String tipo = partes[0];
            StringBuilder expresionCompleta = new StringBuilder();
            for (int k = 3; k < partes.length; k++) {
                expresionCompleta.append(partes[k]);
            }
            String valor = expresionCompleta.toString();

            if (tipo.equals("falink")) {
                // Ahora permite letras, números, comillas, espacios y el signo +
                if (!valor.startsWith("\"") && !valor.matches("[a-zA-Z0-9\\+\\s\"]+")) {
                    return "Excepción: falink requiere comillas, variables o una expresión válida. Línea " + numLinea;
                }
            }

            if (tipo.equals("gabite")) {
                if (valor.contains(".")) {
                    return "Excepción: gabite solo acepta enteros. Línea " + numLinea;
                }
                String soloOperandos = valor.replaceAll("[\\+\\-\\*/]", " ");
                for (String op : soloOperandos.split("\\s+")) {
                    if (!op.matches("\\d+") && !op.matches("[a-zA-Z]+")) {
                        return "Excepción: Valor o variable inválida para gabite. Línea " + numLinea;
                    }
                }
            }

            if (tipo.equals("espeon")) {
                if (valor.matches("[0-9.]+")) {
                    if (!valor.contains(".")) {
                        return "Excepción: espeon requiere punto decimal. Línea " + numLinea;
                    }
                    String[] partesDecimal = valor.split("\\.");
                    if (partesDecimal[0].replace("-", "").length() > 10) {
                        return "Excepción: espeon excede 10 dígitos enteros. Línea " + numLinea;
                    }
                    if (partesDecimal.length > 1 && partesDecimal[1].length() > 7) {
                        return "Excepción: espeon excede 7 dígitos decimales. Línea " + numLinea;
                    }
                }   
            }
        } else {
            return "Excepción: Estructura de declaración incompleta. Línea " + numLinea;
        }

        return null; 
    }
}