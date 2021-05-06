package com.databases1.betterzon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    } // onCreate

    public void cambiarALoginActivity(View v){

        // Cambia la activity actual a LoginActivity
        startActivity(new Intent(this, LoginActivity.class));

        this.finish();

    } // cambiarActivity

    public void cambiarAHomeActivity(View v){

        // Cambia la activity actual a HomeActivity
        startActivity(new Intent(this, HomeActivity.class));

        this.finish();

    } // cambiarActivity

} // class