package com.databases1.betterzon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    // Crear fragmentos y atributos
    private PrimerFragmento materiales;
    private SegundoFragmento proyecto;
    private ChatFragmento chat;
    private TercerFragmento donaciones;
    private SharedPreferences estados;
    private String estado_proyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        materiales = new PrimerFragmento();
        proyecto = new SegundoFragmento();
        chat = new ChatFragmento();
        donaciones = new TercerFragmento();
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(ISlistener);

        // cargar el fragmento de materiales al iniciar la activity
        loadFragment(materiales);

        // Obtener el estado del proyecto
        estados = getSharedPreferences("estados", MODE_PRIVATE);
        estado_proyecto = estados.getString("proyecto", "None");

        // Si el chat esta inicializado, iniciar los servicios en segundo plano
        if (estado_proyecto.equals("Chat")){

            startService(new Intent(this, SegundoPlanoMensajesYLlaves.class));
        }
    }

    // override onNavItemSelected
    private final BottomNavigationView.OnNavigationItemSelectedListener ISlistener = item -> {
        switch (item.getItemId()){

            case R.id.primer_fragmento:
                loadFragment(materiales);
                return true;

            case R.id.segundo_fragmento:
                switch (estado_proyecto){
                    case "None":
                        loadFragment(proyecto);
                        break;
                    case "Chat":
                        loadFragment(chat);
                        break;
                }
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