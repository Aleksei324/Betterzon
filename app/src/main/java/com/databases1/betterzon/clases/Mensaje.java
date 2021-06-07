package com.databases1.betterzon.clases;

import android.graphics.Bitmap;
import java.security.PrivateKey;

public class Mensaje {

    // atributos
    private String texto;
    private Bitmap imagen;
    private boolean mensajePropio;


    // constructor
    public Mensaje(EmpaquetadoDeMensaje m, PrivateKey l, boolean mensajePropio) {
        this.texto = EncriptadoRSA.decifrarTexto(m.getTexto(), l);
        this.imagen = EncriptadoRSA.decifrarImagen(m.getImagen(), l);
        this.mensajePropio = mensajePropio;
    }

    // constructor
    public Mensaje(String mensaje, Bitmap imagen, boolean mensajePropio) {
        this.texto = mensaje;
        this.imagen = imagen;
        this.mensajePropio = mensajePropio;
    }


    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public boolean isMensajePropio() {
        return mensajePropio;
    }

    public void setMensajePropio(boolean mensajePropio) {
        this.mensajePropio = mensajePropio;
    }
}
