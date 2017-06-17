package es.upm.miw.myapplication.Adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.upm.miw.myapplication.Models.Recipe;
import es.upm.miw.myapplication.R;


public class RecipesAdapter  extends ArrayAdapter<Recipe> {

    private final Context context;
    private final List<Recipe> recipes;
    private final static String IMG_URL_BASE = "http://receteame.cecofersa.com/uploads/images/";

    public RecipesAdapter(Context context, List<Recipe> recipes) {
        super(context, R.layout.fila, recipes);
        this.context  = context;
        this.recipes =  recipes;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fila, null);
        }

        try {
            Recipe recipe = recipes.get(position);

            TextView recipeName = (TextView) convertView.findViewById(R.id.recipe_name);
            TextView recipeDescription = (TextView) convertView.findViewById(R.id.recipe_description);
            ImageView recipeImage   = (ImageView) convertView.findViewById(R.id.recipe_img);
            if (recipeName != null) {
                recipeName.setTextSize(15);
                recipeName.setText(recipe.getName());
            }
            if (recipeDescription != null) {
                recipeDescription.setText(recipe.getDescription());
                recipeDescription.setTextSize(10);

            }
            if (null != recipe.getImage()) {
                String url = IMG_URL_BASE + recipe.getImage();
                Picasso.with(context)
                        .load(url)
                        .placeholder(R.drawable.logo_upm_miw)
                        .into(recipeImage);
            }
        } catch (IndexOutOfBoundsException ioobException) {
            Log.e("ERROR", ioobException.getMessage());
        }

        return convertView;
    }
}
