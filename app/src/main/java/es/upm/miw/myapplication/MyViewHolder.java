package es.upm.miw.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView titleTextView;
    public ImageView coverImageView;
    public TextView portionsTextView;
    public TextView durationTextView;

    public MyViewHolder(View v) {
        super(v);
        titleTextView = (TextView) v.findViewById(R.id.titleTextView);
        coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
        portionsTextView = (TextView) v.findViewById(R.id.recipe_portions);
        durationTextView = (TextView) v.findViewById(R.id.recipe_time);
    }
}
