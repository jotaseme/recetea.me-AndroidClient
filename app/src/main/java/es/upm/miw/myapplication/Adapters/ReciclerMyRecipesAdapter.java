package es.upm.miw.myapplication.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.upm.miw.myapplication.Models.Recipe;
import es.upm.miw.myapplication.MyViewHolder;
import es.upm.miw.myapplication.R;

public class ReciclerMyRecipesAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    private List<Recipe> recipes;
    private final static String IMG_URL_BASE = "http://10.0.2.2:8000/uploads/images/";

    public ReciclerMyRecipesAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_my_recipes, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.titleTextView.setText(this.recipes.get(position).getName());
        String url = IMG_URL_BASE + this.recipes.get(position).getImage();
        Picasso.with(context.getApplicationContext())
                .load(url)
                .placeholder(R.drawable.logo_upm_miw)
                .into(holder.coverImageView);

        holder.coverImageView.setTag(this.recipes.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return this.recipes.size();
    }
}