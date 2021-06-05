package com.databases1.betterzon;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.databases1.betterzon.clases.Material;

public class ViewHolderMateriales extends RecyclerView.ViewHolder {

    ImageView iconImage;
    TextView nombre;
    TextView precio;

    public ViewHolderMateriales(@NonNull View itemView) {
        super(itemView);
        iconImage = itemView.findViewById(R.id.card_image_material);
        nombre = itemView.findViewById(R.id.card_text1_material);
        precio = itemView.findViewById(R.id.card_text2_material);
    }

    public void bindData(Material m){

        switch (m.getTipo()){

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

        nombre.setText(m.getNombre());
        precio.setText(R.string.fragment1_precio);
        precio.setText(String.format("%s%s", precio.getText(), m.getPrecio()));

    } // bindData

} // class viewHolder
