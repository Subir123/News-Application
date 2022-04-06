package com.example.newsxyz;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CustomViewHolder extends RecyclerView.ViewHolder {

    TextView text_title,text_source;
    ImageView img_headlines;
    CardView cardView;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);

        text_title = (TextView) itemView.findViewById(R.id.text_title);
        text_source = (TextView) itemView.findViewById(R.id.text_source);
        img_headlines = (ImageView) itemView.findViewById(R.id.img_headline);
        cardView = itemView.findViewById(R.id.main_container);
    }


}
