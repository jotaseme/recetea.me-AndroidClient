package es.upm.miw.myapplication.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.upm.miw.myapplication.Adapters.ReciclerAdapter;
import es.upm.miw.myapplication.Models.Recipe;
import es.upm.miw.myapplication.Models.RecipeSimplify;
import es.upm.miw.myapplication.R;
import es.upm.miw.myapplication.ReceteameApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FiveFragment extends Fragment{
    ArrayList<Recipe> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;
    public static final String LOG_TAG = "TFM2017";
    private final static String URL_BASE = "http://10.0.2.2:8000/api/v1/";
    public static Retrofit retrofit;
    List<Recipe> randomRecipesList = new ArrayList<>();
    ProgressDialog pd;
    public FiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_five, container, false);

        MyRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        pd=ProgressDialog.show(getActivity(),"","Por favor, espera",false);
        getRecipeRandomly();


        if (listitems.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new ReciclerAdapter(randomRecipesList));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        FloatingActionButton floatingActionButtonLike = (FloatingActionButton)  rootView.findViewById(R.id.like);
        FloatingActionButton floatingActionButtonDislike = (FloatingActionButton)  rootView.findViewById(R.id.dislike);
        floatingActionButtonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RecipeSimplify recipeSimplify =
                        new RecipeSimplify(randomRecipesList.get(0).getIdRecipe(), randomRecipesList.get(0).getName(),randomRecipesList.get(0).getImage());
                recipeSimplify.saveRecipe();
                Toast.makeText(getActivity(), "¡Añadida a tu lista de recetas favoritas!", Toast.LENGTH_SHORT).show();
            }
        });
        floatingActionButtonDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd=ProgressDialog.show(getActivity(),"","Por favor, espera",false);
                getRecipeRandomly();


            }
        });
        return rootView;
    }

    public void getRecipeRandomly() {
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ReceteameApiInterface receteameApiInterface = retrofit.create(ReceteameApiInterface.class);

        Call<List<Recipe>> call = receteameApiInterface.getRandomRecipe();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                pd.dismiss();
                randomRecipesList = response.body();
                MyRecyclerView.setAdapter(new ReciclerAdapter(randomRecipesList));

            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
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
