package es.upm.miw.myapplication.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe  {
    @SerializedName("idRecipe")
    @Expose
    private Integer idRecipe;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("portions")
    @Expose
    private String portions;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("recipe_tags")
    @Expose
    private List<RecipeTag> recipeTags = null;
    @SerializedName("recipe_ingredients")
    @Expose
    private List<RecipeIngredient> recipeIngredients = null;
    @SerializedName("recipe_steps")
    @Expose
    private List<RecipeStep> recipeSteps = null;



    public Integer getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(Integer idRecipe) {
        this.idRecipe = idRecipe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public List<RecipeTag> getRecipeTags() {
        return recipeTags;
    }

    public void setRecipeTags(List<RecipeTag> recipeTags) {
        this.recipeTags = recipeTags;
    }

    public List<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public List<RecipeStep> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(List<RecipeStep> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }




    @Override
    public String toString() {
        return "Recipe{" +
                "idRecipe=" + idRecipe +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", createdBy=" + createdBy +
                ", recipeTags=" + recipeTags +
                ", recipeIngredients=" + recipeIngredients +
                ", recipeSteps=" + recipeSteps +
                '}';
    }

    public String getPortions() {
        return portions;
    }

    public void setPortions(String portions) {
        this.portions = portions;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}