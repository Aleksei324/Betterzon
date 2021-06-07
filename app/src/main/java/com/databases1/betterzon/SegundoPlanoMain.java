package com.databases1.betterzon;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.databases1.betterzon.clases.EncriptadoAES;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SegundoPlanoMain extends Service implements Runnable {

    // Atributos
    private SharedPreferences SQLPreference;
    private String json, SQLPasswordFinal, SQLusuarioFinal, SQLipFinal, llave;
    private byte[] SQLPassword, SQLusuario, SQLip;
    private Gson gson;


    @Override
    public void onCreate(){
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int idProcess){

        SQLPreference = getSharedPreferences("SQL", MODE_PRIVATE);
        gson = new Gson();
        llave = "Escribe tu contraseña para la llave aqui"; // TODO: 2021-06-07 this

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

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Obtener ip SQL
        json = SQLPreference.getString("ip", "");
        SQLip = gson.fromJson(json, byte[].class);
        SQLipFinal = EncriptadoAES.decifrar(SQLip, llave);

        // Obtener usuario SQL
        json = SQLPreference.getString("user", null);
        SQLusuario = gson.fromJson(json, byte[].class);
        SQLusuarioFinal = EncriptadoAES.decifrar(SQLusuario, llave);

        // Obtener contraseña SQL
        json = SQLPreference.getString("password", null);
        SQLPassword = gson.fromJson(json, byte[].class);
        SQLPasswordFinal = EncriptadoAES.decifrar(SQLPassword, llave);

        try {

            Connection conn = DriverManager.getConnection(SQLipFinal +
                    "?useUnicode=true&useJDBCCompliantTimezoneShift=true&" +
                    "useLegacyDateTimeCode=false&serverTimezone=UTC",
                    SQLusuarioFinal, SQLPasswordFinal);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
