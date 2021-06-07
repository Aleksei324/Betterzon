package com.databases1.betterzon.clases;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static android.content.Context.MODE_PRIVATE;

public class ClienteSocketLlaves implements Runnable{

    // Atributos
    private static int PUERTO = 2222; // TODO: Debes colocar el puerto que vayas a usar
    private EmpaquetadoDeLlaves mensajeRecibido;
    private String ip, json;
    private Socket socket, socketDeDestino;
    private ServerSocket serverDeEscucha;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;
    private SharedPreferences llaveDelOtroPreference;
    private SharedPreferences.Editor editor1;
    private Context c;
    private Gson gson = new Gson();


    public ClienteSocketLlaves(String ip) {
        this.ip = ip;
    } // Constructor


    public void enviarLlaves(EmpaquetadoDeLlaves llaveParaEnviar){

        try {

            socket = new Socket(ip, PUERTO);

            // El flujo de datos de salida va a circular por el puente virtual del socket
            salida = new ObjectOutputStream(socket.getOutputStream());

            // servidor recibe este mensaje.
            salida.writeObject(llaveParaEnviar);

            socket.close();
            salida.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
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

            // espera una petición en el puerto especificado.
            // Al recibirla establece una conexión con el otro programa.
            socketDeDestino = serverDeEscucha.accept();

            // El flujo de datos de entrada va a circular por el puente virtual del socket
            entrada = new ObjectInputStream(socketDeDestino.getInputStream());

            // Guarda el objeto que obtenga como EmpaquetadoDeLlaves
            mensajeRecibido = (EmpaquetadoDeLlaves) entrada.readObject();

            // Guardar la llave publica del otro
            llaveDelOtroPreference = c.getSharedPreferences("llaves_otro", MODE_PRIVATE);
            editor1 = llaveDelOtroPreference.edit();
            json = gson.toJson(mensajeRecibido.getLlaveRemitente());
            editor1.putString("llave_publica", json);
            editor1.apply();

            entrada.close();
            socketDeDestino.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    } // run()

} // Class
