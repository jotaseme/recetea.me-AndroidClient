package es.upm.miw.myapplication.Models;


import java.io.Serializable;

public class RecipeSimplify implements Serializable {
    private static final long serialVersionUID = 1L;

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
}
