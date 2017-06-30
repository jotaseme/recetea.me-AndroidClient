package es.upm.miw.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.upm.miw.myapplication.Adapters.ReciclerAdapter;
import es.upm.miw.myapplication.Models.Recipe;
import es.upm.miw.myapplication.Models.Recipes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdvancedTagSearchActivity extends AppCompatActivity {
    public static final String LOG_TAG = "TFM2017";
    private final static String URL_BASE = "http://10.0.2.2:8000/api/v1/";
    public static final String CLAVE = LOG_TAG;


    private String tag;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_tag_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id_recipe = getIntent().getIntExtra("id_recipe",0);
        tag = getIntent().getStringExtra("tag");

        TextView header_text = (TextView)findViewById(R.id.header_text);
        header_text.setText("Recetas etiquetadas con " + tag);
        MyRecyclerView = (RecyclerView) findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        reciclerAdapter = new ReciclerAdapter(recipes);
        MyRecyclerView.setAdapter(reciclerAdapter);
        getRecipesByTag(1);
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        MyRecyclerView.setLayoutManager(mLayoutManager);
        MyRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        Intent intent = new Intent(getApplicationContext(), ShowRecipeActivity.class);
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
                    getRecipesByTag(count);
                    loading = true;
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getRecipesByTag(int pageIndex) {
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        receteameApiInterface = retrofit.create(ReceteameApiInterface.class);

        Call<Recipes> call = receteameApiInterface.getRecipesByTag(pageIndex, tag);
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
                        getApplicationContext(),
                        t.toString(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

    }

}
