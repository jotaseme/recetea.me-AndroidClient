package es.upm.miw.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import es.upm.miw.myapplication.Models.UserToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentActivity extends AppCompatActivity {

    private final static String URL_BASE = "http://10.0.2.2:8000/api/v1/";
    public static final String LOG_TAG = "TFM2017";

    TextView recipeName;
    int id_recipe;
    private EditText commentTitle;
    private EditText commentDescription;
    public static Retrofit retrofit;
    public static ReceteameApiInterface receteameApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        id_recipe = getIntent().getIntExtra("id_recipe",0);
        String recipe_name = getIntent().getStringExtra("recipe_name");
        recipeName = (TextView) findViewById(R.id.recipe_name);
        recipeName.setText(recipe_name);
        commentTitle = (EditText) findViewById(R.id.comment_title);
        commentDescription = (EditText) findViewById(R.id.comment_description);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCreateComment();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void attemptCreateComment() {

        commentTitle.setError(null);
        commentDescription.setError(null);

        String title = commentTitle.getText().toString();
        String description = commentDescription.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(title)) {
            commentTitle.setError(getString(R.string.error_field_required));
            focusView = commentTitle;
            cancel = true;
        }else if (TextUtils.isEmpty(description)) {
            commentDescription.setError(getString(R.string.error_field_required));
            focusView = commentDescription;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else{
            createComment(title,description);
            Toast.makeText(getApplicationContext(),
                    "¡OK!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                int id_recipe = getIntent().getIntExtra("id_recipe",0);
                super.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createComment(String title, String description) {
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        receteameApiInterface = retrofit.create(ReceteameApiInterface.class);
        Call<Object> call = receteameApiInterface.createComment("Bearer " + UserToken.getUserToken(), id_recipe,title, description);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.code() == 201) {
                    Toast.makeText(getApplicationContext(),
                            "¡Comentario creado correctamente!", Toast.LENGTH_LONG)
                            .show();
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
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());

            }
        });
    }

}
