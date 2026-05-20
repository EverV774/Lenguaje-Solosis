/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.solosis;

/**
 *
 * @author Heber
 */
public class Falink extends Operacion {
    private String valor;

    public Falink(String valor) {
                if (valor.length() > 30){
        throw new IllegalArgumentException(
        "Falink solo acepta 30 caracteres"
        );
        }
        this.valor = valor;
    }

    @Override
    public Object getValor() {
        return this.valor;
    }

    @Override
    public Operacion suma(Operacion otro) {
        return new Falink(this.valor + otro.getValor().toString());
    }
    
    @Override
    public Operacion resta(Operacion otro) {
        throw new UnsupportedOperationException("No se puede restar en un Falink");
    }

    @Override
    public Operacion multiplicacion(Operacion otro) {
        throw new UnsupportedOperationException("No se puede multiplicar en un Falink");
    }

    @Override
    public Operacion division(Operacion otro) {
        throw new UnsupportedOperationException("No se puede dividir en un Falink");
    }

    @Override
    public String toString() { return valor; }
}
