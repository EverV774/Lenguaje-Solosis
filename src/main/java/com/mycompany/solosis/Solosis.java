/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.solosis;

/**
 *
 * @author Heber
 */
public class Solosis {
    
    public static void main(String[] args) {

       //Operacion a = new gabite(13);
       // Operacion b = new gabite(9);

        //Operacion resultado = a.suma(b);
//
        //System.out.println("Resultado: " + resultado);
        
        Steelix validador = new Steelix();
    
 

    String codigo = "valor string = 6;"; 
    int linea = 1;
    String codigo2 = "int Casa = 10"; 
    int linea2 = 2;

    try {
        validador.validarLinea(codigo, linea);
        
        
        validador.validarLinea(codigo2, linea2);
       
        Operacion a = new gabite(6); 
        System.out.println("Resultado: " + a);
        
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    }
}
