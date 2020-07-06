package com.amply.recipefinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amply.recipefinder.R;
import com.amply.recipefinder.adapter.IngredientsDetailsAdapter;
import com.amply.recipefinder.models.Recipes;
import com.amply.recipefinder.utils.SharedPreferencesManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RecipeDetailActivity extends AppCompatActivity implements IngredientsDetailsAdapter.ItemClickListener {

    private String RECIPES_DETAIL_URL = "https://us-central1-useful-patrol-258007.cloudfunctions.net/function-2";
    private String recipeIdStr = "";

    private TextView mTextRecipeName, mTextShrtDes, mTextPerpTime, mTextRecipeNutrition, mTextProcedure, mTxtDone;
    private ImageView mImgRecipeImage, mImageURL;
    private CardView mCardRecipeImage;
    private String procedureStr = "";

    private ArrayList<Recipes> recipesList;
    private RecyclerView mRecyclerIntrs;
    private IngredientsDetailsAdapter mIngrAdapter;
    private LinearLayout mLayoutNoImage;
    private AVLoadingIndicatorView avi;

    private Recipes recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mTextRecipeName = findViewById(R.id.tv_detail_recipe_name);
        mTextShrtDes = findViewById(R.id.tv_detail_recipe_shrt_des);
        mTextPerpTime = findViewById(R.id.tv_detail_recipe_perp);
        mTextRecipeNutrition = findViewById(R.id.tv_detail_recipe_nutr);
        mTextProcedure = findViewById(R.id.tv_detail_recipe_procedure);
        mImgRecipeImage = findViewById(R.id.img_recipe_detail);
        mImageURL = findViewById(R.id.img_recipe);
        mCardRecipeImage = findViewById(R.id.card_view_recipe_image);
        mTxtDone = findViewById(R.id.tv_done);
        mRecyclerIntrs = findViewById(R.id.recycler_detail_ingr);
        mLayoutNoImage = findViewById(R.id.ll_no_image);
        avi = findViewById(R.id.avi);

        recipeIdStr = getIntent().getExtras().getString("RECIPE_ID_EXTRA");

//        Toast.makeText(this, ""+recipeIdStr, Toast.LENGTH_SHORT).show();

        mLayoutNoImage.setVisibility(View.GONE);

        try {
            getRecipesData(recipeIdStr);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    private void getRecipesData(String ingredientStr) throws JSONException, UnsupportedEncodingException {
        avi.show();
        recipesList = new ArrayList<>();
        AsyncHttpClient client = new AsyncHttpClient();
        Log.v("ingr_pass", ingredientStr);

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("recipe_id", "" + ingredientStr); //{"ingredients":"chicken,onion"}
        StringEntity entity = new StringEntity(jsonParams.toString());
        client.setTimeout(15 * 1000); // 15 sec timeout
        client.post(RecipeDetailActivity.this, RECIPES_DETAIL_URL, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                avi.hide();
                Gson gson = new Gson();
                String jsonOutput = new String(responseBody);
                Type listType = new TypeToken<ArrayList<Recipes>>() {
                }.getType();
                recipesList = gson.fromJson(jsonOutput, listType);
                recipes = recipesList.get(0); //gson.fromJson(jsonOutput, Recipes.class);
                setRecipeUI(recipes);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                avi.hide();
            }
        });


        mTxtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveAttemptedRecipe();

                startActivity(new Intent(RecipeDetailActivity.this, IngredientsActivity.class));
                finish();
            }
        });

    }

    private void saveAttemptedRecipe() {

        if (SharedPreferencesManager.getAttempedRecipes(RecipeDetailActivity.this) != null) {
            if (SharedPreferencesManager.getAttempedRecipes(RecipeDetailActivity.this).size() != 0) {
                recipesList.addAll(SharedPreferencesManager.getAttempedRecipes(RecipeDetailActivity.this));
            }
        }

        SharedPreferencesManager.saveAttempedRecipes(RecipeDetailActivity.this, recipesList);
    }

    private void setRecipeUI(Recipes recipes) {
        avi.hide();


        mTextRecipeName.setText("" + makeCaptial(recipes.getRecipeName()));
        mTextShrtDes.setText("" + makeCaptial(recipes.getRecipeShortDescription()));

        if (!TextUtils.isEmpty(recipes.getRecipePreptime())) {
            mTextPerpTime.setText("" + recipes.getRecipePreptime());
        } else {
            mTextPerpTime.setText("No Data");
        }

        if (!TextUtils.isEmpty(recipes.getRecipeNutrition())) {
            mTextRecipeNutrition.setText("" + recipes.getRecipeNutrition());
        } else {
            mTextRecipeNutrition.setText("No Data");
        }

        for (int i = 0; i < recipes.getRecipeProcedure().size(); i++) {
//            String sourceString = "<b>" + boldText + "</b> " + normalText;
            procedureStr += "<b>" + "Step:" + String.valueOf(i + 1) + "</b>" + " <br />" + makeCaptial(recipes.getRecipeProcedure().get(i).getDesc()) + " <br /><br />";
        }

        mTextProcedure.setText(Html.fromHtml(procedureStr));

        if (recipes.getImage().size() <= 1) {
            if (!TextUtils.isEmpty("" + recipes.getImage().get(0).getUrl())) {
                mLayoutNoImage.setVisibility(View.GONE);

                System.out.println("image_url::::" + recipes.getImage().get(0).getUrl());
                System.out.println("image_url__recipeID::::" + recipeIdStr);
                Glide.with(this)
                        .load("" + recipes.getImage().get(0).getUrl())
                        .placeholder(R.drawable.splash_screen)
                        .into(mImageURL);
            } else {
                mLayoutNoImage.setVisibility(View.VISIBLE);
            }
        } else {
            Glide.with(this)
                    .load("")
                    .placeholder(R.drawable.splash_screen)
                    .into(mImageURL);
//            mLayoutNoImage.setVisibility(View.VISIBLE);
        }


        mRecyclerIntrs.setLayoutManager(new GridLayoutManager(this, 2));
        mIngrAdapter = new IngredientsDetailsAdapter(this, recipes.getRecipeIngredient());
        mIngrAdapter.setClickListener(RecipeDetailActivity.this);
        mRecyclerIntrs.setAdapter(mIngrAdapter);

    }

    private String makeCaptial(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(this, ""+recipesList.get(0).getRecipeIngredient().get(position).getData(), Toast.LENGTH_SHORT).show();
    }
}