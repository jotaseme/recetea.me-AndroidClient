package es.upm.miw.myapplication.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.upm.miw.myapplication.Models.RecipeStep;
import es.upm.miw.myapplication.R;


public class StepsAdapter extends ArrayAdapter<RecipeStep> {

    private final Context context;
    private final List<RecipeStep> steps;


    public StepsAdapter(Context context, List<RecipeStep> steps) {
        super(context, R.layout.steptfila, steps);
        this.context  = context;
        this.steps = steps;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.steptfila, null);
        }

        try {
            RecipeStep step = steps.get(position);

            TextView textView = (TextView) convertView.findViewById(R.id.step_description);

            if (step != null) {

                textView.setText(step.getDescription());
            }

        } catch (IndexOutOfBoundsException ioobException) {
            Log.e("ERROR", ioobException.getMessage());
        }

        return convertView;
    }
}
