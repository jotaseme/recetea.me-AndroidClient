package es.upm.miw.myapplication.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import es.upm.miw.myapplication.Models.RecipeStep;
import es.upm.miw.myapplication.MyStepsViewHolder;
import es.upm.miw.myapplication.R;

public class ReciclerRecipeStepsAdapter extends RecyclerView.Adapter<MyStepsViewHolder> {
    Context context;
    private List<RecipeStep> steps;

    public ReciclerRecipeStepsAdapter(List<RecipeStep> steps) {
        this.steps = steps;
    }

    @Override
    public MyStepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler__recipe_steps, parent, false);
        MyStepsViewHolder holder = new MyStepsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyStepsViewHolder holder, int position) {

        holder.stepTextView.setText(this.steps.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return this.steps.size();
    }
}