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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterActivity extends AppCompatActivity implements Runnable {

    // Atributos
    private SharedPreferences SQLPreference, ipPreference, credencialesPreference;
    private SharedPreferences.Editor editor1;
    private String json, SQLPasswordFinal, SQLusuarioFinal, SQLipFinal, llave, instruccion, ipMia;
    private byte[] SQLPassword, SQLusuario, SQLip;
    private EditText cedula, nombre, celular, password;
    private Gson gson;
    private URL whatismyip;
    private BufferedReader in;
    private ResultSet personaResultante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SQLPreference = getSharedPreferences("SQL", MODE_PRIVATE);
        ipPreference = getSharedPreferences("IPs", MODE_PRIVATE);
        credencialesPreference = getSharedPreferences("credenciales", MODE_PRIVATE);
        cedula = findViewById(R.id.editTextNumberCedulaRegistro);
        nombre = findViewById(R.id.editTextTextPersonNombreRegistro);
        celular = findViewById(R.id.editTextNumberCelularRegistro);
        password = findViewById(R.id.editTextTextPasswordRegistro);
        gson = new Gson();
        llave = "Escribe tu contraseña para la llave aqui"; // TODO: 2021-06-07 this

    } // onCreate

    public void cambiarALoginActivity(View v){

        // Cambia la activity actual a LoginActivity
        startActivity(new Intent(this, LoginActivity.class));

        this.finish();

    } // cambiarActivity

    public void cambiarAHomeActivity(View v){

        Thread hiloParaServicio = new Thread(this);
        hiloParaServicio.start();

    } // cambiarActivity

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

        // Guardar ip propia localmente
        editor1 = ipPreference.edit();
        editor1.putString("mia", ipMia);
        editor1.apply();

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

            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection("jdbc:" + SQLipFinal +
                            "?verifyServerCertificate=false", SQLusuarioFinal, SQLPasswordFinal);

            Statement st = conn.createStatement();

            instruccion = "SELECT cedula FROM PERSONAS WHERE cedula = "+ cedula.getText().toString() + ";";

            personaResultante = st.executeQuery(instruccion);

            // Si existe una fila (lo que significa que se encontro la cedula)...
            if (personaResultante.next()) {

                this.runOnUiThread(() -> {
                    Toast.makeText(this, "Este usuario ya existe", Toast.LENGTH_SHORT).show();
                });
            }
            else{

                instruccion = "INSERT INTO PERSONAS(cedula,nombre,contraseña,celular,tipo,inhabilitado,direccionIP)" +
                        "values('" + cedula.getText() + "', '" + nombre.getText() + "', '" + password.getText() +
                        "', '" + celular.getText() + "','Cliente',0,'" + ipMia + "');";

                st.executeUpdate(instruccion);

                editor1 = credencialesPreference.edit();
                editor1.putBoolean("existe", true);
                editor1.apply();

                editor1 = credencialesPreference.edit();
                json = gson.toJson(new Persona(Integer.parseInt(cedula.getText().toString()),
                        Integer.parseInt(celular.getText().toString()),
                        nombre.getText().toString(), password.getText().toString(),
                        "Cliente", ipMia));

                editor1.putString("persona", json);
                editor1.apply();

                // Cambia la activity actual a HomeActivity
                startActivity(new Intent(this, HomeActivity.class));
                this.finish();
            }

            st.close();
            conn.close();

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            this.runOnUiThread(() -> {
                Toast.makeText(this, "No fue posible crear la cuenta", Toast.LENGTH_SHORT).show();
            });
        }

    } // run

} // class