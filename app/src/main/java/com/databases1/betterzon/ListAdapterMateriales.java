package com.databases1.betterzon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.databases1.betterzon.clases.Material;
import java.util.LinkedList;

public class ListAdapterMateriales extends RecyclerView.Adapter<ViewHolderMateriales> {
    private LayoutInflater mInflador;
    private LinkedList<Material> listaItemsOriginal;
    private LinkedList<Material> listaItems;

    public ListAdapterMateriales(Context contexto) {
        this.mInflador = LayoutInflater.from(contexto);

        this.listaItemsOriginal = new LinkedList<>();
        crearListaMateriales();

        this.listaItems = new LinkedList<>();
        this.listaItems.addAll(this.listaItemsOriginal);
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

    public void crearListaMateriales(){

        // TODO: 2021-06-03 La base de datos debe llenar esta lista

        this.listaItemsOriginal.add(new Material(
                123,"Arcilla azul", "Arcilla", "La mejor marca", "Un pedazo de arcilla", 123.4));

        this.listaItemsOriginal.add(new Material(
                456,"Ladrillo de plastico", "Plastico", "La mejor marca", "De materiales reciclables", 456.7));

        this.listaItemsOriginal.add(new Material(
                789,"Concreto amarillo", "Concreto", "La mejor marca", "Un poco raro", 789.0));

        this.listaItemsOriginal.add(new Material(
                1123,"Piedra roja", "Petreo", "La mejor marca", "Un pedazo de piedra", 123.4));

        this.listaItemsOriginal.add(new Material(
                1456,"Ladrillo raro", "Plastico", "La mejor marca", "De materiales reciclables", 456.7));

        this.listaItemsOriginal.add(new Material(
                1789,"Concreto amarillo", "Concreto", "La mejor marca", "Un poco raro", 789.0));
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
