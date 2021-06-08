package com.databases1.betterzon;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.databases1.betterzon.clases.EncriptadoAES;
import com.databases1.betterzon.clases.Persona;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity implements Runnable{

    // Atributos
    private SharedPreferences credUsuarioPreferences, SQLPreferences;
    private SharedPreferences.Editor editor1;
    private Gson gson;
    private String json, llave, SQLPasswordFinal, SQLusuarioFinal, SQLipFinal, instruccion, nombre, password, tipo, ipMia;
    private int cedula, celular, inhabilitado;
    private byte[] SQLPassword, SQLusuario, SQLip;
    private Persona personaCred;
    private ResultSet personaResultante;
    private EditText campoCedula, campoPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SQLPreferences = getSharedPreferences("SQL", MODE_PRIVATE);
        credUsuarioPreferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        campoCedula = findViewById(R.id.editTextNumberCedulaLogin);
        campoPassword = findViewById(R.id.editTextTextPasswordLogin);
        gson = new Gson();
        llave = "Escribe tu contraseña para la llave aqui"; // TODO: 2021-06-07 this
    }

    public void cambiarARegisterActivity(View v){

        // Cambia la activity actual a RegisterActivity
        startActivity(new Intent(this, RegisterActivity.class));

        this.finish();

    } // cambiarActivity

    public void cambiarAHomeActivity(View v){

        Thread hiloParaServicio = new Thread(this);
        hiloParaServicio.start();

    } // cambiarActivity

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

                if (cedula == Integer.parseInt(campoCedula.getText().toString()) &&
                        password.equals(campoPassword.getText().toString()) && inhabilitado == 0) {

                    ipMia = MainActivity.getIP();

                    editor1 = credUsuarioPreferences.edit();
                    json = gson.toJson(new Persona(cedula, celular, nombre, password, tipo, ipMia));
                    editor1.putString("persona", json);
                    editor1.apply();

                    // Cambia la activity actual a HomeActivity
                    startActivity(new Intent(this, HomeActivity.class));
                    this.finish();
                }
                else {
                    this.runOnUiThread(() -> {
                        Toast.makeText(this, "No fue posible ingresar a la cuenta", Toast.LENGTH_SHORT).show();
                    });
                }
            }
            else {
                this.runOnUiThread(() -> {
                    Toast.makeText(this, "No existe la cuenta", Toast.LENGTH_SHORT).show();
                });
            }

        }catch (SQLException | ClassNotFoundException throwables){
            throwables.printStackTrace();
            this.runOnUiThread(() -> {
                Toast.makeText(this, "No fue posible ingresar a la cuenta", Toast.LENGTH_SHORT).show();
            });
        }
    }
}