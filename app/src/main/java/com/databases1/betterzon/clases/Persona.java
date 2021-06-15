package com.databases1.betterzon.clases;

public class Persona {

    // Atributo
    private Long cedula, celular;
    private String nombre, password, tipo, ip;
    private boolean inhabilitado = false;

    public Persona(Long cedula, Long celular, String nombre, String password, String tipo, String ip) {
        this.cedula = cedula;
        this.celular = celular;
        this.nombre = nombre;
        this.password = password;
        this.tipo = tipo;
        this.ip = ip;
    }

    public Long getCedula() {
        return cedula;
    }

    public void setCedula(Long cedula) {
        this.cedula = cedula;
    }

    public Long getCelular() {
        return celular;
    }

    public void setCelular(Long celular) {
        this.celular = celular;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isInhabilitado() {
        return inhabilitado;
    }

    public void setInhabilitado(boolean inhabilitado) {
        this.inhabilitado = inhabilitado;
    }

} // class
