package es.upm.miw.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MyStepsViewHolder extends RecyclerView.ViewHolder {

    public TextView stepTextView;

    public MyStepsViewHolder(View v) {
        super(v);
        stepTextView = (TextView) v.findViewById(R.id.recipe_step);

    }
}
