package es.upm.miw.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import es.upm.miw.myapplication.Adapters.ReciclerRecipeCommentsAdapter;
import es.upm.miw.myapplication.Models.RecipeComment;
import es.upm.miw.myapplication.Models.UserToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowCommentsActivity extends AppCompatActivity {

    private final static String URL_BASE = "http://10.0.2.2:8000/api/v1/";
    public static final String LOG_TAG = "TFM2017";

    int id_recipe;
    String recipen_name;
    public static Retrofit retrofit;
    public static ReceteameApiInterface receteameApiInterface;
    private List<RecipeComment> comments;
    RecyclerView recipeCommentsRecycler;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        id_recipe = getIntent().getIntExtra("id_recipe",0);
        recipen_name = getIntent().getStringExtra("recipe_name");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
                intent.putExtra("id_recipe", id_recipe);
                intent.putExtra("recipe_name", recipen_name);
                startActivity(intent);
            }
        });
        recipeCommentsRecycler = (RecyclerView) findViewById(R.id.commentRecyclerView);
        recipeCommentsRecycler.setHasFixedSize(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getRecipeComments();
        pd= ProgressDialog.show(ShowCommentsActivity.this,"","Por favor, espera",false);
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

    private void getRecipeComments() {
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        receteameApiInterface = retrofit.create(ReceteameApiInterface.class);
        Call<List<RecipeComment>> call = receteameApiInterface.getRecipeComments("Bearer " + UserToken.getUserToken(), id_recipe);
        call.enqueue(new Callback<List<RecipeComment>>() {
            @Override
            public void onResponse(Call<List<RecipeComment>> call, Response<List<RecipeComment>> response) {
                pd.dismiss();
                if (response.code() == 200) {
                    comments = response.body();
                    if (comments != null){
                        recipeCommentsRecycler.setAdapter(new ReciclerRecipeCommentsAdapter(comments));
                        new ReciclerRecipeCommentsAdapter(comments).notifyDataSetChanged();
                        recipeCommentsRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

                    }
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(),
                            "¡Error! Por favor vuelve a hacer login", Toast.LENGTH_LONG)
                            .show();
                    UserToken.destroyToken();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                } else if (response.code() == 400) {
                    Toast.makeText(getApplicationContext(),
                            "¡Error! Por favor vuelve a rellenar el formulario", Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "¡Error! ¿Que haces aquí?", Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<List<RecipeComment>> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());

            }
        });
    }

}
