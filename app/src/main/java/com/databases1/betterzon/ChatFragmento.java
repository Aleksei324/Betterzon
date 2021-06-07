package com.databases1.betterzon;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.databases1.betterzon.clases.ClienteSocketMensajes;
import com.databases1.betterzon.clases.EmpaquetadoDeMensaje;
import com.databases1.betterzon.clases.EncriptadoAES;
import com.databases1.betterzon.clases.EncriptadoRSA;
import com.databases1.betterzon.clases.Mensaje;
import com.google.gson.Gson;
import java.security.PublicKey;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragmento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragmento extends Fragment {

    // Atributos
    private RecyclerView recyclerView1;
    private ListAdapterChat adapter;
    private Button boton_send;
    private EditText textbox1;
    private SharedPreferences servidorPreference, llaveDelOtroPreference, llaveMiaPreference, ipOtroPreference;
    private String ipServidor, ipOtro, passwordServidor, json;
    private ClienteSocketMensajes clienteM;
    private PublicKey llavePublicaOtro;
    private Gson gson;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragmento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragmento newInstance(String param1, String param2) {
        ChatFragmento fragment = new ChatFragmento();
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
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView1 = v.findViewById(R.id.recyclerViewChat);
        boton_send = v.findViewById(R.id.boton_send_mensaje);
        textbox1 = v.findViewById(R.id.editTextMensaje);
        servidorPreference = requireActivity().getSharedPreferences("servidor", MODE_PRIVATE);
        llaveDelOtroPreference = requireActivity().getSharedPreferences("llaves_otro", MODE_PRIVATE);
        llaveMiaPreference = requireActivity().getSharedPreferences("llaves_propias", MODE_PRIVATE);
        ipOtroPreference = requireActivity().getSharedPreferences("IPs", MODE_PRIVATE);
        ipServidor = servidorPreference.getString("ip", null);
        passwordServidor = servidorPreference.getString("password", null);
        clienteM = new ClienteSocketMensajes(ipServidor);
        gson = new Gson();

        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(v.getContext()));
        adapter = new ListAdapterChat(v.getContext());
        recyclerView1.setAdapter(adapter);

        boton_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: 2021-06-06 Confirmar que los datos sean validos

                // TODO: 2021-06-06 Guardar mensaje en base de datos

                // Obtener la ip de la otra persona
                ipOtro = ipOtroPreference.getString("del otro", null);

                // Obtener la llave publica del otro
                json = llaveDelOtroPreference.getString("llave_publica", null);
                llavePublicaOtro = gson.fromJson(json, PublicKey.class);

                EmpaquetadoDeMensaje datosDelMensaje = new EmpaquetadoDeMensaje(
                        EncriptadoAES.encriptar(ipOtro, passwordServidor),
                        EncriptadoRSA.encriptarTexto(textbox1.getText().toString(), llavePublicaOtro), null);

                // Enviar mensaje
                clienteM.enviarMensaje(datosDelMensaje);

                // Guardar mensaje localmente
                ListAdapterChat.addMensaje(new Mensaje(
                        textbox1.getText().toString(), null, true));
            }
        });

        return v;
    }
}