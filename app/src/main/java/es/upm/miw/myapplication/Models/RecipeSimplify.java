package es.upm.miw.myapplication.Models;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import es.upm.miw.myapplication.Utils.AppendingObjectOutputStream;

public class RecipeSimplify implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FAVS = "/data/data/es.upm.miw.myapplication/files/favList.txt";

    private int idRecipe;
    private String recipe_name;
    private String url_image;

    public RecipeSimplify(int idRecipe, String recipe_name, String url_image) {
        this.idRecipe = idRecipe;
        this.recipe_name = recipe_name;
        this.url_image = url_image;
    }


    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public int getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(int idRecipe) {
        this.idRecipe = idRecipe;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public void saveRecipe(){
        try
        {
            File file;
            file = new File(FAVS);
            FileOutputStream fos = new FileOutputStream(FAVS, true);

            if (file.length() == 0) {
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(this);
                out.close();
            } else {
                ObjectOutputStream out = new AppendingObjectOutputStream(fos);
                out.writeObject(this);
                out.close();
            }

        }catch(IOException i)
        {
            i.printStackTrace();
        }
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.detach(this).attach(this).commit();
    }
}
