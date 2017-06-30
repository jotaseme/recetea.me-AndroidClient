package es.upm.miw.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.upm.miw.myapplication.Models.Recipe;
import es.upm.miw.myapplication.Models.Recipes;
import es.upm.miw.myapplication.R;
import es.upm.miw.myapplication.ReceteameApiInterface;
import es.upm.miw.myapplication.Adapters.ReciclerAdapter;
import es.upm.miw.myapplication.RecyclerItemClickListener;
import es.upm.miw.myapplication.ShowRecipeActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OneFragment extends Fragment {
    public static final String LOG_TAG = "TFM2017";
    private final static String URL_BASE = "http://10.0.2.2:8000/api/v1/";
    public static final String CLAVE = LOG_TAG;

    public static ReceteameApiInterface receteameApiInterface;
    public static Retrofit retrofit;
    private RecyclerView MyRecyclerView;
    private List<Recipe> recipes = new ArrayList<>();

    private int count = 1;
    private ReciclerAdapter reciclerAdapter;

    private GridLayoutManager mLayoutManager;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        MyRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        reciclerAdapter = new ReciclerAdapter(recipes);
        MyRecyclerView.setAdapter(reciclerAdapter);
        getRecipes(1);
        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        MyRecyclerView.setLayoutManager(mLayoutManager);
        MyRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity().getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        Intent intent = new Intent(getActivity().getBaseContext(), ShowRecipeActivity.class);
                        intent.putExtra(CLAVE, recipes.get(i).getIdRecipe());
                        startActivity(intent);
                    }
                }));

        MyRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = MyRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    count++;
                    getRecipes(count);
                    loading = true;
                }
            }
        });
        return rootView;
    }

    public void getRecipes(int pageIndex) {
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        receteameApiInterface = retrofit.create(ReceteameApiInterface.class);

        Call<Recipes> call = receteameApiInterface.getRecipes(pageIndex);
        call.enqueue(new Callback<Recipes>() {
            @Override
            public void onResponse(Call<Recipes> call, Response<Recipes> response) {
                Recipes recipesResult = response.body();
                recipes.addAll(recipesResult.getRecipes());
                reciclerAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<Recipes> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());
                Toast.makeText(
                        getContext(),
                        t.toString(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

}
