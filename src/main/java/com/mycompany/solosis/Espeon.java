/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.solosis;

/**
 *
 * @author Heber
 */
public class Espeon extends Operacion {
    
    private double valor;
     
    public Espeon (double valor){
        this.valor=valor;
    }
     
    public double getvalor(){
        return valor;
    }
    
    @Override
    public Operacion suma(Operacion otro) {
        Espeon o = (Espeon) otro;
        return new Espeon(this.valor + o.valor);
    }

    @Override
    public Operacion resta(Operacion otro) {
        Espeon o = (Espeon) otro;
        return new Espeon(this.valor - o.valor);
    }

    @Override
    public Operacion multiplicacion(Operacion otro) {
        Espeon o = (Espeon) otro;
        return new Espeon(this.valor * o.valor);
    }

    @Override
    public Operacion division(Operacion otro) {
        Espeon o = (Espeon) otro;
        if (o.valor == 0) {
            throw new ArithmeticException("División entre cero");
        }
        return new Espeon(this.valor / o.valor);
    }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }
}
