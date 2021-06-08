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
import android.widget.EditText;
import android.widget.Spinner;
import com.databases1.betterzon.clases.ClienteSocketLlaves;
import com.databases1.betterzon.clases.EmpaquetadoDeLlaves;
import com.databases1.betterzon.clases.EncriptadoAES;
import com.databases1.betterzon.clases.EncriptadoRSA;
import com.google.gson.Gson;
import java.security.KeyPair;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SegundoFragmento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SegundoFragmento extends Fragment{

    // Atributos
    private ChatFragmento frag = new ChatFragmento();
    private Spinner spinner1;
    private EditText presupuesto, direccion, descripcion;
    private Button boton1;
    private ClienteSocketLlaves clienteL;
    private SharedPreferences datosServidorPreference, estadoPreference, llaveGuardadaPreference, ipPreference, SQLPreference;
    private SharedPreferences.Editor editor1;
    private byte[] SQLPassword, SQLusuario, SQLip;
    private int contador;
    private String ipServidor, passwordServidor, json, ipSuya, SQLPasswordFinal, SQLusuarioFinal, SQLipFinal, llave, instruccion;
    private Gson gson;
    private ResultSet resultadoQuery;


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

        llave = "Escribe tu contraseña para la llave aqui"; // TODO: 2021-06-07 this
        spinner1 = v.findViewById(R.id.spinnerCrearProyecto);
        presupuesto = v.findViewById(R.id.editTextNumberPresupuesto);
        direccion = v.findViewById(R.id.editTextDireccion);
        descripcion = v.findViewById(R.id.editTextDescripcion);
        boton1 = v.findViewById(R.id.buttonCrearProyecto);
        datosServidorPreference = requireActivity().getSharedPreferences("servidor", MODE_PRIVATE);
        estadoPreference = requireActivity().getSharedPreferences("estados", MODE_PRIVATE);
        llaveGuardadaPreference = requireActivity().getSharedPreferences("llaves_propias", MODE_PRIVATE);
        SQLPreference = requireActivity().getSharedPreferences("SQL", MODE_PRIVATE);
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

                if ( !(presupuesto.getText().toString().equals("") || direccion.getText().toString().equals("")) ){

                    Thread hiloParaServicio = new Thread() {

                        @Override
                        public void run() {
                            segundoPlano();
                        } // run

                    }; // hilo

                    hiloParaServicio.start();

                } // if

            } // onclick

        }); // listener

        return v;
    }

    public void segundoPlano() {

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

            if (descripcion != null) {
                instruccion = "INSERT INTO PROYECTOS(presupuesto,tipo,direccion,descripcion)" +
                        "values(" + presupuesto.getText().toString() + ", '" +
                        spinner1.getSelectedItem().toString() + "', '" + direccion.getText().toString() +
                        "', '" + descripcion.getText().toString() + "');";
            }
            else{
                instruccion = "INSERT INTO PROYECTOS(presupuesto,tipo,direccion)" +
                        "values(" + presupuesto.getText().toString() + ", '" +
                        spinner1.getSelectedItem().toString() + "', '" +
                        direccion.getText().toString()  + "');";
            }

            st.executeUpdate(instruccion);

            // Guarda el nuevo estado para que siempre se ejecute ChatFragment
            editor1 = estadoPreference.edit();
            editor1.putString("proyecto", "Chat");
            editor1.apply();

            // servicio en segundo plano
            requireActivity().startService(new Intent(requireActivity(), SegundoPlanoMensajesYLlaves.class));

            instruccion = "SELECT id FROM PROYECTOS;";
            resultadoQuery = st.executeQuery(instruccion);
            contador = 0;

            while (resultadoQuery.next()) {
                contador++;
            }

            instruccion = "SELECT * FROM PERSONAS WHERE tipo = 'Profesional';";
            resultadoQuery = st.executeQuery(instruccion);

            for (int i = 0; resultadoQuery.next(); i++) {
                if (i == 0) { // TODO: 2021-06-08 Arreglar esto luego

                    ipSuya = resultadoQuery.getString("direccionIP");
                }
            }

            // Guardar ip del otro
            editor1 = ipPreference.edit();
            editor1.putString("del_otro", ipSuya);
            editor1.apply();

            st.close();
            conn.close();

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

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    } // segundoPlano

}