package com.databases1.betterzon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    } // onCreate

    protected void onResume() {
        super.onResume();

        Thread hilo1 = new Thread() {
            @Override
            public void run() {
                esperarYCambiarActivity();
            } // run

        }; // hilo

        hilo1.start();

    } // onResume

    public void esperarYCambiarActivity(){

        // TODO: 2021-05-06 Si la app no tiene las credenciales de login, abrir la activity de registro.
        if (true){

            try {
                // Espera un segundo
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // Cambia la activity actual a RegisterActivity
                startActivity(new Intent(this, RegisterActivity.class));
            }

        } else {
            // Cambia la activity actual a HomeActivity
            startActivity(new Intent(this, HomeActivity.class));
        }

        this.finish();

    } // esperarYCambiarActivity

} // clase