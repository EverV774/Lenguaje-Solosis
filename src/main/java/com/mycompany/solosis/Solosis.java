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

        Operacion a = new gabite(13);
        Operacion b = new gabite(9);

        Operacion resultado = a.suma(b);

        System.out.println("Resultado: " + resultado);
    }
}
