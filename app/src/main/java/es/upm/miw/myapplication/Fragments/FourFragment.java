package es.upm.miw.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import es.upm.miw.myapplication.Models.RecipeSimplify;
import es.upm.miw.myapplication.R;
import es.upm.miw.myapplication.Adapters.ReciclerSimplifyAdapter;
import es.upm.miw.myapplication.RecyclerItemClickListener;
import es.upm.miw.myapplication.ShowRecipeActivity;

public class FourFragment extends Fragment{
    private static final String FAVS = "/data/data/es.upm.miw.myapplication/files/favList.txt";
    public static final String LOG_TAG = "TFM2017";
    public static final String CLAVE = LOG_TAG;
    private ArrayList<RecipeSimplify> recipesSimplify = new ArrayList<>();
    ReciclerSimplifyAdapter adapter;
    public FourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_four, container, false);
        RecyclerView MyRecyclerView;
        recipesSimplify = loadSerializedObject();

        MyRecyclerView = (RecyclerView) rootView.findViewById(R.id.simpleCardView);
        MyRecyclerView.setHasFixedSize(true);
        adapter = new ReciclerSimplifyAdapter(recipesSimplify);
        MyRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        MyRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        MyRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity().getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        Intent intent = new Intent(getActivity().getBaseContext(), ShowRecipeActivity.class);
                        intent.putExtra(CLAVE, recipesSimplify.get(i).getIdRecipe());
                        startActivity(intent);
                    }
                }));
        FloatingActionButton floatingActionButton = (FloatingActionButton)  rootView.findViewById(R.id.trash);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFavRecipesList();
                //Toast.makeText(getActivity(), R.string.action, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    public ArrayList<RecipeSimplify> loadSerializedObject() {
        File yourFile = new File(FAVS);

        try {
            yourFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<RecipeSimplify> recipesSimplify = new ArrayList<>();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(FAVS);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (true)
                recipesSimplify.add((RecipeSimplify) in.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipesSimplify;
    }

    public void deleteFavRecipesList() {
        File file = new File(FAVS);
        file.delete();
        Toast.makeText(
                getActivity().getApplicationContext(),
                R.string.favRecipesListDeletion,
                Toast.LENGTH_SHORT
        ).show();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        recipesSimplify.clear();
        adapter.notifyDataSetChanged();
    }

}
