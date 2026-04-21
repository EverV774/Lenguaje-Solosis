package com.mycompany.solosis;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author daneysha
 */

public abstract class Operacion {

    public abstract Operacion suma(Operacion otro);

    public abstract Operacion resta(Operacion otro);

    public abstract Operacion multiplicacion(Operacion otro);

    public abstract Operacion division(Operacion otro);
}
