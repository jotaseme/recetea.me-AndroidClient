package es.upm.miw.myapplication.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import es.upm.miw.myapplication.Adapters.IngredientsAdapter;
import es.upm.miw.myapplication.Models.RecipeIngredient;
import es.upm.miw.myapplication.Models.ShoppingList;
import es.upm.miw.myapplication.R;

public class ThreeFragment extends Fragment{

    private static final String FICHERO = "/data/data/es.upm.miw.myapplication/files/shoppingList.txt";

    private IngredientsAdapter adapter;
    List<RecipeIngredient> shoppingIngredients = new ArrayList<>();
    ListView ingredientsView;
    public static final String LOG_TAG = "TFM2017";

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_three, container, false);
        adapter = new IngredientsAdapter(getActivity().getApplicationContext(), this.shoppingIngredients, "shopping");
        LinearLayout myRoot = (LinearLayout) rootView.findViewById(R.id.linear_parent);
        LinearLayout a = new LinearLayout(getActivity().getApplicationContext());
        a.setOrientation(LinearLayout.HORIZONTAL);
        ingredientsView = (ListView) rootView.findViewById(R.id.ingredientsListView);
        ingredientsView.setAdapter(adapter);
        ArrayList<ShoppingList> shoppingList = loadSerializedObject();

        if(shoppingList.size()>0){
            for (int i = 0; i <shoppingList.size(); i++) {
                for (int j = 0; j <shoppingList.get(i).getIngredients().size(); j++) {
                    adapter.addAll(shoppingList.get(i).getIngredients().get(j));
                }
            }
            adapter.notifyDataSetChanged();
            justifyListViewHeightBasedOnChildren(ingredientsView);
        }

        FloatingActionButton floatingActionButton = (FloatingActionButton)  rootView.findViewById(R.id.trash);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteShoppingList();
                //Toast.makeText(getActivity(), R.string.action, Toast.LENGTH_SHORT).show();
            }
        });

        myRoot.addView(a);
        return rootView;
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

    public ArrayList<ShoppingList> loadSerializedObject() {
        File yourFile = new File(FICHERO);
        try {
            yourFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<ShoppingList> shoppingList = new ArrayList<>();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(FICHERO);
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
                shoppingList.add((ShoppingList) in.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shoppingList;
    }

    public void deleteShoppingList() {
        File file = new File(FICHERO);
        file.delete();
        Toast.makeText(
                getActivity().getApplicationContext(),
                R.string.shoppingListDeletion,
                Toast.LENGTH_SHORT
        ).show();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        shoppingIngredients.clear();
        adapter.notifyDataSetChanged();
    }

}
