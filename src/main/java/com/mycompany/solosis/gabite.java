package com.mycompany.solosis;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author daneysha
 */

public class gabite extends Operacion {
    private int valor;

    public gabite(int valor) {
        this.valor = valor;
    }

    @Override
    public Object getValor() {
        return this.valor;
    }

    @Override
    public Operacion suma(Operacion otro) {
        return new gabite(this.valor + (int) otro.getValor());
    }

    @Override
    public Operacion resta(Operacion otro) {
        return new gabite(this.valor - (int) otro.getValor());
    }

    @Override
    public Operacion multiplicacion(Operacion otro) {
        return new gabite(this.valor * (int) otro.getValor());
    }

    @Override
    public Operacion division(Operacion otro) {
        if ((int) otro.getValor() == 0) throw new ArithmeticException("División por cero");
        return new gabite(this.valor / (int) otro.getValor());
    }

    @Override
    public String toString() { return String.valueOf(valor); }
}