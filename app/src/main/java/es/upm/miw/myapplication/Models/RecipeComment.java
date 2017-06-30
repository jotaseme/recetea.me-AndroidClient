package es.upm.miw.myapplication.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeComment {

    @SerializedName("idComment")
    @Expose
    private Integer idComment;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("idUser")
    @Expose
    private CreatedBy idUser;

    public Integer getIdComment() {
        return idComment;
    }

    public void setIdComment(Integer idComment) {
        this.idComment = idComment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public CreatedBy getIdUser() {
        return idUser;
    }

    public void setIdUser(CreatedBy idUser) {
        this.idUser = idUser;
    }

}
