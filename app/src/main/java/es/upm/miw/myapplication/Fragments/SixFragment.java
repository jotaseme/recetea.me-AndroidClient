package es.upm.miw.myapplication.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.upm.miw.myapplication.Adapters.ReciclerMyRecipesAdapter;
import es.upm.miw.myapplication.CreateRecipeActivity;
import es.upm.miw.myapplication.Models.Recipe;
import es.upm.miw.myapplication.Models.UserToken;
import es.upm.miw.myapplication.R;
import es.upm.miw.myapplication.ReceteameApiInterface;
import es.upm.miw.myapplication.RecyclerItemClickListener;
import es.upm.miw.myapplication.RegisterActivity;
import es.upm.miw.myapplication.ShowRecipeActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SixFragment extends Fragment{

    public static final String LOG_TAG = "TFM2017";
    private static final String USER_TOKEN= "data/data/es.upm.miw.myapplication/files/token.txt";
    private final static String URL_BASE = "http://10.0.2.2:8000/api/v1/";
    public static final String CLAVE = LOG_TAG;
    public static ReceteameApiInterface receteameApiInterface;
    public static Retrofit retrofit;
    private EditText inputEmailLogin;
    private EditText inputPasswordLogin;
    View rootView;
    ProgressDialog pd;
    private ReciclerMyRecipesAdapter reciclerMyRecipesAdapter;
    private List<Recipe> myRecipeList;
    ReciclerMyRecipesAdapter adapter;
    RecyclerView myRecipesRecycler;


    public SixFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(UserToken.getUserToken()==null){
            final View rootView =  inflater.inflate(R.layout.fragment_seven, container, false);

            inputEmailLogin = (EditText) rootView.findViewById(R.id.email);
            inputPasswordLogin = (EditText) rootView.findViewById(R.id.password);
            Button btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
            Button btnLinkToRegister = (Button) rootView.findViewById(R.id.btnLinkToRegisterScreen);


            btnLogin.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    String email = inputEmailLogin.getText().toString().trim();
                    String password = inputPasswordLogin.getText().toString().trim();

                    // Check for empty data in the form
                    if (!email.isEmpty() && !password.isEmpty()) {
                        pd=ProgressDialog.show(getActivity(),"","Espera, por favor",false);
                        loginUser(email,password);
                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Por favor, revisa tus credenciales", Toast.LENGTH_LONG)
                                .show();
                    }
                }

            });

            btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getBaseContext(), RegisterActivity.class);
                    startActivity(intent);

                }
            });
            return rootView;
        }else{
            getSelfRecipes();
            String userToken = UserToken.getUserToken();
            String username = "";
            try {
               username = UserToken.decoded(userToken);

            } catch (Exception e) {
                e.printStackTrace();
            }
            rootView =  inflater.inflate(R.layout.profile_layout, container, false);
            TextView usernameTextView = (TextView) rootView.findViewById(R.id.user_profile_name);
            usernameTextView.setText(username);
            myRecipesRecycler = (RecyclerView) rootView.findViewById(R.id.myRecipesCardView);
            myRecipesRecycler.setHasFixedSize(true);
            myRecipesRecycler.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity().getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int i) {
                            Intent intent = new Intent(getActivity().getBaseContext(), ShowRecipeActivity.class);
                            intent.putExtra(CLAVE, myRecipeList.get(i).getIdRecipe());
                            startActivity(intent);
                        }
                    }));
            ImageView btnLogout = (ImageView) rootView.findViewById(R.id.logout);
            FloatingActionButton addRecipe = (FloatingActionButton)  rootView.findViewById(R.id.fab);
            addRecipe.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity().getBaseContext(), CreateRecipeActivity.class);
                    startActivity(intent);
                    new RecyclerItemClickListener(getActivity().getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int i) {
                        }
                    });
                }
            });

            btnLogout.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "¡Adios!", Toast.LENGTH_SHORT).show();
                            (getActivity()).recreate();
                            UserToken.destroyToken();
                        }

                    });
            return rootView;
        }
    }

    private void loginUser(String email, String password){
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        receteameApiInterface = retrofit.create(ReceteameApiInterface.class);

        Call<Object> call = receteameApiInterface.loginUser(email, password);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                pd.dismiss();
                if(response.code()==200){
                    Object token = response.body();
                    UserToken userToken = new UserToken(token.toString());
                    userToken.saveUserToken();
                    (getActivity()).recreate();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),
                            "¡Error! Por favor revisa tus credenciales", Toast.LENGTH_LONG)
                            .show();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());
                Toast.makeText(
                        getContext(),
                        t.toString(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void getSelfRecipes(){
        String token = "Bearer " + UserToken.getUserToken();
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        receteameApiInterface = retrofit.create(ReceteameApiInterface.class);

        Call<List<Recipe>> call = receteameApiInterface.getSelfRecipes(token);
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.code()==200){
                    myRecipeList = response.body();
                    myRecipesRecycler.setAdapter(new ReciclerMyRecipesAdapter(myRecipeList));
                    new ReciclerMyRecipesAdapter(myRecipeList).notifyDataSetChanged();

                    myRecipesRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),
                            "¡Error! Por favor revisa tus credenciales", Toast.LENGTH_LONG)
                            .show();
                    UserToken.destroyToken();
                }
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
