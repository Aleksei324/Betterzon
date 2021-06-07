package com.databases1.betterzon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.databases1.betterzon.clases.ClienteSocketLlaves;
import com.databases1.betterzon.clases.EmpaquetadoDeLlaves;
import com.databases1.betterzon.clases.EncriptadoAES;
import com.databases1.betterzon.clases.EncriptadoRSA;
import com.google.gson.Gson;
import java.security.KeyPair;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SegundoFragmento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SegundoFragmento extends Fragment {

    // Atributos
    ChatFragmento frag = new ChatFragmento();
    Spinner spinner1;
    Button boton1;
    ClienteSocketLlaves clienteL;
    SharedPreferences datosServidorPreference, estadoPreference, llaveGuardadaPreference, ipPreference;
    SharedPreferences.Editor editor1;
    String ipServidor, passwordServidor, json, ipSuya;
    Gson gson;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SegundoFragmento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SegundoFragmento.
     */
    // TODO: Rename and change types and number of parameters
    public static SegundoFragmento newInstance(String param1, String param2) {
        SegundoFragmento fragment = new SegundoFragmento();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_segundo_fragmento, container, false);

        spinner1 = v.findViewById(R.id.spinnerCrearProyecto);
        boton1 = v.findViewById(R.id.buttonCrearProyecto);
        datosServidorPreference = requireActivity().getSharedPreferences("servidor", MODE_PRIVATE);
        estadoPreference = requireActivity().getSharedPreferences("estados", MODE_PRIVATE);
        llaveGuardadaPreference = requireActivity().getSharedPreferences("llaves_propias", MODE_PRIVATE);
        ipPreference = requireActivity().getSharedPreferences("IPs", MODE_PRIVATE);
        ipServidor = datosServidorPreference.getString("ip", null);
        passwordServidor = datosServidorPreference.getString("password", null);
        clienteL = new ClienteSocketLlaves(ipServidor);
        gson = new Gson();

        String [] opciones = {"Vivienda nueva", "Remodelación"};
        spinner1.setAdapter(new ArrayAdapter<>(
                v.getContext(), android.R.layout.simple_spinner_dropdown_item, opciones) );

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: 2021-06-06 Confirmar que los datos son validos y enviarlos

                // Guarda el nuevo estado para que siempre se ejecute ChatFragment
                editor1 = estadoPreference.edit();
                editor1.putString("proyecto", "Chat");
                editor1.apply();

                // servicio en segundo plano
                requireActivity().startService(new Intent(requireActivity(), SegundoPlanoMensajesYLlaves.class));

                // TODO: 2021-06-06 obtener ip de la base de datos = ipSuya

                // Guardar ip del otro
                editor1 = ipPreference.edit();
                editor1.putString("del_otro", ipSuya);
                editor1.apply();

                KeyPair llavesCreadas = EncriptadoRSA.crearLlaves();

                // Guardar la llave privada propia
                editor1 = llaveGuardadaPreference.edit();
                json = gson.toJson(llavesCreadas.getPrivate());
                editor1.putString("llave_privada", json);
                editor1.apply();

                EmpaquetadoDeLlaves llaveEmpaquetada = new EmpaquetadoDeLlaves(llavesCreadas.getPublic(),
                        EncriptadoAES.encriptar(ipSuya, passwordServidor));

                // Enviar llave
                clienteL.enviarLlaves(llaveEmpaquetada);

                // Cambia el fragmento actual a ChatFragment
                FragmentTransaction transaccion = requireActivity().getSupportFragmentManager().beginTransaction();
                transaccion.replace(R.id.frame, frag);
                transaccion.commit();
            }
        });

        return v;
    }

}