package com.databases1.betterzon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    // crear componentes
    Spinner spinner1;

    // crear fragmentos
    PrimerFragmento materiales = new PrimerFragmento();
    SegundoFragmento proyecto = new SegundoFragmento();
    TercerFragmento donaciones = new TercerFragmento();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(ISlistener);

        // cargar el fragmento de proyecto al iniciar la activity
        loadFragment(proyecto);
        navigation.setSelectedItemId(R.id.segundo_fragmento);
    }

    protected void onStart() {
        super.onStart();

        spinner1 = findViewById(R.id.spinnerCrearProyecto);
        String [] opciones = {"Vivienda nueva", "Remodelación"};
        spinner1.setAdapter( new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, opciones) );

    } // onStart

    // override onNavItemSelected
    private final BottomNavigationView.OnNavigationItemSelectedListener ISlistener = item -> {
        switch (item.getItemId()){
            case R.id.primer_fragmento:
                loadFragment(materiales);
                return true;
            case R.id.segundo_fragmento:
                loadFragment(proyecto);
                return true;
            case R.id.tercer_fragmento:
                loadFragment(donaciones);
                return true;
        }
        return false;

    };
    public void loadFragment(Fragment f){
        FragmentTransaction transaccion = getSupportFragmentManager().beginTransaction();
        transaccion.replace(R.id.frame, f);
        transaccion.commit();
    }
} // class