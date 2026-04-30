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

    public Espeon(double valor) {
        this.valor = valor;
    }

    @Override
    public Object getValor() {
        return this.valor;
    }

    @Override
    public Operacion suma(Operacion otro) {
        return new Espeon(this.valor + ((Number) otro.getValor()).doubleValue());
    }

    @Override
    public Operacion resta(Operacion otro) {
        return new Espeon(this.valor - ((Number) otro.getValor()).doubleValue());
    }

    @Override
    public Operacion multiplicacion(Operacion otro) {
        double valorOtro = ((Number) otro.getValor()).doubleValue();
        return new Espeon(this.valor * valorOtro);
    }

    @Override
    public Operacion division(Operacion otro) {
        double divisor = ((Number) otro.getValor()).doubleValue();
        if (divisor == 0) throw new ArithmeticException("División por cero");
        return new Espeon(this.valor / divisor);
    }

    @Override
    public String toString() { return String.valueOf(valor); }
}