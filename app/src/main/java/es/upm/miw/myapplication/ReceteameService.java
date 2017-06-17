package es.upm.miw.myapplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceteameService {

    private final static String URL_RECURSO = "http://localhost:8000/recipes";

    public static final Retrofit retrofit = new Retrofit
            .Builder()
            .baseUrl(URL_RECURSO)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ReceteameApiInterface receteameClient = retrofit.create(ReceteameApiInterface.class);
}