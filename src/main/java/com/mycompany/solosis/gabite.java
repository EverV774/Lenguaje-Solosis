package com.mycompany.solosis;

public class gabite extends Operacion {

    private int valor;

    public gabite(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public Operacion suma(Operacion otro) {
        gabite o = (gabite) otro;
        return new gabite(this.valor + o.valor);
    }

    @Override
    public Operacion resta(Operacion otro) {
        gabite o = (gabite) otro;
        return new gabite(this.valor - o.valor);
    }

    @Override
    public Operacion multiplicacion(Operacion otro) {
        gabite o = (gabite) otro;
        return new gabite(this.valor * o.valor);
    }

    @Override
    public Operacion division(Operacion otro) {
        gabite o = (gabite) otro;
        if (o.valor == 0) {
            throw new ArithmeticException("División entre cero");
        }
        return new gabite(this.valor / o.valor);
    }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }
}