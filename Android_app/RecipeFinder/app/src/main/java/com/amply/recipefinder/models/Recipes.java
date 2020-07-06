package com.amply.recipefinder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipes {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("recipe_url")
    @Expose
    private String recipeUrl;
    @SerializedName("recipe_name")
    @Expose
    private String recipeName;
    @SerializedName("recipe_short_description")
    @Expose
    private String recipeShortDescription;
    @SerializedName("image")
    @Expose
    private List<RecipeImage> image = null;
    @SerializedName("recipe_calories")
    @Expose
    private Integer recipeCalories;
    @SerializedName("recipe_preptime")
    @Expose
    private String recipePreptime;
    @SerializedName("recipe_servings")
    @Expose
    private Integer recipeServings;
    @SerializedName("recipe_ingredient")
    @Expose
    private List<RecipeIngredient> recipeIngredient = null;
    @SerializedName("recipe_description")
    @Expose
    private String recipeDescription;
    @SerializedName("recipe_procedure")
    @Expose
    private List<RecipeProcedure> recipeProcedure = null;
    @SerializedName("recipe_nutrition")
    @Expose
    private String recipeNutrition;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeShortDescription() {
        return recipeShortDescription;
    }

    public void setRecipeShortDescription(String recipeShortDescription) {
        this.recipeShortDescription = recipeShortDescription;
    }

    public List<RecipeImage> getImage() {
        return image;
    }

    public void setImage(List<RecipeImage> image) {
        this.image = image;
    }

    public Integer getRecipeCalories() {
        return recipeCalories;
    }

    public void setRecipeCalories(Integer recipeCalories) {
        this.recipeCalories = recipeCalories;
    }

    public String getRecipePreptime() {
        return recipePreptime;
    }

    public void setRecipePreptime(String recipePreptime) {
        this.recipePreptime = recipePreptime;
    }

    public Integer getRecipeServings() {
        return recipeServings;
    }

    public void setRecipeServings(Integer recipeServings) {
        this.recipeServings = recipeServings;
    }

    public List<RecipeIngredient> getRecipeIngredient() {
        return recipeIngredient;
    }

    public void setRecipeIngredient(List<RecipeIngredient> recipeIngredient) {
        this.recipeIngredient = recipeIngredient;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public List<RecipeProcedure> getRecipeProcedure() {
        return recipeProcedure;
    }

    public void setRecipeProcedure(List<RecipeProcedure> recipeProcedure) {
        this.recipeProcedure = recipeProcedure;
    }

    public String getRecipeNutrition() {
        return recipeNutrition;
    }

    public void setRecipeNutrition(String recipeNutrition) {
        this.recipeNutrition = recipeNutrition;
    }



//
//    private String category;
//    private String recipeUrl;
//    private String recipeName;
//    private String recipeShortDescription;
//    private ArrayList<RecipeImage> image = null;
//    private Integer recipeCalories;
//    private String recipePreptime;
//    private Integer recipeServings;
//    private ArrayList<RecipeIngredient> recipeIngredient = null;
//    private String recipeDescription;
//    private ArrayList<RecipeProcedure> recipeProcedure = null;
//    private String recipeNutrition;
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public String getRecipeUrl() {
//        return recipeUrl;
//    }
//
//    public void setRecipeUrl(String recipeUrl) {
//        this.recipeUrl = recipeUrl;
//    }
//
//    public String getRecipeName() {
//        return recipeName;
//    }
//
//    public void setRecipeName(String recipeName) {
//        this.recipeName = recipeName;
//    }
//
//    public String getRecipeShortDescription() {
//        return recipeShortDescription;
//    }
//
//    public void setRecipeShortDescription(String recipeShortDescription) {
//        this.recipeShortDescription = recipeShortDescription;
//    }
//
//    public ArrayList<RecipeImage> getImage() {
//        return image;
//    }
//
//    public void setImage(ArrayList<RecipeImage> image) {
//        this.image = image;
//    }
//
//    public Integer getRecipeCalories() {
//        return recipeCalories;
//    }
//
//    public void setRecipeCalories(Integer recipeCalories) {
//        this.recipeCalories = recipeCalories;
//    }
//
//    public String getRecipePreptime() {
//        return recipePreptime;
//    }
//
//    public void setRecipePreptime(String recipePreptime) {
//        this.recipePreptime = recipePreptime;
//    }
//
//    public Integer getRecipeServings() {
//        return recipeServings;
//    }
//
//    public void setRecipeServings(Integer recipeServings) {
//        this.recipeServings = recipeServings;
//    }
//
//    public ArrayList<RecipeIngredient> getRecipeIngredient() {
//        return recipeIngredient;
//    }
//
//    public void setRecipeIngredient(ArrayList<RecipeIngredient> recipeIngredient) {
//        this.recipeIngredient = recipeIngredient;
//    }
//
//    public String getRecipeDescription() {
//        return recipeDescription;
//    }
//
//    public void setRecipeDescription(String recipeDescription) {
//        this.recipeDescription = recipeDescription;
//    }
//
//    public ArrayList<RecipeProcedure> getRecipeProcedure() {
//        return recipeProcedure;
//    }
//
//    public void setRecipeProcedure(ArrayList<RecipeProcedure> recipeProcedure) {
//        this.recipeProcedure = recipeProcedure;
//    }
//
//    public String getRecipeNutrition() {
//        return recipeNutrition;
//    }
//
//    public void setRecipeNutrition(String recipeNutrition) {
//        this.recipeNutrition = recipeNutrition;
//    }


}
