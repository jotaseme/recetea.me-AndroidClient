package es.upm.miw.myapplication.Models;


import java.io.Serializable;
import java.util.List;

public class ShoppingList implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idRecipe;
    private String recipe_name;
    private List<RecipeIngredient> ingredients;
    private static final String FICHERO = "data/data/es.upm.miw.myapplication/files/shoppingList.txt";

    public ShoppingList(int idRecipe, String recipe_name, List<RecipeIngredient> ingredients) {

        this.idRecipe =  idRecipe;
        this.recipe_name = recipe_name;
        this.ingredients = ingredients;
    }

    public int getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(int idRecipe) {
        this.idRecipe = idRecipe;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}