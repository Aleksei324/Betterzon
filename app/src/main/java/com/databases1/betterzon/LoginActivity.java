package com.databases1.betterzon;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void cambiarARegisterActivity(View v){

        // Cambia la activity actual a RegisterActivity
        startActivity(new Intent(this, RegisterActivity.class));

        this.finish();

    } // cambiarActivity

    public void cambiarAHomeActivity(View v){

        // Cambia la activity actual a HomeActivity
        startActivity(new Intent(this, HomeActivity.class));

        this.finish();

    } // cambiarActivity
}