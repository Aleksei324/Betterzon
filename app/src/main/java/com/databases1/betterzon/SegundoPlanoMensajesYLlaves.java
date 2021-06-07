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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class SegundoPlanoMensajesYLlaves extends Service implements Runnable {

    // Atributos
    private ClienteSocketMensajes clienteM;
    private ClienteSocketLlaves clienteL;
    private SharedPreferences ipServidorPreference, ipPreference;
    private SharedPreferences.Editor editor1;
    private Gson gson;
    private String ipMia, json, llave, ipServidorFinal;
    private URL whatismyip;
    private BufferedReader in;
    private byte[] ipServidor;


    @Override
    public void onCreate(){
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int idProcess){

        ipServidorPreference = getSharedPreferences("servidor", MODE_PRIVATE);
        ipPreference = getSharedPreferences("IPs", MODE_PRIVATE);
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

        // Obtener ip
        while (ipMia == null){
            try {
                // Usa un servicio de una pagina para eso
                whatismyip = new URL("https://ipv4.icanhazip.com/");
                in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
                ipMia = in.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // TODO: 2021-06-06 Guardar la ip en la base de datos

        // Guardar ip propia localmente
        editor1 = ipPreference.edit();
        editor1.putString("mia", ipMia);
        editor1.apply();

        // ip del servidor
        json = ipServidorPreference.getString("ip", null);
        ipServidor = gson.fromJson(json, byte[].class);
        ipServidorFinal = EncriptadoAES.decifrar(ipServidor, llave);

        if (ipServidorFinal != null){
            clienteM = new ClienteSocketMensajes(ipServidorFinal);
            clienteL = new ClienteSocketLlaves(ipServidorFinal);

            // Inicializar MAS hilos para que pueda recibir mensajes y llaves
            clienteM.inicializarHilo(this);
            clienteL.inicializarHilo(this);
        }
    }
}
