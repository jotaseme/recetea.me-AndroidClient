package es.upm.miw.myapplication;

import java.util.List;

import es.upm.miw.myapplication.Models.Recipe;
import es.upm.miw.myapplication.Models.RecipeComment;
import es.upm.miw.myapplication.Models.RecipeTag;
import es.upm.miw.myapplication.Models.Recipes;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReceteameApiInterface {

    @GET("recipes")
    Call<Recipes> getRecipes(@Query("page") int pageIndex);

    @GET("recipes/{id_recipe}")
    Call<Recipe> getRecipe(@Path("id_recipe") int id_recipe);

    @GET("recipes/random")
    Call<List<Recipe>> getRandomRecipe();

    @FormUrlEncoded
    @POST("users")
    Call<Object> registerUser (@Field("user_form[name]") String name,
                                  @Field("user_form[email]") String email,
                                  @Field("user_form[password]") String password);

    @FormUrlEncoded
    @POST("users/auth")
        Call<Object> loginUser (@Field("login_form[email]") String email,
                            @Field("login_form[password]") String password);
    @FormUrlEncoded
    @POST("recipes")
    Call<Object> createRecipe (@Header("Authorization") String authorization,
                               @Field("recipe_form[name]") String recipeName,
                               @Field("recipe_form[description]") String recipeDescription,
                               @Field("recipe_form[duration]") String recipeDuration,
                               @Field("recipe_form[portions]") String recipePortions,
                               @Field("recipe_form[recipe_tags][0][tag]") String recipeTags,
                               @Field("recipe_form[recipe_ingredients][0][name]") String recipeIngredients,
                               @Field("recipe_form[recipe_steps][0][description]") String recipeSteps,
                               @Field("recipe_form[image]") String image);
    @GET("tags")
    Call<List<RecipeTag>> getTags();

    @GET("recipes")
    Call<Recipes> getRecipesByName(@Query("recipe_filter[name]") String name,
                                   @Query("page") int pageIndex);

    @GET("recipes/user-profile")
    Call<List<Recipe>> getSelfRecipes(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("recipes/{id_recipe}/comments")
    Call<Object> createComment (@Header("Authorization") String authorization,
                               @Path("id_recipe") int id_recipe,
                               @Field("form_comment[title]") String title,
                               @Field("form_comment[description]") String description);

    @GET("recipes/{id_recipe}/comments")
    Call<List<RecipeComment>> getRecipeComments(@Header("Authorization") String authorization,
                                                      @Path("id_recipe") int id_recipe);
}
