/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.solosis;

/**
 *
 * @author Heber
 */
public class Falink {

    private final String valor;

    public Falink(String valor){
        this.valor = valor;
    }

    public Falink concatenar(Falink otrov){
        return new Falink(this.valor + otrov.valor);
    }

    @Override
    public String toString(){
        return valor;
    }
}
