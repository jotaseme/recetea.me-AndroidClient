package es.upm.miw.myapplication.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.upm.miw.myapplication.Models.RecipeSimplify;
import es.upm.miw.myapplication.MyViewHolder;
import es.upm.miw.myapplication.R;

public class ReciclerSimplifyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<RecipeSimplify> list;
    private final static String IMG_URL_BASE = "http://receteame.cecofersa.com/uploads/images/";

    public ReciclerSimplifyAdapter(List<RecipeSimplify> Data) {
        list = Data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_simplify_items, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.titleTextView.setText(list.get(position).getRecipe_name());
        String url = IMG_URL_BASE + list.get(position).getUrl_image();
        Picasso.with(context.getApplicationContext())
                .load(url)
                .placeholder(R.drawable.logo_upm_miw)
                .into(holder.coverImageView);
        holder.coverImageView.setTag(list.get(position).getUrl_image());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}