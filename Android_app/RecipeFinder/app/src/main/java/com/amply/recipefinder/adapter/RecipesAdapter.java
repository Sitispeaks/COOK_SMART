package com.amply.recipefinder.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amply.recipefinder.R;
import com.amply.recipefinder.models.Recipes;

import java.util.ArrayList;
import java.util.Random;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder>  {

    private ArrayList<Recipes> mRecipeList;
    private LayoutInflater mInflater;
    private RecipeClickListener mClickListener;

    private String mIngrList;

    // data is passed into the constructor
    public RecipesAdapter(Context context, ArrayList<Recipes> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mRecipeList = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.each_recipe_design, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Recipes recipes = mRecipeList.get(position);

        holder.mTextCount.setText(""+String.valueOf(position+1));

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.mCardViewRecipe.setCardBackgroundColor(color);

        holder.mTextRecipeName.setText(""+recipes.getRecipeName().substring(0, 1).toUpperCase() + recipes.getRecipeName().substring(1).toLowerCase());
//        holder.mTextIngrList.setText(mIngrList);

        if (TextUtils.isEmpty(recipes.getRecipeIngredient().get(1).getContent())){
            holder.mTextIngr1.setVisibility(View.GONE);
        }else if (TextUtils.isEmpty(recipes.getRecipeIngredient().get(2).getContent())){
            holder.mTextIngr2.setVisibility(View.GONE);
        }else if (TextUtils.isEmpty(recipes.getRecipeIngredient().get(3).getContent())){
            holder.mTextIngr3.setVisibility(View.GONE);
        }else if (recipes.getRecipeIngredient().get(1).getContent().contains("chicken")) {
            holder.mTextIngr1.setText("chicken");
        }else if (recipes.getRecipeIngredient().get(2).getContent().contains("chicken")) {
            holder.mTextIngr2.setText("chicken");
        }else if (recipes.getRecipeIngredient().get(3).getContent().contains("chicken")) {
            holder.mTextIngr3.setText("chicken");
        }


        holder.mTextIngr1.setText(""+recipes.getRecipeIngredient().get(1).getIngr());
        holder.mTextIngr2.setText(""+recipes.getRecipeIngredient().get(2).getIngr());
        holder.mTextIngr3.setText(""+recipes.getRecipeIngredient().get(3).getIngr());

        mIngrList = "";
        if (recipes.getRecipeServings() != null) {
            holder.mTextServing.setText("Servings: "+String.valueOf(recipes.getRecipeServings()));
        }else {
            holder.mTextServing.setText("Data Not found!");
        }

        if (recipes.getRecipePreptime() != null) {
            holder.mTextPrepTime.setText(recipes.getRecipePreptime());
        }else {
            holder.mTextPrepTime.setText("Data Not found!");
        }

        if (recipes.getRecipeCalories() != null) {
            holder.mTextCalories.setText("Calories: "+String.valueOf(recipes.getRecipeCalories())+" cals");
        }else {
            holder.mTextCalories.setText("Data Not found!");
        }

        holder.mTextIngrFirstChr.setText(""+recipes.getRecipeName().substring(0, 1).toUpperCase());

    }

    public void filterList(ArrayList<Recipes> filterdNames) {
        this.mRecipeList = filterdNames;
        notifyDataSetChanged();
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextRecipeName, mTextPrepTime, mTextIngrFirstChr, mTextServing, mTextCalories, mTextCount;
        TextView mTextIngr1,mTextIngr2,mTextIngr3;
        CardView mCardViewRecipe;

        ViewHolder(View itemView) {
            super(itemView);
            mTextRecipeName = itemView.findViewById(R.id.tv_recipe_name);
            mTextIngrFirstChr = itemView.findViewById(R.id.tv_ingr_name);
//            mTextIngrList = itemView.findViewById(R.id.tv_recipe_ingr_list);
            mTextServing = itemView.findViewById(R.id.tv_recipe_ingr_serv);
            mTextPrepTime = itemView.findViewById(R.id.tv_recipe_prep_time);
            mTextCalories = itemView.findViewById(R.id.tv_recipe_calories);
            mTextCount = itemView.findViewById(R.id.tv_count);
            mTextIngr1 = itemView.findViewById(R.id.tv_ingr_1);
            mTextIngr2 = itemView.findViewById(R.id.tv_ingr_2);
            mTextIngr3 = itemView.findViewById(R.id.tv_ingr_3);

            mCardViewRecipe = itemView.findViewById(R.id.card_view_recipe);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onRecipeClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Recipes getItem(int id) {
        return mRecipeList.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(RecipeClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface RecipeClickListener {
        void onRecipeClick(View view, int position);
    }

    public void clear() {
        int size = mRecipeList.size();
        mRecipeList.clear();
        notifyItemRangeRemoved(0, size);
    }

}