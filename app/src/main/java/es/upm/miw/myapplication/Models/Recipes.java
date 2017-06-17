package es.upm.miw.myapplication.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipes {

    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("prev")
    @Expose
    private Integer prev;
    @SerializedName("next")
    @Expose
    private Integer next;
    @SerializedName("recipes")
    @Expose
    private List<Recipe> recipes = null;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPrev() {
        return prev;
    }

    public void setPrev(Integer prev) {
        this.prev = prev;
    }

    public Integer getNext() {
        return next;
    }

    public void setNext(Integer next) {
        this.next = next;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

}

