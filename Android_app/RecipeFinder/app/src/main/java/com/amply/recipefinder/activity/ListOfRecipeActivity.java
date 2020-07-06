package com.amply.recipefinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amply.recipefinder.R;
import com.amply.recipefinder.adapter.AlertIngredientsAdapter;
import com.amply.recipefinder.adapter.AlertRemoveIngredientsAdapter;
import com.amply.recipefinder.adapter.RecipesAdapter;
import com.amply.recipefinder.models.Recipes;
import com.amply.recipefinder.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ListOfRecipeActivity extends AppCompatActivity implements RecipesAdapter.RecipeClickListener, AlertIngredientsAdapter.ItemClickListener, AlertRemoveIngredientsAdapter.ItemClickListener {

    private RecyclerView mRecyclerRecipes;
    private String RECIPES_URL = "https://us-central1-useful-patrol-258007.cloudfunctions.net/function-1";
    private ArrayList<String> searchedItems;
    private String ingrListStr = "";

    private RecipesAdapter adapter;
    private ArrayList<Recipes> recipesList;

    private LinearLayout llNoData;
    private LinearLayout llNoIntetnet;
    private AVLoadingIndicatorView avi;

    private TextView mTextProgress;
    private EditText mEditSearchRecipe;
    private TextView mTextUpdateIngr;
    List<String> allRecipeList = new ArrayList<>();

    private RecyclerView recyclerAlertIngr, recyclerSearchedIngr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_recipe);

        mRecyclerRecipes = findViewById(R.id.recycler_recipes);
        mRecyclerRecipes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        llNoData = findViewById(R.id.ll_no_data);
        mEditSearchRecipe = findViewById(R.id.et_search_recipe);
        mTextUpdateIngr = findViewById(R.id.tv_update_ingr);
        mTextProgress = findViewById(R.id.tv_progress);
        llNoIntetnet = findViewById(R.id.ll_no_internet);
        avi = findViewById(R.id.avi);
        llNoData.setVisibility(View.GONE);
        llNoIntetnet.setVisibility(View.GONE);
        mEditSearchRecipe.setVisibility(View.GONE);

        if (getIntent().getExtras() != null) {
            //Searched Items
            searchedItems = new ArrayList<>();
            searchedItems = getIntent().getExtras().getStringArrayList("IngrListToFindRecipe");

            if (searchedItems != null) {
                for (int i = 0; i < searchedItems.size(); i++) {
                    ingrListStr += searchedItems.get(i) + ",";
                }
            }
        }

        if (Util.checkConnection(ListOfRecipeActivity.this)) {
            try {
                getRecipesData(ingrListStr);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            llNoIntetnet.setVisibility(View.VISIBLE);
            llNoData.setVisibility(View.GONE);
        }

        mTextUpdateIngr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(ListOfRecipeActivity.this);
                final View vv = inflater.inflate(R.layout.alert_update_ingr, null);
                final AlertDialog.Builder alert = new AlertDialog.Builder(
                        ListOfRecipeActivity.this);

                alert.setView(vv);

                final AlertDialog dialog = alert.create();
                dialog.show();


                TextView tvUpdate = vv.findViewById(R.id.tv_alert_update_ingr);
                recyclerAlertIngr = vv.findViewById(R.id.recycler_alert_ingr);
                recyclerSearchedIngr = vv.findViewById(R.id.recycler_alert_ingr_searched);
                TextView tvCancel = vv.findViewById(R.id.tv_alert_cancel);

                try {
                    allRecipeList = readLabelFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                updateList(allRecipeList, searchedItems);

                tvUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//
                        mRecyclerRecipes.removeAllViewsInLayout();
//
//                        adapter.clear();

                        if (searchedItems != null) {
                            for (int i = 0; i < searchedItems.size(); i++) {
                                ingrListStr += searchedItems.get(i) + ",";
                            }
                        }


                        if (Util.checkConnection(ListOfRecipeActivity.this)) {
                            try {
                                getRecipesData(ingrListStr);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            llNoIntetnet.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        }

                        dialog.cancel();
                    }
                });


                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });


            }
        });

    }


    public static List<String> readLabelFile() throws IOException {

        File rootPath = new File(Environment.getExternalStorageDirectory() + "/.mllogs/", "model");
//        File modelFile = new File(rootPath, getModelPath());
        File labelFile = new File(rootPath, "labels.txt"); //labels.txt

        StringBuilder sb = new StringBuilder();
        InputStream in = new FileInputStream(labelFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        List<String> labels = new ArrayList();
        String line;
        while((line = br.readLine()) != null) {
            labels.add(line);
        }
        br.close();
        return labels;
    }

    private void updateList(List<String> allIngrList, List<String> serchedIngrList) {

        if (serchedIngrList != null) {
            for (int i = 0; i < serchedIngrList.size(); i++) {
                allIngrList.remove(serchedIngrList.get(i));
            }

            populateAlertIngrs(allIngrList, recyclerAlertIngr);
            populateSearchedAlertIngrs(serchedIngrList, recyclerSearchedIngr);
        } else {
            Toast.makeText(this, "Please choose Ingredients", Toast.LENGTH_SHORT).show();
        }

    }

    private void populateAlertIngrs(List<String> allRecipeList, RecyclerView recyclerAlertIngr) {
        recyclerAlertIngr.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        Collections.shuffle(allRecipeList);
        AlertIngredientsAdapter allIngrAdapter = new AlertIngredientsAdapter(this, allRecipeList, searchedItems);
        allIngrAdapter.setClickListener(ListOfRecipeActivity.this);
        recyclerAlertIngr.setAdapter(allIngrAdapter);
    }

    private void populateSearchedAlertIngrs(List<String> allRecipeList, RecyclerView recyclerAlertIngr) {
        recyclerAlertIngr.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        Collections.shuffle(allRecipeList);
        AlertRemoveIngredientsAdapter allRemIngrAdapter = new AlertRemoveIngredientsAdapter(this, searchedItems);
        allRemIngrAdapter.setClickListener(ListOfRecipeActivity.this);
        recyclerAlertIngr.setAdapter(allRemIngrAdapter);
    }

    private void getRecipesData(String ingredientStr) throws JSONException, UnsupportedEncodingException {
        avi.show();
        llNoData.setVisibility(View.GONE);
        mTextProgress.setVisibility(View.VISIBLE);
        recipesList = new ArrayList<>();
        AsyncHttpClient client = new AsyncHttpClient();
//        Log.v("ingr_pass", ingredientStr);

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("ingredients", "" + ingredientStr); //{"ingredients":"chicken,onion"}
        StringEntity entity = new StringEntity(jsonParams.toString());
        client.setTimeout(15 * 1000); // 15 sec timeout
        client.post(ListOfRecipeActivity.this, RECIPES_URL, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Gson gson = new Gson();
                String jsonOutput = new String(responseBody);
                if (!jsonOutput.trim().equals("[]")) {
                    mEditSearchRecipe.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                    avi.hide();
                    mTextProgress.setVisibility(View.GONE);
                    Type listType = new TypeToken<ArrayList<Recipes>>() {
                    }.getType();
                    recipesList = gson.fromJson(jsonOutput, listType);
                    populateRecipes(recipesList);
                    System.out.println("recipes:" + jsonOutput);
                    ingrListStr = "";
                } else {
                    llNoData.setVisibility(View.VISIBLE);
                    avi.hide();
                    mTextProgress.setVisibility(View.GONE);
                    ingrListStr = "";
                    mEditSearchRecipe.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                avi.hide();
                mTextProgress.setVisibility(View.GONE);
                ingrListStr = "";
            }
        });
        searchRecipe();

    }

    private void populateRecipes(ArrayList<Recipes> recipesArrayList) {
        adapter = new RecipesAdapter(this, recipesArrayList);
        adapter.setClickListener(this);
        mRecyclerRecipes.setAdapter(adapter);
        mEditSearchRecipe.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemRemoveSearchClick(int position) {
        allRecipeList.add(searchedItems.get(position));
        searchedItems.remove(searchedItems.get(position));
        updateList(allRecipeList, searchedItems);
    }


    @Override
    public void onItemAddClick(int position) {
//        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();

        searchedItems.add(allRecipeList.get(position));
        updateList(allRecipeList, searchedItems);
    }


    private void searchRecipe() {
        mEditSearchRecipe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<Recipes> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (Recipes s : recipesList) {
            //if the existing elements contains the search input
            if (s.getRecipeName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);
    }

    @Override
    public void onRecipeClick(View view, int position) {
//        Toast.makeText(this, "" + recipesList.get(position), Toast.LENGTH_SHORT).show();
//        showRecipeDetails();
        Intent intent = new Intent(ListOfRecipeActivity.this, RecipeDetailActivity.class);
        intent.putExtra("RECIPE_ID_EXTRA", "" + recipesList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {

    }


}

