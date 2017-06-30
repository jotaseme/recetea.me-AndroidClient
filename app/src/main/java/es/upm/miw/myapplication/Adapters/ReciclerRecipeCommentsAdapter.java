package es.upm.miw.myapplication.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import es.upm.miw.myapplication.CommentViewHolder;
import es.upm.miw.myapplication.Models.RecipeComment;
import es.upm.miw.myapplication.R;

public class ReciclerRecipeCommentsAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    Context context;
    private List<RecipeComment> comments;

    public ReciclerRecipeCommentsAdapter(List<RecipeComment> comments) {
        this.comments = comments;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_recipe_comments, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {
        holder.descriptionTextView.setText(this.comments.get(position).getDescription());
        holder.userTextView.setText(this.comments.get(position).getIdUser().getName());
        holder.dateTextView.setText(this.comments.get(position).getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return this.comments.size();
    }
}