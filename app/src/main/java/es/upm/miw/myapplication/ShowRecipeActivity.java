package es.upm.miw.myapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import es.upm.miw.myapplication.Adapters.IngredientsAdapter;
import es.upm.miw.myapplication.Adapters.ReciclerRecipeStepsAdapter;
import es.upm.miw.myapplication.Models.Recipe;
import es.upm.miw.myapplication.Models.RecipeIngredient;
import es.upm.miw.myapplication.Models.RecipeStep;
import es.upm.miw.myapplication.Models.RecipeTag;
import es.upm.miw.myapplication.Models.ShoppingList;
import es.upm.miw.myapplication.Models.UserToken;
import es.upm.miw.myapplication.Utils.AppendingObjectOutputStream;
import me.gujun.android.taggroup.TagGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowRecipeActivity extends AppCompatActivity {

    public static final String LOG_TAG = "TFM2017";
    private static final String FICHERO = "data/data/es.upm.miw.myapplication/files/shoppingList.txt";
    private TextView tvName, tvDescription;
    private ImageView ivRecipeImage;
    private Recipe recipe;
    private Toolbar toolbar;
    public static ReceteameApiInterface receteameApiInterface;
    private TagGroup mTagGroup;
    private ListView ingredientsView;
    private Button button;
    private Button addCommentButton;
    private Button showCommentsButton;
    private IngredientsAdapter adapter;
    //private StepsAdapter steps_adapter;
    private List<RecipeTag> tags = new ArrayList<>();
    private List<RecipeIngredient> ingredients = new ArrayList<>();
    private List<RecipeStep> steps = new ArrayList<>();
    public static Retrofit retrofit;
    RecyclerView recipeStepsRecycler;
    ProgressDialog pd;
    private final static String URL_BASE = "http://10.0.2.2:8000/api/v1/";
    private final static String IMG_URL_BASE = "http://10.0.2.2:8000/uploads/images/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail);

        adapter = new IngredientsAdapter(getApplicationContext(), this.ingredients,null);
        ingredientsView = (ListView)  findViewById(R.id.ingredientsListView);
        ingredientsView.setAdapter(adapter);
        toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        button = (Button) findViewById(R.id.shoppingList);
        addCommentButton = (Button) findViewById(R.id.addComment);
        showCommentsButton = (Button) findViewById(R.id.viewComments);
        if(UserToken.getUserToken()!=null){
            addCommentButton.setVisibility(View.VISIBLE);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recipeStepsRecycler = (RecyclerView) findViewById(R.id.recipeStepsCardView);
        recipeStepsRecycler.setHasFixedSize(true);


        ivRecipeImage   = (ImageView) findViewById(R.id.recipe_img);
        tvName   = (TextView)  findViewById(R.id.recipe_name);
        tvDescription     = (TextView)  findViewById(R.id.recipe_description);

        int id_recipe = getIntent().getIntExtra(MainActivity.CLAVE,0);
        recipeDetail(id_recipe);
        pd=ProgressDialog.show(ShowRecipeActivity.this,"","Por favor, espera",false);
        mTagGroup = (TagGroup) findViewById(R.id.tag_group);

        button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               List <RecipeIngredient> shoppingListIngredients = new ArrayList<>();
               CheckBox cb;
               int counter = 0;
               for (int x = 0; x<ingredientsView.getChildCount();x++){
                   cb = (CheckBox)ingredientsView.getChildAt(x).findViewById(R.id.checkBox);
                   if(cb.isChecked()){
                       shoppingListIngredients.add(recipe.getRecipeIngredients().get(x));
                       counter++;
                   }
               }
               if(counter > 0){
                   ShoppingList shoppingList = new ShoppingList(recipe.getIdRecipe(),recipe.getName(),shoppingListIngredients);
                   saveRecipeIngredients(shoppingList);
                   Toast.makeText(getApplicationContext(), "¡Ingredientes añadidos a tu carrito de la compra!", Toast.LENGTH_SHORT).show();
               }
           }
       });

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
                intent.putExtra("id_recipe", recipe.getIdRecipe());
                intent.putExtra("recipe_name", recipe.getName());
                startActivity(intent);
            }
        });
        showCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowCommentsActivity.class);
                intent.putExtra("id_recipe", recipe.getIdRecipe());
                intent.putExtra("recipe_name", recipe.getName());
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void recipeDetail(int id_recipe) {
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        receteameApiInterface = retrofit.create(ReceteameApiInterface.class);

        Call<Recipe> call = receteameApiInterface.getRecipe(id_recipe);
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                pd.dismiss();
                recipe = response.body();
                if (recipe.getImage() != null) {
                    String url = IMG_URL_BASE + recipe.getImage();
                    Picasso.with(getBaseContext())
                            .load(url)
                            .placeholder(R.drawable.logo_upm_miw)
                            .into(ivRecipeImage);
                }
                tvName.setText(recipe.getName());
                tvDescription.setText( Html.fromHtml(recipe.getDescription()));
                tags = recipe.getRecipeTags();
                ArrayList<String> tag_list = new ArrayList<>();
                ingredients = recipe.getRecipeIngredients();
                steps = recipe.getRecipeSteps();

                if (tags != null) {
                    for (RecipeTag tag: tags) {
                        tag_list.add(tag.getTag());
                    }
                }

                if (ingredients != null) {
                    for (RecipeIngredient ingredient: ingredients) {
                        adapter.addAll(ingredient);
                    }
                }

                if (steps != null){
                    recipeStepsRecycler.setAdapter(new ReciclerRecipeStepsAdapter(steps));
                    new ReciclerRecipeStepsAdapter(steps).notifyDataSetChanged();
                    recipeStepsRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

                }
                adapter.notifyDataSetChanged();
                mTagGroup.setTags(tag_list);
                justifyListViewHeightBasedOnChildren(ingredientsView);
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());
                Toast.makeText(
                        getApplicationContext(),
                        t.toString(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    public static void justifyListViewHeightBasedOnChildren (ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    public void saveRecipeIngredients(ShoppingList shoppingList){
        try
        {
            File file;
            file = new File(FICHERO);
            FileOutputStream fos = new FileOutputStream(FICHERO, true);
            if (file.length() == 0) {
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(shoppingList);
                out.close();
            } else {
                ObjectOutputStream out = new AppendingObjectOutputStream(fos);
                out.writeObject(shoppingList);
                out.close();
            }
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }
}
