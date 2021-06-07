package com.databases1.betterzon;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import com.databases1.betterzon.clases.Mensaje;
import static android.view.View.GONE;

public class ViewHolderChat extends RecyclerView.ViewHolder{

    // Atributos
    CardView card;
    ImageView iconImage;
    TextView texto;
    ConstraintLayout cl;

    public ViewHolderChat(@NonNull View itemView) {
        super(itemView);
        card = itemView.findViewById(R.id.elemento_card_chat);
        iconImage = itemView.findViewById(R.id.card_image_mensaje);
        texto = itemView.findViewById(R.id.card_text1_mensaje);
        cl = itemView.findViewById(R.id.constraintChat);
    }

    public void bindData(Mensaje m){

        // Coloca el mensaje a la derecha
        if (m.isMensajePropio()) {

            ConstraintSet cs = new ConstraintSet();
            cs.clone(cl);
            cs.clear(R.id.elemento_card_chat, ConstraintSet.LEFT);
            cs.connect(R.id.elemento_card_chat, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,0);
            cs.applyTo(cl);
        }



        if (m.getTexto() == null) {

            texto.setVisibility(GONE);
            iconImage.setImageBitmap(m.getImagen());
            iconImage.setAdjustViewBounds(true);
            iconImage.setMaxHeight(200);
            iconImage.setMaxWidth(200);
        }
        else {
            texto.setText(m.getTexto());
            iconImage.setVisibility(GONE);
        }

    } // bindData

} // class viewHolder
