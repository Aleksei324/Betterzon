package com.databases1.betterzon.clases;

import java.io.Serializable;

public class EmpaquetadoDeMensaje implements Serializable{

    // Atributos
    private byte[] texto, destinatarioIP, imagen;


    public EmpaquetadoDeMensaje(byte[] destinatarioIP, byte[] texto, byte[] imagen) {

        this.destinatarioIP = destinatarioIP;
        this.texto = texto;
        this.imagen = imagen;

    } // Constructor


    public byte[] getTexto() {
        return texto;
    }

    public byte[] getDestinatarioIP() {
        return destinatarioIP;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setTexto(byte[] texto) {
        this.texto = texto;
    }

    public void setDestinatarioIP(byte[] destinatarioIP) {
        this.destinatarioIP = destinatarioIP;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

} // Clase