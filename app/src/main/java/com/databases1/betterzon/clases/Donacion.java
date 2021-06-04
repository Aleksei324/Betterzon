package com.databases1.betterzon.clases;

public class Donacion {

    // ATRIBUTOS
    private Persona p;
    private Material m;

    public Donacion(Persona p, Material m) {
        this.p = p;
        this.m = m;
    }

    public Persona getP() {
        return p;
    }

    public void setP(Persona p) {
        this.p = p;
    }

    public Material getM() {
        return m;
    }

    public void setM(Material m) {
        this.m = m;
    }
}
