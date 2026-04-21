package com.mycompany.solosis;

public abstract class Operacion {

    public abstract Operacion suma(Operacion otro);

    public abstract Operacion resta(Operacion otro);

    public abstract Operacion multiplicacion(Operacion otro);

    public abstract Operacion division(Operacion otro);
}
