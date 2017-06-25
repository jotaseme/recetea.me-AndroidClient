package es.upm.miw.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import es.upm.miw.myapplication.Models.RecipeTag;
import es.upm.miw.myapplication.Models.UserToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A login screen that offers login via email/password.
 */
public class CreateRecipeActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    public static final String LOG_TAG = "TFM2017";
    //private final static String URL_BASE = "http://receteame.cecofersa.com/";
    private static final String USER_TOKEN= "data/data/es.upm.miw.myapplication/files/token.txt";
    private final static String URL_BASE = "http://10.0.2.2:8000/api/v1/";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private int PICK_IMAGE_REQUEST = 1;

    private List<RecipeTag> tags;

    AutoCompleteTextView textIn;
    AutoCompleteTextView textInIngredient;
    AutoCompleteTextView textInStep;
    Button buttonAdd;
    Button buttonAddIngredient;
    Button buttonAddStep;
    LinearLayout container;
    LinearLayout containerIngredient;
    LinearLayout containerStep;
    TextView reList, info, reListIngredient, infoIngredient, reListStep, infoStep;

    ArrayAdapter<String> adapter;

    private String[] TAGS;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.

    private EditText mRecipeNameView;
    private EditText mRecipeDescriptionView;
    private EditText mRecipeDurationView;
    private EditText mRecipePortionsView;
    private ImageView imageView;
    public static ReceteameApiInterface receteameApiInterface;
    public static Retrofit retrofit;
    String imagePath;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        receteameApiInterface = retrofit.create(ReceteameApiInterface.class);
        setContentView(R.layout.activity_create_recipe);
        getTags();
        mRecipeNameView = (EditText) findViewById(R.id.creation_recipe_name);
        mRecipeDescriptionView = (EditText) findViewById(R.id.creation_recipe_description);
        mRecipeDurationView = (EditText) findViewById(R.id.creation_recipe_duration);
        mRecipePortionsView = (EditText) findViewById(R.id.creation_recipe_portions);
        imageView = (ImageView) findViewById(R.id.imageView);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button mUploadImageButton = (Button) findViewById(R.id.button_upload_image);

        mUploadImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCreateRecipe();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        textIn = (AutoCompleteTextView)findViewById(R.id.textin);
        buttonAdd = (Button)findViewById(R.id.add);
        container = (LinearLayout) findViewById(R.id.container);
        reList = (TextView)findViewById(R.id.relist);
        reList.setMovementMethod(new ScrollingMovementMethod());
        info = (TextView)findViewById(R.id.info);
        info.setMovementMethod(new ScrollingMovementMethod());

        textInIngredient = (AutoCompleteTextView)findViewById(R.id.textinIngredient);
        buttonAddIngredient = (Button)findViewById(R.id.addIngredient);
        containerIngredient = (LinearLayout) findViewById(R.id.containerIngredient);
        reListIngredient = (TextView)findViewById(R.id.relistIngredient);
        reListIngredient.setMovementMethod(new ScrollingMovementMethod());
        infoIngredient = (TextView)findViewById(R.id.infoIngredient);
        infoIngredient.setMovementMethod(new ScrollingMovementMethod());
        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                View focusView = null;
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.dynamic_add_row, null);

                AutoCompleteTextView textOut = (AutoCompleteTextView)addView.findViewById(R.id.textout);
                if (TextUtils.isEmpty(textIn.getText())) {
                    textIn.setError(getString(R.string.error_field_required));
                    focusView = textIn;

                }else{
                    textOut.setAdapter(adapter);
                    textOut.setText(textIn.getText().toString());
                    textIn.setText("");
                    Button buttonRemove = (Button)addView.findViewById(R.id.remove);

                    final View.OnClickListener thisListener = new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            info.append("thisListener called:\t" + this + "\n");
                            info.append("Remove addView: " + addView + "\n\n");
                            ((LinearLayout)addView.getParent()).removeView(addView);

                            listAllAddView();
                        }
                    };

                    buttonRemove.setOnClickListener(thisListener);
                    container.addView(addView);
                    info.append(
                            "thisListener:\t" + thisListener + "\n"
                                    + "addView:\t" + addView + "\n\n"
                    );
                    listAllAddView();
                }




            }
        });

        buttonAddIngredient.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                View focusView = null;
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.dynamic_add_row_ingredient, null);

                AutoCompleteTextView textOutIngredient = (AutoCompleteTextView)addView.findViewById(R.id.textoutIngredient);
                if (TextUtils.isEmpty(textInIngredient.getText())) {

                    textInIngredient.setError(getString(R.string.error_field_required));
                    focusView = textInIngredient;

                }else{
                    textOutIngredient.setText(textInIngredient.getText().toString());
                    Button buttonRemoveIngredient = (Button)addView.findViewById(R.id.removeIngredient);
                    textInIngredient.setText("");
                    final View.OnClickListener thisListener = new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            infoIngredient.append("thisListener called:\t" + this + "\n");
                            infoIngredient.append("Remove addView: " + addView + "\n\n");
                            ((LinearLayout)addView.getParent()).removeView(addView);

                            listAllAddViewIngredient();
                        }
                    };

                    buttonRemoveIngredient.setOnClickListener(thisListener);
                    containerIngredient.addView(addView);
                    infoIngredient.append(
                            "thisListener:\t" + thisListener + "\n"
                                    + "addView:\t" + addView + "\n\n"
                    );
                    listAllAddViewIngredient();
                }
            }
        });

        textInStep = (AutoCompleteTextView)findViewById(R.id.textinStep);
        buttonAddStep = (Button)findViewById(R.id.addStep);
        containerStep = (LinearLayout) findViewById(R.id.containerStep);
        reListStep = (TextView)findViewById(R.id.relistStep);
        reListStep.setMovementMethod(new ScrollingMovementMethod());
        infoStep = (TextView)findViewById(R.id.infoStep);
        infoStep.setMovementMethod(new ScrollingMovementMethod());

        buttonAddStep.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                View focusView = null;
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.dynamic_add_row_step, null);

                AutoCompleteTextView textOutStep = (AutoCompleteTextView)addView.findViewById(R.id.textoutStep);
                if (TextUtils.isEmpty(textInStep.getText())) {
                    textInStep.setError(getString(R.string.error_field_required));
                    focusView = textInStep;

                }else{
                    textOutStep.setText(textInStep.getText().toString());
                    Button buttonRemoveStep = (Button)addView.findViewById(R.id.removeStep);
                    textInStep.setText("");
                    final View.OnClickListener thisListener = new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            infoStep.append("thisListener called:\t" + this + "\n");
                            infoStep.append("Remove addView: " + addView + "\n\n");
                            ((LinearLayout)addView.getParent()).removeView(addView);

                            listAllAddViewStep();
                        }
                    };

                    buttonRemoveStep.setOnClickListener(thisListener);
                    containerStep.addView(addView);
                    infoStep.append(
                            "thisListener:\t" + thisListener + "\n"
                                    + "addView:\t" + addView + "\n\n"
                    );
                    listAllAddViewStep();
                }
            }
        });
    }



    private void listAllAddView(){
        reList.setText("");

        int childCount = container.getChildCount();
        for(int i=0; i<childCount; i++){
            View thisChild = container.getChildAt(i);
            reList.append(thisChild + "\n");

            AutoCompleteTextView childTextView = (AutoCompleteTextView) thisChild.findViewById(R.id.textout);
            String childTextViewValue = childTextView.getText().toString();
            reList.append("= " + childTextViewValue + "\n");
        }
    }

    private void listAllAddViewIngredient(){
        reListIngredient.setText("");

        int childCount = containerIngredient.getChildCount();
        for(int i=0; i<childCount; i++){
            View thisChild = containerIngredient.getChildAt(i);
            reListIngredient.append(thisChild + "\n");

            AutoCompleteTextView childTextViewIngredient = (AutoCompleteTextView) thisChild.findViewById(R.id.textoutIngredient);
            String childTextViewValueIngredient = childTextViewIngredient.getText().toString();
            reList.append("= " + childTextViewValueIngredient + "\n");
        }
    }

    private void listAllAddViewStep(){
        reListStep.setText("");

        int childCount = containerStep.getChildCount();
        for(int i=0; i<childCount; i++){
            View thisChild = containerStep.getChildAt(i);
            reListStep.append(thisChild + "\n");

            AutoCompleteTextView childTextViewStep = (AutoCompleteTextView) thisChild.findViewById(R.id.textoutStep);
            String childTextViewValueStep = childTextViewStep.getText().toString();
            reList.append("= " + childTextViewValueStep + "\n");
        }
    }



    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imagePath = toBase64(bitmap);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptCreateRecipe() {
        if (mAuthTask != null) {
            return;
        }

        mRecipeNameView.setError(null);
        mRecipeDescriptionView.setError(null);
        mRecipeDurationView.setError(null);
        mRecipePortionsView.setError(null);


        String recipeName = mRecipeNameView.getText().toString();
        String recipeDescription = mRecipeDescriptionView.getText().toString();
        String recipeDuration = mRecipeDurationView.getText().toString();
        String recipePortions = mRecipePortionsView.getText().toString();


        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(recipeName)) {
            mRecipeNameView.setError(getString(R.string.error_field_required));
            focusView = mRecipeNameView;
            cancel = true;
        } else if(!isRecipeNameValid(recipeName)) {
            mRecipeNameView.setError(getString(R.string.error_field_too_long));
            focusView = mRecipeNameView;
            cancel = true;

        } else if (TextUtils.isEmpty(recipeDescription)) {
            mRecipeDescriptionView.setError(getString(R.string.error_field_required));
            focusView = mRecipeDescriptionView;
            cancel = true;
        } else if (TextUtils.isEmpty(recipeDuration)) {
            mRecipeDurationView.setError(getString(R.string.error_field_required));
            focusView = mRecipeDurationView;
            cancel = true;
        }else if(!isRecipeDurationValid(recipeDuration)) {
            mRecipeDurationView.setError(getString(R.string.error_field_too_long));
            focusView = mRecipeDurationView;
            cancel = true;

        }else if (TextUtils.isEmpty(recipePortions)) {
            mRecipePortionsView.setError(getString(R.string.error_field_required));
            focusView = mRecipePortionsView;
            cancel = true;
        }else if(!isRecipePortionsValid(recipePortions)) {
            mRecipePortionsView.setError(getString(R.string.error_field_too_long));
            focusView = mRecipePortionsView;
            cancel = true;
        }else if(container.getChildCount()==0){
            textIn.setError("Introduce al menos una etiqueta");
            focusView = textIn;
            cancel= true;
        }else if(containerIngredient.getChildCount()==0){
            textInIngredient.setError("Introduce al menos un ingrediente");
            focusView = textInIngredient;
            cancel= true;
        }else if(containerStep.getChildCount()==0){
            textInStep.setError("Introduce al menos un paso para realizar la receta");
            focusView = textInStep;
            cancel= true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            String tags = "";
            for(int i = 0; i<container.getChildCount();i++){
                RelativeLayout rl = (RelativeLayout)container.getChildAt(i);
                AutoCompleteTextView atv = (AutoCompleteTextView) rl.getChildAt(1);
                tags = tags + atv.getText().toString() +";";
            }

            String ingredients = "";
            for(int i = 0; i<containerIngredient.getChildCount();i++){
                RelativeLayout rl = (RelativeLayout)containerIngredient.getChildAt(i);
                AutoCompleteTextView atv = (AutoCompleteTextView) rl.getChildAt(1);
                ingredients = ingredients + atv.getText().toString() +";";
            }

            String steps = "";
            for(int i = 0; i<containerStep.getChildCount();i++){
                RelativeLayout rl = (RelativeLayout)containerStep.getChildAt(i);
                AutoCompleteTextView atv = (AutoCompleteTextView) rl.getChildAt(1);
                steps = steps + atv.getText().toString() +";";
            }

            createRecipe(recipeName,recipeDescription, recipeDuration, recipePortions, tags, ingredients, steps);

            mAuthTask = new UserLoginTask(recipeName, recipeDuration);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isRecipeNameValid(String recipeName) {
        return recipeName.length()<99;
    }

    private boolean isRecipeDurationValid(String recipeDuration) {
        return recipeDuration.length()<45;
    }

    private boolean isRecipePortionsValid(String recipePortions) {
        return recipePortions.length()<99;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }


    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void createRecipe(String recipeName, String recipeDescription, String recipeDuration, String recipePortions, String tags, String ingredients, String steps){
        Call<Object> call = receteameApiInterface.createRecipe("Bearer "+UserToken.getUserToken(), recipeName, recipeDescription, recipeDuration,recipePortions, tags, ingredients, steps,imagePath);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                //progress.dismiss();
                Log.i(LOG_TAG, response + " RETURN");
                Log.i(LOG_TAG, response.code() + " CODE");
                Log.i(LOG_TAG, response.body()+" BODY");


                if(response.code()==200){
                    Toast.makeText(getApplicationContext(),
                            "OK", Toast.LENGTH_LONG)
                            .show();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "¡Error! Por favor revisa tus credenciales", Toast.LENGTH_LONG)
                            .show();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());

            }
        });
    }

    private void getTags(){
        Call<List<RecipeTag>> call = receteameApiInterface.getTags();
        call.enqueue(new Callback<List<RecipeTag>>() {
            @Override
            public void onResponse(Call<List<RecipeTag>> call, Response<List<RecipeTag>> response) {
                if(response.code()==200){
                    tags = response.body();
                    TAGS = new String[tags.size()];
                    for(int i=0;i<tags.size();i++) {
                        TAGS[i] = tags.get(i).getTag();
                    }
                    adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_dropdown_item_1line, TAGS);
                    textIn.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "¡Error!", Toast.LENGTH_LONG)
                            .show();
                }
            }
            @Override
            public void onFailure(Call<List<RecipeTag>> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());

            }
        });
    }


}

