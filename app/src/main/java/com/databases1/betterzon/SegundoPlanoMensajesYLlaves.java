package com.databases1.betterzon;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.databases1.betterzon.clases.ClienteSocketLlaves;
import com.databases1.betterzon.clases.ClienteSocketMensajes;
import com.databases1.betterzon.clases.EncriptadoAES;
import com.google.gson.Gson;

public class SegundoPlanoMensajesYLlaves extends Service implements Runnable {

    // Atributos
    private ClienteSocketMensajes clienteM;
    private ClienteSocketLlaves clienteL;
    private SharedPreferences ipServidorPreference;
    private Gson gson;
    private String json, llave, ipServidorFinal;
    private byte[] ipServidor;


    @Override
    public void onCreate(){
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int idProcess){

        ipServidorPreference = getSharedPreferences("servidor", MODE_PRIVATE);
        llave = "Escribe tu contraseña para la llave aqui"; // TODO: 2021-06-07 this
        gson = new Gson();

        Thread hiloParaServicio = new Thread(this);
        hiloParaServicio.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {

        // ip del servidor
        json = ipServidorPreference.getString("ip", null);
        ipServidor = gson.fromJson(json, byte[].class);
        ipServidorFinal = EncriptadoAES.decifrar(ipServidor, llave);

        clienteM = new ClienteSocketMensajes(ipServidorFinal);
        clienteL = new ClienteSocketLlaves(ipServidorFinal);

        // Inicializar MAS hilos para que pueda recibir mensajes y llaves
        clienteM.inicializarHilo(this);
        clienteL.inicializarHilo(this);
    }
}
