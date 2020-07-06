package com.amply.recipefinder.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.amply.recipefinder.models.Recipes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferencesManager {

    private static final String APP_SETTINGS = "APP_SETTINGS";


    // properties
    private static final String INGREDIENTS = "INGREDIENTS";
    private static final String RECIPES = "RECIPES";
    private static final String ATT_RECIPES = "ATT_RECIPES";
    private static final String USER = "USER";
    private static final String USER_BOOL = "USER_BOOL";
    private static final String USER_IMG = "USER_IMG";
    private static final String MODEL_VERSION = "MODEL_VERSION";
    // other properties...


    public SharedPreferencesManager() {
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static ArrayList<String> getSavedIngredients(Context context) {
        Gson gson = new Gson();
        String json = getSharedPreferences(context).getString(INGREDIENTS, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveIngredients(Context context, ArrayList<String> list) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(INGREDIENTS, json);
        editor.commit();
    }

    public static void deleteAllSharePrefs(Context context) {
        getSharedPreferences(context).edit().remove(INGREDIENTS).apply();
//        getSharedPreferences(context).edit().clear().apply();

//        SharedPreferences preferences = getSharedPreferences(context);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.commit();

    }

    public static ArrayList<Recipes> getSavedRecipes(Context context) {
        Gson gson = new Gson();
        String json = getSharedPreferences(context).getString(RECIPES, null);
        Type type = new TypeToken<ArrayList<Recipes>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveRecipes(Context context, ArrayList<Recipes> list) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(RECIPES, json);
        editor.commit();
    }

    public static ArrayList<Recipes> getAttempedRecipes(Context context) {
        Gson gson = new Gson();
        String json = getSharedPreferences(context).getString(ATT_RECIPES, null);
        Type type = new TypeToken<ArrayList<Recipes>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveAttempedRecipes(Context context, ArrayList<Recipes> list) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(ATT_RECIPES, json);
        editor.commit();
    }

    public static String getFirebaseUser(Context context) {
        Gson gson = new Gson();
        String json = getSharedPreferences(context).getString(USER, null);
//        Type type = new TypeToken<ArrayList<Recipes>>() {}.getType();
//        return gson.fromJson(json, FirebaseUser.class);
        return json;
    }


    public static void saveFirebaseUser(Context context, String userEmail) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(user);
        editor.putString(USER, userEmail);
        editor.commit();
    }

    public static boolean getUserCheck(Context context) {
        Gson gson = new Gson();
        boolean json = getSharedPreferences(context).getBoolean(USER_BOOL, false);
        return json;
    }

    public static void saveUserCheck(Context context, boolean user) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(user);
        editor.putBoolean(USER_BOOL, user);
        editor.commit();
    }

    public static int getUserProfileImage(Context context) {
        Gson gson = new Gson();
        int json = getSharedPreferences(context).getInt(USER_IMG, 0);
        return json;
    }

    public static void setUserProfileImage(Context context, int count) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(USER_IMG, count);
        editor.commit();
    }

    public static int getModelVersion(Context context) {
        Gson gson = new Gson();
        int json = getSharedPreferences(context).getInt(MODEL_VERSION, 0);
        return json;
    }

    public static void setModelVersion(Context context, int count) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(MODEL_VERSION, count);
        editor.commit();
    }





}
