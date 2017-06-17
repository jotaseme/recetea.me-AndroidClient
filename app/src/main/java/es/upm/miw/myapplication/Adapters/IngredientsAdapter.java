package es.upm.miw.myapplication.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.List;

import es.upm.miw.myapplication.Models.RecipeIngredient;
import es.upm.miw.myapplication.R;


public class IngredientsAdapter  extends ArrayAdapter<RecipeIngredient> {

    private final Context context;
    private final List<RecipeIngredient> ingredients;
    private String type;


    public IngredientsAdapter(Context context, List<RecipeIngredient> ingredients, String type) {
        super(context, R.layout.tagfila, ingredients);
        this.context  = context;
        this.ingredients = ingredients;
        this.type = type;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(this.type!=null){
                convertView = inflater.inflate(R.layout.tagfila1, null);
            }else{
                convertView = inflater.inflate(R.layout.tagfila, null);
            }

        }

        try {
            RecipeIngredient ingredient = ingredients.get(position);

            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            if (ingredient != null) {
                checkBox.setText(ingredient.getName());
            }

        } catch (IndexOutOfBoundsException ioobException) {
            Log.e("ERROR", ioobException.getMessage());
        }

        return convertView;
    }
}
