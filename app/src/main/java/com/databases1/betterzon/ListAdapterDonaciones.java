package com.databases1.betterzon;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.databases1.betterzon.clases.Donacion;
import com.databases1.betterzon.clases.EncriptadoAES;
import com.databases1.betterzon.clases.Material;
import com.databases1.betterzon.clases.Persona;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import static android.content.Context.MODE_PRIVATE;

public class ListAdapterDonaciones extends RecyclerView.Adapter<ViewHolderDonaciones> implements Runnable{

    // Atributos
    private LayoutInflater mInflador;
    private static LinkedList<Donacion> listaItemsOriginal;
    private static LinkedList<Donacion> listaItems;
    private Gson gson;
    private String json, SQLPasswordFinal, SQLusuarioFinal, SQLipFinal, llave, instruccion;
    private SharedPreferences SQLPreference;
    private byte[] SQLPassword, SQLusuario, SQLip;
    private ResultSet resultadoQuery;
    private Context c;

    public ListAdapterDonaciones(Context contexto) {

        this.c = contexto;

        llave = "Escribe tu contraseña para la llave aqui"; // TODO: 2021-06-07 this
        SQLPreference = contexto.getSharedPreferences("SQL", MODE_PRIVATE);
        gson = new Gson();
        this.mInflador = LayoutInflater.from(contexto);

        listaItemsOriginal = new LinkedList<>();
        listaItems = new LinkedList<>();

        Thread segundoPlano = new Thread(this);
        segundoPlano.start();
    }

    @Override
    public int getItemCount() {
        return listaItems.size();
    }

    @Override
    public ViewHolderDonaciones onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = mInflador.inflate(R.layout.gui_elemento_card_donacion, parent, false);
        return new ViewHolderDonaciones(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDonaciones holder, int position) {
        holder.bindData(listaItems.get(position));
    }

    @Override
    public void run(){

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

            instruccion = "SELECT * FROM DONACIONES AS D JOIN PERSONAS AS P ON D.cedula = P.cedula";

            resultadoQuery = st.executeQuery(instruccion);

            while (resultadoQuery.next()){

                listaItemsOriginal.add(new Donacion(
                        new Persona(
                                resultadoQuery.getInt("cedula"), 0, resultadoQuery.getString("nombre"), null, null, null),
                        new Material(
                                resultadoQuery.getInt("codigo"),null, null, null, null, 0.0)));
            }
            st.close();
            conn.close();

            listaItems.addAll(listaItemsOriginal);

            ((Activity)c).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void filterSearch(String search){

        String s = search.trim().toLowerCase();
        listaItems.clear();

        if (s.length() == 0){

            listaItems.addAll(listaItemsOriginal);
        }
        else {

            for (Donacion d : listaItemsOriginal) {

                if (d.getM().getNombre().toLowerCase().contains(s)) {

                    listaItems.add(d);
                }

            } // for

        } // else

        notifyDataSetChanged();

    } // filter search

} // class
