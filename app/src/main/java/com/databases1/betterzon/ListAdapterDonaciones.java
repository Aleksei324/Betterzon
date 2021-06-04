package com.databases1.betterzon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.databases1.betterzon.clases.Donacion;
import com.databases1.betterzon.clases.Material;
import com.databases1.betterzon.clases.Persona;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterDonaciones extends RecyclerView.Adapter<ViewHolderDonaciones> {
    private List<Donacion> listaItems;
    private LayoutInflater mInflador;

    public ListAdapterDonaciones(Context contexto) {
        this.mInflador = LayoutInflater.from(contexto);
        this.listaItems = new ArrayList<>();

        crearListaDonaciones();
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

    public void setItems(List<Donacion> items){
        this.listaItems = items;
    }

    public void crearListaDonaciones(){

        this.listaItems.clear();

        // TODO: 2021-06-03 La base de datos debe llenar esta lista
        this.listaItems.add(new Donacion(new Persona(
                123, 123, "Juan Morales", "0000", "Usuario", "123"),
                new Material(
                        123,"Madera roja", "Madera", "La mejor marca", "Una tabla de madera", 123.4)));
        this.listaItems.add(new Donacion(new Persona(
                456, 456, "Roberto Menez", "0000", "Usuario", "456"),
                new Material(
                        456,"Acero inoxidable", "Metal", "La mejor marca", "Un pedazo oxidado", 456.7)));
        this.listaItems.add(new Donacion(new Persona(
                789, 789, "Luis Salazar", "0000", "Usuario", "123"),
                new Material(
                        789,"Vidrio reforzado", "Vidrio", "La mejor marca", "Un vidrio oscuro resistente", 789.0)));
    }

} // class
