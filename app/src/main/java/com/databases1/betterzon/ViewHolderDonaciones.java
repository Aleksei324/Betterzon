package com.databases1.betterzon;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.databases1.betterzon.clases.Donacion;

public class ViewHolderDonaciones extends RecyclerView.ViewHolder {

    // Atributos
    ImageView iconImage;
    TextView nombre_material;
    TextView nombre;

    public ViewHolderDonaciones(@NonNull View itemView) {
        super(itemView);
        iconImage = itemView.findViewById(R.id.card_image_donacion);
        nombre_material = itemView.findViewById(R.id.card_text1_donacion);
        nombre = itemView.findViewById(R.id.card_text2_donacion);
    }

    public void bindData(Donacion d){

        switch (d.getM().getTipo()){

            case "Madera":
                iconImage.setImageResource(R.drawable.ic_log);
                break;

            case "Metal":
                iconImage.setImageResource(R.drawable.ic_beam);
                break;

            case "Arcilla":
                iconImage.setImageResource(R.drawable.ic_clay_crafting);
                break;

            case "Petreo":
                iconImage.setImageResource(R.drawable.ic_rock);
                break;

            case "Vidrio":
                iconImage.setImageResource(R.drawable.ic_glass);
                break;

            case "Plastico":
                iconImage.setImageResource(R.drawable.ic_plastic);
                break;

            case "Concreto":
                iconImage.setImageResource(R.drawable.ic_concrete_mixer);
                break;
        }

        nombre_material.setText(d.getM().getNombre());
        nombre.setText(d.getP().getNombre());

    } // bindData

} // class viewHolder
