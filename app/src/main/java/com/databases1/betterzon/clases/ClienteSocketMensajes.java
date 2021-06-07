package com.databases1.betterzon.clases;

import android.content.Context;
import android.content.SharedPreferences;
import com.databases1.betterzon.ListAdapterChat;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PrivateKey;
import static android.content.Context.MODE_PRIVATE;

public class ClienteSocketMensajes implements Runnable{

    // Atributos
    private static int PUERTO = 1111; // TODO: Debes colocar el puerto que vayas a usar
    private EmpaquetadoDeMensaje mensajeRecibido;
    private String ip, json;
    private Socket socket, socketDeDestino;
    private ServerSocket serverDeEscucha;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;
    private SharedPreferences llavesMiaPreferences;
    private Context c;
    private Gson gson = new Gson();
    private PrivateKey llave_privada;


    public ClienteSocketMensajes(String ip) {
        this.ip = ip;
    } // Constructor


    public void enviarMensaje(EmpaquetadoDeMensaje mensajeParaEnviar){

        try {

            socket = new Socket(ip, PUERTO);

            // El flujo de datos de salida va a circular por el puente virtual del socket
            salida = new ObjectOutputStream(socket.getOutputStream());

            // servidor recibe este mensaje.
            salida.writeObject(mensajeParaEnviar);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            socket.close();
            salida.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    } // enviarMensaje()


    public void inicializarHilo(Context c) {

        this.c = c;

        Thread hiloParaCliente = new Thread(this);
        hiloParaCliente.start();

    } // InicializarHilo()


    @Override
    public void run() {

        try {

            serverDeEscucha = new ServerSocket(PUERTO + 10);

            while (true){

                // espera una petición en el puerto especificado.
                // Al recibirla establece una conexión con el otro programa.
                socketDeDestino = serverDeEscucha.accept();

                // El flujo de datos de entrada va a circular por el puente virtual del socket
                entrada = new ObjectInputStream(socketDeDestino.getInputStream());

                // Guarda el objeto que obtenga como EmpaquetadoDeMensaje
                mensajeRecibido = (EmpaquetadoDeMensaje) entrada.readObject();

                // TODO: 2021-06-05 Añadir el mensaje a base de datos

                // Obtener mi llave privada
                llavesMiaPreferences = c.getSharedPreferences("llaves_propias", MODE_PRIVATE);
                json = llavesMiaPreferences.getString("llave_privada", null);
                llave_privada = gson.fromJson(json, PrivateKey.class);

                // Guardar mensaje localmente
                ListAdapterChat.addMensaje(new Mensaje(mensajeRecibido, llave_privada, false));

                entrada.close();
                socketDeDestino.close();

            } // while

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    } // run()

} // Class
