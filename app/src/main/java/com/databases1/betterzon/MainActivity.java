package com.databases1.betterzon;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.databases1.betterzon.clases.EncriptadoAES;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    // Atributos
    private SharedPreferences credUsuarioPreferences, servidorPreferences, SQLPreferences;
    private SharedPreferences.Editor editor1;
    private Gson gson;
    private String json, llave;

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
        json = gson.toJson(EncriptadoAES.encriptar("IP SERVIDOR",
                llave)); // TODO: 2021-06-06 Coloca la ip del servidor de mensajes aqui

        editor1.putString("ip", json);
        editor1.apply();

        // Guardar la ip de mySQL
        editor1 = SQLPreferences.edit();
        json = gson.toJson(EncriptadoAES.encriptar(
                "URI SQL",
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

            // TODO: 2021-06-08 Confirmar que sean validas

            // servicio en segundo plano
            startService(new Intent(this, SegundoPlanoMensajesYLlaves.class));

            // Cambia la activity actual a RegisterActivity
            startActivity(new Intent(this, HomeActivity.class));
        }
        else {
            // Cambia la activity actual a HomeActivity
            startActivity(new Intent(this, RegisterActivity.class));
        }

        this.finish();

    } // esperarYCambiarActivity

} // clase