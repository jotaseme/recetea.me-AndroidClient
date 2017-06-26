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

public class ReciclerAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    private List<Recipe> list;
    private final static String IMG_URL_BASE = "http://receteame.cecofersa.com/uploads/images/";

    public ReciclerAdapter(List<Recipe> Data) {
        list = Data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_items, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.titleTextView.setText(list.get(position).getName());
        String url = IMG_URL_BASE + list.get(position).getImage();
        Picasso.with(context.getApplicationContext())
                .load(url)
                .placeholder(R.drawable.logo_upm_miw)
                .into(holder.coverImageView);
        holder.portionsTextView.setText(list.get(position).getPortions());
        holder.durationTextView.setText(list.get(position).getDuration());
        holder.coverImageView.setTag(list.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}