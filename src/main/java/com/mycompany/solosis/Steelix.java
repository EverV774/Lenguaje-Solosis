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
   
    private final List<String> tiposValidos = Arrays.asList("int", "double", "string");

    public void validarLinea(String linea, int numLinea) throws Exception {
        linea = linea.trim(); 
        if (!linea.endsWith(";")) {
            throw new Exception("Excepción: Falta signo de cierre ';' al final. Línea " + numLinea);
        }

       
        String[] partes = linea.replace(";", "").split("\\s+");

        
        if (partes.length >= 2) {
            boolean empiezaConTipo = tiposValidos.contains(partes[0]);
            boolean elSegundoEsTipo = tiposValidos.contains(partes[1]);

            if (!empiezaConTipo && elSegundoEsTipo) {
                throw new Exception("Excepción: No se tienen definidos las variables correctamente. Línea " + numLinea);
            }
        }

        
        if (linea.contains("+") || linea.contains("-") || linea.contains("*") || linea.contains("/")) {
            
            if (linea.matches(".[\\+\\-\\/]\\s*;")) {
                throw new Exception("Excepción: La operación está incompleta. Línea " + numLinea);
            }
        }

      
        if ((linea.contains("+") || linea.contains("-")) && !linea.contains("=")) {
            throw new Exception("Excepción: Las operaciones no tienen asignación. Línea " + numLinea);
        }
        
        System.out.println("Línea " + numLinea + " validada: [OK]");
    }  
}
