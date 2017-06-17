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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import es.upm.miw.myapplication.CreateRecipeActivity;
import es.upm.miw.myapplication.Utils.AppendingObjectOutputStream;
import es.upm.miw.myapplication.Models.UserToken;
import es.upm.miw.myapplication.R;
import es.upm.miw.myapplication.ReceteameApiInterface;
import es.upm.miw.myapplication.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SixFragment extends Fragment{

    public static final String LOG_TAG = "TFM2017";
    //private final static String URL_BASE = "http://receteame.cecofersa.com/";
    private static final String USER_TOKEN= "data/data/es.upm.miw.myapplication/files/token.txt";
    private final static String URL_BASE = "http://10.0.2.2:8000/";
    public static final String CLAVE = LOG_TAG;

    public static ReceteameApiInterface receteameApiInterface;
    public static Retrofit retrofit;

    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;

    private Button btnLogin;
    private Button btnLinkToRegister;
    private ImageView btnLogout;
    private EditText inputEmailLogin;
    private EditText inputPasswordLogin;
    //private SessionManager session;
    Boolean isLogged = false;
    View rootView;
    View secondView;
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

        if(!UserToken.isLogged()){
            final View rootView =  inflater.inflate(R.layout.fragment_seven, container, false);

            inputEmailLogin = (EditText) rootView.findViewById(R.id.email);
            inputPasswordLogin = (EditText) rootView.findViewById(R.id.password);
            btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
            btnLinkToRegister = (Button) rootView.findViewById(R.id.btnLinkToRegisterScreen);

            // Progress dialog
            pDialog = new ProgressDialog(getActivity().getApplicationContext());
            pDialog.setCancelable(false);


            btnLogin.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    String email = inputEmailLogin.getText().toString().trim();
                    String password = inputPasswordLogin.getText().toString().trim();

                    // Check for empty data in the form
                    if (!email.isEmpty() && !password.isEmpty()) {
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
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Registro", Toast.LENGTH_LONG)
                            .show();

                }
            });
            return rootView;
        }else{

            rootView =  inflater.inflate(R.layout.profile_layout, container, false);
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
            /*
            inputFullName = (EditText) rootView.findViewById(R.id.name);
            inputEmail = (EditText) rootView.findViewById(R.id.email);
            inputPassword = (EditText) rootView.findViewById(R.id.password);
            btnRegister = (Button) rootView.findViewById(R.id.btnRegister);
            btnLinkToLogin = (Button) rootView.findViewById(R.id.btnLinkToLoginScreen);

            btnRegister.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String name = inputFullName.getText().toString().trim();
                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();

                    if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                        registerUser(name, email, password);
                    } else {

                        Toast.makeText(getActivity().getApplicationContext(),
                                "Please enter your details!", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });

            btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                        UserToken.destroyToken();
                }

            });*/
            return rootView;
        }
    }


    private void registerUser(String user, String email, String password){
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        receteameApiInterface = retrofit.create(ReceteameApiInterface.class);

        Call<Object> call = receteameApiInterface.registerUser(user, email, password);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                //progress.dismiss();
                Object user = response.body();
                Object code = response.code();
                Log.i(LOG_TAG, code + " ER CODE");
                //inputEmail.setError("El codigo de error " + code);
                Log.i(LOG_TAG, user + " esto tengo");

                try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Log.i(LOG_TAG, "MAS INFO: " + jObjError.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
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
                //progress.dismiss();
                Log.i(LOG_TAG, response.code() + " CODE");
                //inputEmail.setError("El codigo de error " + code);
                Log.i(LOG_TAG, response.body()+"");
                if(response.code()==200){
                    Object token = response.body();
                    token = token.toString().substring(token.toString().lastIndexOf("=") + 1);
                    token = token.toString().substring(0,token.toString().length()-1);
                    UserToken userToken = new UserToken(token.toString());
                    saveUserToken(userToken);
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

    public void saveUserToken(UserToken userToken){
        try
        {
            File file;

            file = new File(USER_TOKEN);
            file.delete();
            FileOutputStream fos = new FileOutputStream(USER_TOKEN, true);

            if (file.length() == 0) {
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(userToken);
                out.close();
            } else {
                ObjectOutputStream out = new AppendingObjectOutputStream(fos);
                out.writeObject(userToken);
                out.close();
            }

        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }

}
