package com.databases1.betterzon;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.databases1.betterzon.clases.EncriptadoAES;
import com.databases1.betterzon.clases.Persona;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity implements Runnable{

    // Atributos
    private SharedPreferences credUsuarioPreferences, servidorPreferences, SQLPreferences;
    private SharedPreferences.Editor editor1;
    private Gson gson;
    private String json, llave, SQLPasswordFinal, SQLusuarioFinal, SQLipFinal, instruccion, nombre, password, tipo;
    private int cedula, celular, inhabilitado;
    private byte[] SQLPassword, SQLusuario, SQLip;
    private Persona personaCred;
    private ResultSet personaResultante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        credUsuarioPreferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        servidorPreferences = getSharedPreferences("servidor", MODE_PRIVATE);
        SQLPreferences = getSharedPreferences("SQL", MODE_PRIVATE);
        gson = new Gson();
        llave = "Escribe tu contraseña para la llave aqui"; // TODO: 2021-06-07 this
    } // onCreate

    protected void onResume() {
        super.onResume();

        Thread hilo1 = new Thread() {

            @Override
            public void run() {
                esperarInicializarTodoYCambiarActivity();
            } // run

        }; // hilo

        hilo1.start();

    } // onResume

    public void esperarInicializarTodoYCambiarActivity(){

        try {
            // Espera un segundo
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Guardar la ip del servidor
        editor1 = servidorPreferences.edit();
        json = gson.toJson(EncriptadoAES.encriptar("IP",
                llave)); // TODO: 2021-06-06 Coloca la ip del servidor de mensajes aqui

        editor1.putString("ip", json);
        editor1.apply();

        // Guardar la ip de mySQL
        editor1 = SQLPreferences.edit();
        json = gson.toJson(EncriptadoAES.encriptar(
                "URI",
                llave)); // TODO: 2021-06-07 Coloca el URI de la base de datos

        editor1.putString("ip", json);
        editor1.apply();

        // Guardar el usuario de SQL
        editor1 = SQLPreferences.edit();
        json = gson.toJson(EncriptadoAES.encriptar(
                "USUARIO", // TODO: 2021-06-06 Escribe el usuario de SQL
                llave));

        editor1.putString("user", json);
        editor1.apply();

        // Guardar la contraseña de SQL
        editor1 = SQLPreferences.edit();
        json = gson.toJson(EncriptadoAES.encriptar(
                "CONTRASEÑA", // TODO: 2021-06-06 Escribe la contraseña de SQL
                llave));

        editor1.putString("password", json);
        editor1.apply();

        if (credUsuarioPreferences.getBoolean("existe", false)){

            Thread hiloParaServicio = new Thread(this);
            hiloParaServicio.start();
        }
        else {
            // Cambia la activity actual a HomeActivity
            startActivity(new Intent(this, RegisterActivity.class));
            this.finish();
        }

    } // esperarYCambiarActivity

    @Override
    public void run() {

        // Obtener ip SQL
        json = SQLPreferences.getString("ip", "");
        SQLip = gson.fromJson(json, byte[].class);
        SQLipFinal = EncriptadoAES.decifrar(SQLip, llave);

        // Obtener usuario SQL
        json = SQLPreferences.getString("user", null);
        SQLusuario = gson.fromJson(json, byte[].class);
        SQLusuarioFinal = EncriptadoAES.decifrar(SQLusuario, llave);

        // Obtener contraseña SQL
        json = SQLPreferences.getString("password", null);
        SQLPassword = gson.fromJson(json, byte[].class);
        SQLPasswordFinal = EncriptadoAES.decifrar(SQLPassword, llave);

        // Credenciales de la persona
        json = credUsuarioPreferences.getString("persona", null);
        personaCred = gson.fromJson(json, Persona.class);

        try {

            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection("jdbc:" + SQLipFinal +
                    "?verifyServerCertificate=false", SQLusuarioFinal, SQLPasswordFinal);

            Statement st = conn.createStatement();

            instruccion = "SELECT * FROM PERSONAS WHERE cedula = " + personaCred.getCedula() + ";";

            personaResultante = st.executeQuery(instruccion);

            if (personaResultante.next()){

                cedula = personaResultante.getInt("cedula");
                nombre = personaResultante.getString("nombre");
                celular = personaResultante.getInt("celular");
                password = personaResultante.getString("contraseña");
                tipo = personaResultante.getString("tipo");
                inhabilitado = personaResultante.getInt("inhabilitado");

                if (cedula == personaCred.getCedula() && nombre.equals(personaCred.getNombre()) &&
                        celular == personaCred.getCelular() && password.equals(personaCred.getPassword()) &&
                        tipo.equals(personaCred.getTipo()) && inhabilitado == 0) {

                    // Cambia la activity actual a RegisterActivity
                    startActivity(new Intent(this, HomeActivity.class));
                }
                else {
                    editor1 = credUsuarioPreferences.edit();
                    editor1.putBoolean("existe", false);
                    editor1.apply();

                    // Cambia la activity actual a HomeActivity
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                this.finish();
            }
            else {
                editor1 = credUsuarioPreferences.edit();
                editor1.putBoolean("existe", false);
                editor1.apply();

                // Cambia la activity actual a HomeActivity
                startActivity(new Intent(this, RegisterActivity.class));
                this.finish();
            }

        }catch (SQLException | ClassNotFoundException throwables){
            throwables.printStackTrace();
        }
    }

    public static String getIP(){

        String ipPropia = null;
        URL webService;
        BufferedReader readerIP = null;

        while (ipPropia == null){
            try {
                // Usa un servicio de una pagina para eso
                webService = new URL("https://ipv4.icanhazip.com/");
                readerIP = new BufferedReader(new InputStreamReader(webService.openStream()));
                ipPropia = readerIP.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (readerIP != null){
                    readerIP.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ipPropia;
    }

} // clase