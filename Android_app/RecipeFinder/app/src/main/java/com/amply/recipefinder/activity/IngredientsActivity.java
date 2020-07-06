package com.amply.recipefinder.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amply.recipefinder.ClassifierActivity;
import com.amply.recipefinder.R;
import com.amply.recipefinder.adapter.IngredientsAdapter;
import com.amply.recipefinder.adapter.RecipesAdapter;
import com.amply.recipefinder.models.Recipes;
import com.amply.recipefinder.utils.SharedPreferencesManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsActivity extends AppCompatActivity implements IngredientsAdapter.ItemClickListener, RecipesAdapter.RecipeClickListener {

    // Button Camera
    TextView mTextHeaderEmail;//, mClearAll;

    // Bottom Tabs
//    private TextView tv_tab_recipe,tv_tab_ingr,tv_tab_profile;
    private static final String GRID = "GRID_LAYOUT";
    private static final String LINEAR = "LINEAR_LAYOUT";

    private static final int CLICKED = 1;
    private static final int NOT_CLICKED = 0;
    @BindView(R.id.tv_progress_bar_progress)
    TextView tvProgressBarProgress;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;
    @BindView(R.id.tv_download_model)
    TextView tvDownloadModel;
    @BindView(R.id.card_download_model)
    CardView cardDownloadModel;
    @BindView(R.id.tv_download_model_extra)
    TextView tvDownloadModelExtra;
    @BindView(R.id.card_lets_see)
    CardView cardLetsSee;

    private List<String> allRecipeList;
    private IngredientsAdapter allIngrAdapter;
    private IngredientsAdapter selectedIngrAdapter;
    private NavigationView mNavigationView;
    private ArrayList<String> recivedItems, searchedItems, selectedItems;
    private RecyclerView recyclerView_all_ingredients;
    private RecyclerView recycler_attempted_recipes;
    private int numberOfColumns = 4;

    private LinearLayout mLinearCamera, mLayoutNoData; //mllFindRecipe,

    private Gson gson;
    private String recivedItemsStr = "";
    HashSet remove_duplicate_ingrs = new HashSet();


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CardView mCardLast;
    private RecipesAdapter attemptedRecipeAdapter;

    private ArrayList<Recipes> attemptedRecipesArrayList;

    private static final int REQUEST_PERMISSION = 786;


    private FirebaseDatabase database;
    private DatabaseReference model_is_updated_ref;
    private int new_model_version;


    private long modelFileSize = 0;
    private long labelFileSize = 0;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ButterKnife.bind(this);

        mNavigationView = findViewById(R.id.nav_view);


        recyclerView_all_ingredients = findViewById(R.id.recycler_ingr);
        recycler_attempted_recipes = findViewById(R.id.recycler_attempted_recipes);
        mLinearCamera = findViewById(R.id.ll_camera_open);
        mLayoutNoData = findViewById(R.id.no_ll_data_recipe);
        mCardLast = findViewById(R.id.card_banner_last);
        View header = mNavigationView.getHeaderView(0);
        mTextHeaderEmail = header.findViewById(R.id.tv_header_email);
        mLayoutNoData.setVisibility(View.GONE);

        searchedItems = new ArrayList<>();
        recivedItems = new ArrayList<>();

        cardDownloadModel.setVisibility(View.GONE);
//        handleModelFile();


        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        mLinearCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardDownloadModel.getVisibility() == View.VISIBLE) {
                    Toast.makeText(IngredientsActivity.this, "Please download the model first", Toast.LENGTH_SHORT).show();

                } else {

                    if (!verifyLocalfile()) {
                        cardDownloadModel.setVisibility(View.VISIBLE);
                        Toast.makeText(IngredientsActivity.this, "Please download the model first", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(IngredientsActivity.this, ClassifierActivity.class));
                    }
                }
            }
        });

        // populate All Ingredients
        try {
//            recyclerView_all_ingredients.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
            recyclerView_all_ingredients.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            allRecipeList = readLabelFile(); //FileUtil.loadLabels(this, "labels.txt");
            Collections.shuffle(allRecipeList);
            allIngrAdapter = new IngredientsAdapter(this, allRecipeList);
            allIngrAdapter.setClickListener(this);
            recyclerView_all_ingredients.setAdapter(allIngrAdapter);

        } catch (IOException e) {
            e.printStackTrace();
        }

        setUpNavDrawer();

        setUpAttemptedRecipeList();

        autoScroll();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                handleDownLoadingModelFile();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        tvDownloadModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvDownloadModel.getText().toString().trim().equals("Done")) {
                    cardDownloadModel.setVisibility(View.GONE);
                }

                modelFileSize = DownloadFromFirebase("model.tflite");
                labelFileSize = DownloadFromFirebase("labels.txt");

