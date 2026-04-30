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
        
        if (linea.contains("#")) {
            linea = linea.substring(0, linea.indexOf("#"));
        }
        
        linea = linea.trim();
        
        if (linea.isEmpty()) return;
        
        if (linea.startsWith("meowl")) {
            if (!linea.endsWith(";")) {
                throw new Exception("Excepción: Falta ';' en la instrucción loudred. Línea " + numLinea);
            }
            return; 
        }
        
        if (!linea.endsWith(";")) {
            throw new Exception("Excepción: Falta signo de cierre ';' al final. Línea " + numLinea);
        }
        
        if (linea.contains("=")) {
            throw new Exception("Excepción: Operador inválido. Use '?' para asignación. Línea " + numLinea);
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
                if (!valor.startsWith("\"") && !valor.matches("[a-zA-Z]+")) {
                    throw new Exception("Excepción: falink requiere comillas o una variable válida. Línea " + numLinea);
                }
            }
            
            if (tipo.equals("gabite")) {
                if (valor.contains(".")) {
                    throw new Exception("Excepción: gabite solo acepta enteros. Línea " + numLinea);
                }
                
                String soloOperandos = valor.replaceAll("[\\+\\-\\*/]", " ");
                for (String op : soloOperandos.split("\\s+")) {
                    if (!op.matches("\\d+") && !op.matches("[a-zA-Z]+")) {
                        throw new Exception("Excepción: Valor o variable inválida para gabite. Línea " + numLinea);
                    }
                }
            }
            
            if (tipo.equals("espeon")) {
                if (valor.matches("[0-9.]+")) {
                    if (!valor.contains(".")) {
                        throw new Exception("Excepción: espeon requiere punto decimal. Línea " + numLinea);
                    }
                    
                    String[] partesDecimal = valor.split("\\.");
                    if (partesDecimal[0].replace("-", "").length() > 10) {
                        throw new Exception("Excepción: espeon excede 10 dígitos enteros. Línea " + numLinea);
                    }
                    if (partesDecimal.length > 1 && partesDecimal[1].length() > 7) {
                        throw new Exception("Excepción: espeon excede 7 dígitos decimales. Línea " + numLinea);
                    }
                }
            }
        }
        
        if (!linea.startsWith("gabite") && !linea.startsWith("espeon") && 
            !linea.startsWith("falink") && !linea.startsWith("meowl")) {
            throw new Exception("Excepción: Instrucción no reconocida '" + linea + "'. Línea " + numLinea);
        }
    }
}