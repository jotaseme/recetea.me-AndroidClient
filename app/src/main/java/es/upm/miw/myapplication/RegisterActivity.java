package es.upm.miw.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import es.upm.miw.myapplication.Models.UserToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    public static final String LOG_TAG = "TFM2017";
    private final static String URL_BASE = "http://10.0.2.2:8000/api/v1/";
    public static final String CLAVE = LOG_TAG;

    public static ReceteameApiInterface receteameApiInterface;
    public static Retrofit retrofit;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        Button btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    pd=ProgressDialog.show(RegisterActivity.this,"","Please Wait",false);
                    registerUser(name, email, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                                "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 1) {
        } else if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else {
            fm.popBackStack();
        }
    }
    private void registerUser(String user, final String email, String password){
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        receteameApiInterface = retrofit.create(ReceteameApiInterface.class);

        Call<Object> call = receteameApiInterface.registerUser(user, email, password);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                pd.dismiss();
                int code = response.code();
                String messsage = "¡Ha ocurrido un error!";
                if(code==201){
                    Object token = response.body();
                    UserToken userToken = new UserToken(token.toString());
                    userToken.saveUserToken();
                    Toast.makeText(getApplicationContext(), "¡Registro completado!. Ahora, introduce tus credenciales", Toast.LENGTH_LONG).show();
                    onBackPressed();

                }else if(code==400){
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errors = jObjError.getString("errors");
                        int a = errors.indexOf("errors");
                        String sub = errors.substring(a+10);
                        int b = sub.indexOf("]");
                        messsage = errors.substring(a+10,a+10+b-1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getApplicationContext(),
                            messsage, Toast.LENGTH_LONG)
                            .show();
                    inputPassword.setText(null);
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
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