//                modelFileSize = taskSnapshot.getTotalByteCount();
//                labelFileSize = taskSnapshot.getTotalByteCount();
            }
        });


    }

    private boolean verifyLocalfile() {

        boolean allDownloadedSuccessfully = false;

        // card visibility check with local folder exsting check
        File rootPath = new File(Environment.getExternalStorageDirectory() + "/.mllogs/", "model");

        File modelFile = new File(rootPath, "model.tflite");
        File labelFile = new File(rootPath, "labels.txt");

//        Toast.makeText(this, "m: " + modelFile.length() + "l: "+labelFile.length(), Toast.LENGTH_SHORT).show();

        // 60642192 model
        // 656 label
        if ((modelFile.length() >= 60642191) && (labelFile.length() >= 655)) {
            allDownloadedSuccessfully = true;
        }
        return allDownloadedSuccessfully;
    }


    private boolean checkIffiledownlaodedSucessfully() {

        boolean downloadAgain = false;

        File rootPath = new File(Environment.getExternalStorageDirectory() + "/.mllogs/", "model");

        File modelFile = new File(rootPath, "model.tflite");
        File labelFile = new File(rootPath, "labels.txt");


        if ((modelFile.length() < modelFileSize) || (labelFile.length() < labelFileSize)) {
            Toast.makeText(this, "Something wrong with local file download it again", Toast.LENGTH_SHORT).show();
            downloadAgain = true;
        }

        return downloadAgain;
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
        while ((line = br.readLine()) != null) {
            labels.add(line);
        }
        br.close();
        return labels;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleDownLoadingModelFile() throws FileNotFoundException {

        database = FirebaseDatabase.getInstance();
        model_is_updated_ref = database.getReference("model_version");

        // card visibility check with model version
        // Read from the database
        model_is_updated_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                new_model_version = dataSnapshot.getValue(Integer.class);

                Log.d("Model_Update", "New version is " + new_model_version + " I have model version" + SharedPreferencesManager.getModelVersion(IngredientsActivity.this));

                if (new_model_version > SharedPreferencesManager.getModelVersion(IngredientsActivity.this)) {
                    cardDownloadModel.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SaveToInternalMemory(File tempModelFile) {

        byte[] fileArr = null;
        try {
            fileArr = Files.readAllBytes(Paths.get(tempModelFile.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream outputStream = openFileOutput(Environment.getRootDirectory() + "/ABCDEF/" + tempModelFile.getName(), Context.MODE_PRIVATE);
            outputStream.write(fileArr);
            outputStream.close();
            Toast.makeText(this, "File Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();

        }


    }


    private long DownloadFromFirebase(String fileName) {

        final long[] fileSize = {0};

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://cook-smart-4344b.appspot.com/");
        StorageReference islandRef = storageRef.child("models/" + fileName);

        File rootPath = new File(Environment.getExternalStorageDirectory() + "/.mllogs/", "model");
        Log.v("path", rootPath.getAbsolutePath());
        if (!rootPath.exists()) {
//            Toast.makeText(this, "Creating Folder", Toast.LENGTH_SHORT).show();
            rootPath.mkdirs();
        }

//        File localFile = new File(rootPath, "labels.txt");
        File localFile = new File(rootPath, "" + fileName);

        islandRef.getFile(localFile).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                tvProgressBarProgress.setText("Collecting files to download...");
                tvDownloadModelExtra.setText("downloading...");
                tvProgressBarProgress.setText("Downloading ..."
                        + fileName
                        + "\n"
                        + Formatter.formatFileSize(getApplicationContext(), taskSnapshot.getBytesTransferred()) + " / "
                        + Formatter.formatFileSize(getApplicationContext(), taskSnapshot.getTotalByteCount()));
                tvDownloadModel.setText("Waiting to complete..");


                fileSize[0] = taskSnapshot.getTotalByteCount();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tvProgressBarProgress.setText("Something Wrong with the download");
                Log.e("firebase ", ";local tem file not created  created " + e.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                SharedPreferencesManager.setModelVersion(IngredientsActivity.this, new_model_version);
                tvDownloadModel.setText("Done");
                tvProgressBarProgress.setText("Thanks for downloading.");
                tvDownloadModelExtra.setText("Download Success");


//                Toast.makeText(IngredientsActivity.this, "downloaded Successfully", Toast.LENGTH_SHORT).show();
            }
        });

//        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//
////                tvProgressBarProgress.setText("Download Success");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//
//            }
//        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//            }
//
//
//        });

        return fileSize[0];
    }

    private void setUpAttemptedRecipeList() {
        attemptedRecipesArrayList = SharedPreferencesManager.getAttempedRecipes(IngredientsActivity.this);

        if (attemptedRecipesArrayList == null || attemptedRecipesArrayList.size() == 0) {
            mLayoutNoData.setVisibility(View.VISIBLE);
            return;
        }

//        Toast.makeText(this, "Attempted recipe count: " + attemptedRecipesArrayList.size(), Toast.LENGTH_SHORT).show();
//        Log.v("Attempted recipe: ", "" + attemptedRecipesArrayList.get(0).getRecipeName());

        recycler_attempted_recipes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        attemptedRecipeAdapter = new RecipesAdapter(this, attemptedRecipesArrayList);
        attemptedRecipeAdapter.setClickListener(this);
        recycler_attempted_recipes.setAdapter(attemptedRecipeAdapter);

    }

    private void autoScroll() {

        final HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.scroll_banner);


        hsv.postDelayed(new Runnable() {
            @Override
            public void run() {
//                hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                hsv.smoothScrollBy(1000, mCardLast.getTop());
            }
        }, 1000);

        hsv.postDelayed(new Runnable() {
            @Override
            public void run() {
                hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
//                hsv.smoothScrollBy(100, mCardLast.getTop());
            }
        }, 2000);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @SuppressLint("RestrictedApi")
    private void setUpNavDrawer() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            SharedPreferencesManager.saveFirebaseUser(IngredientsActivity.this, "" + FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }
        //Header
        mTextHeaderEmail.setText("" + SharedPreferencesManager.getFirebaseUser(IngredientsActivity.this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.navigation_ingredients:
                        startActivity(new Intent(getApplicationContext(), IngredientsActivity.class));
                        finish();
                        break;
                    case R.id.navigation_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//                        finish();
                        break;
                    case R.id.navigation_recipe:
                        startActivity(new Intent(getApplicationContext(), ListOfRecipeActivity.class));
                        finish();
                        break;
                    case R.id.navigation_signout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                        SharedPreferencesManager.saveUserCheck(IngredientsActivity.this, false);
                        finish();
                        break;
                }
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });


    }

    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(this, "" + allRecipeList.get(position), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRecipeClick(View view, int position) {
//        Toast.makeText(this, "" + attemptedRecipesArrayList.get(position), Toast.LENGTH_SHORT).show();
//        showRecipeDetails();

        // ObjectId("5e36975798fbf9567565579d")

        String objID = String.valueOf(attemptedRecipesArrayList.get(position).getId());

        objID = objID.replace("ObjectId(\"", "");
        objID = objID.replace("\")", "");

        Intent intent = new Intent(IngredientsActivity.this, RecipeDetailActivity.class);
        intent.putExtra("RECIPE_ID_EXTRA", "" + objID);
        startActivity(intent);
    }
}
