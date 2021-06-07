package com.databases1.betterzon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.databases1.betterzon.clases.Mensaje;
import java.util.LinkedList;

public class ListAdapterChat extends RecyclerView.Adapter<ViewHolderChat>{

    // Atributos
    private LayoutInflater mInflador;
    private static LinkedList<Mensaje> listaItems;

    public ListAdapterChat(Context contexto) {
        this.mInflador = LayoutInflater.from(contexto);

        listaItems = new LinkedList<>();
        crearListaMateriales();
    }

    @Override
    public int getItemCount() {
        return listaItems.size();
    }

    @Override
    public ViewHolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = mInflador.inflate(R.layout.gui_elemento_card_chat, parent, false);
        return new ViewHolderChat(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderChat holder, int position) {
        holder.bindData(listaItems.get(position));
    }

    public void crearListaMateriales(){

        // TODO: 2021-06-03 La base de datos debe llenar esta lista

    }

    public static void addMensaje(Mensaje mensaje){
        listaItems.add(mensaje);
    }

} // class