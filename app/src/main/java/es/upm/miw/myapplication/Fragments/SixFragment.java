package es.upm.miw.myapplication.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import es.upm.miw.myapplication.CreateRecipeActivity;
import es.upm.miw.myapplication.Models.UserToken;
import es.upm.miw.myapplication.R;
import es.upm.miw.myapplication.ReceteameApiInterface;
import es.upm.miw.myapplication.RecyclerItemClickListener;
import es.upm.miw.myapplication.RegisterActivity;
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
    private ProgressDialog pDialog;

    private Button btnLogin;
    private Button btnLinkToRegister;
    private ImageView btnLogout;
    private EditText inputEmailLogin;
    private EditText inputPasswordLogin;
    private TextView usernameTextView;
    Boolean isLogged = false;
    View rootView;
    ProgressDialog pd;
    public SixFragment() {
        // Required empty public constructor
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
            btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
            btnLinkToRegister = (Button) rootView.findViewById(R.id.btnLinkToRegisterScreen);
            pDialog = new ProgressDialog(getActivity().getApplicationContext());
            pDialog.setCancelable(false);


            btnLogin.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    String email = inputEmailLogin.getText().toString().trim();
                    String password = inputPasswordLogin.getText().toString().trim();

                    // Check for empty data in the form
                    if (!email.isEmpty() && !password.isEmpty()) {
                        pd=ProgressDialog.show(getActivity(),"","Please Wait",false);
                        loginUser(email,password);
                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Please enter the credentials!", Toast.LENGTH_LONG)
                                .show();
                    }
                }

            });

            btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getBaseContext(), RegisterActivity.class);
                    intent.putExtra(CLAVE, "HOLA AMIGO");
                    startActivity(intent);

                }
            });
            return rootView;
        }else{
            String userToken = UserToken.getUserToken();
            String username = "";
            try {
               username = UserToken.decoded(userToken);

            } catch (Exception e) {
                e.printStackTrace();
            }

            rootView =  inflater.inflate(R.layout.profile_layout, container, false);
            usernameTextView = (TextView) rootView.findViewById(R.id.user_profile_name);
            usernameTextView.setText(username);
            btnLogout = (ImageView) rootView.findViewById(R.id.logout);
            FloatingActionButton addRecipe = (FloatingActionButton)  rootView.findViewById(R.id.fab);
            addRecipe.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity().getBaseContext(), CreateRecipeActivity.class);
                    intent.putExtra("JA", "JAJAJ");
                    startActivity(intent);
                   // Toast.makeText(getActivity(), "¡Añadida a tu lista de recetas favoritas!", Toast.LENGTH_SHORT).show();
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
}
