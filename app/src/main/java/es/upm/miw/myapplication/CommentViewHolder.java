package es.upm.miw.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class CommentViewHolder extends RecyclerView.ViewHolder {

    public TextView userTextView;
    public TextView descriptionTextView;
    public TextView dateTextView;

    public CommentViewHolder(View v) {
        super(v);
        userTextView = (TextView) v.findViewById(R.id.user_name);
        descriptionTextView = (TextView) v.findViewById(R.id.content);
        dateTextView  =(TextView)v.findViewById(R.id.time);
    }
}
