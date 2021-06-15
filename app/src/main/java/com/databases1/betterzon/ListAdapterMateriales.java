package com.databases1.betterzon;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.databases1.betterzon.clases.EncriptadoAES;
import com.databases1.betterzon.clases.Material;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import static android.content.Context.MODE_PRIVATE;

public class ListAdapterMateriales extends RecyclerView.Adapter<ViewHolderMateriales> implements Runnable {

    // Atributos
    private LayoutInflater mInflador;
    private static LinkedList<Material> listaItemsOriginal;
    private static LinkedList<Material> listaItems;
    private Gson gson;
    private String json, SQLPasswordFinal, SQLusuarioFinal, SQLipFinal, llave, instruccion;
    private SharedPreferences SQLPreference;
    private byte[] SQLPassword, SQLusuario, SQLip;
    private ResultSet resultadoQuery;
    private Context c;

    public ListAdapterMateriales(Context contexto) {
        llave = "Escribe tu contraseña para la llave aqui"; // TODO: 2021-06-07 this
        SQLPreference = contexto.getSharedPreferences("SQL", MODE_PRIVATE);
        gson = new Gson();
        this.c = contexto;

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
    public ViewHolderMateriales onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = mInflador.inflate(R.layout.gui_elemento_card_material, parent, false);
        return new ViewHolderMateriales(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMateriales holder, int position) {
        holder.bindData(listaItems.get(position));
    }

    @Override
    public void run(){

        listaItemsOriginal.clear();

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

            instruccion = "SELECT * FROM MATERIALES";

            resultadoQuery = st.executeQuery(instruccion);

            while (resultadoQuery.next()){
                listaItemsOriginal.add(new Material(
                        resultadoQuery.getLong("codigo"),
                        resultadoQuery.getString("nombre"),
                        resultadoQuery.getString("tipo"),
                        resultadoQuery.getString("marca"),
                        resultadoQuery.getString("descripcion"),
                        resultadoQuery.getDouble("precio")));
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

            for (Material m : listaItemsOriginal) {

                if (m.getTipo().toLowerCase().contains(s) ||
                        m.getNombre().toLowerCase().contains(s)) {

                    listaItems.add(m);
                }

            } // for

        } // else

        notifyDataSetChanged();

    } // filter search

} // class
