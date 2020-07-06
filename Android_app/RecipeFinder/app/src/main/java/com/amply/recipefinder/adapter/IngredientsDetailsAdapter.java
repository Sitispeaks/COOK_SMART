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
import com.amply.recipefinder.models.RecipeIngredient;

import java.util.List;
import java.util.Random;

public class IngredientsDetailsAdapter extends RecyclerView.Adapter<IngredientsDetailsAdapter.ViewHolder>  {

    private List<RecipeIngredient> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public IngredientsDetailsAdapter(Context context, List<RecipeIngredient> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.each_ingr_detail_design, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        RecipeIngredient recipeIngredient = mData.get(position);


        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.mCardView.setCardBackgroundColor(color);

        if (TextUtils.isEmpty(recipeIngredient.getIngr()) && TextUtils.isEmpty(recipeIngredient.getAmount()) && TextUtils.isEmpty(recipeIngredient.getContent())){
            holder.itemView.setVisibility(View.GONE);
            return;
        }

        if (!TextUtils.isEmpty(recipeIngredient.getIngr())){
            holder.mTextIngdetailName.setText(recipeIngredient.getIngr().substring(0, 1).toUpperCase());
        }
        holder.mIngdetailName.setText(recipeIngredient.getIngr());
        holder.mTextIngdetailAmount.setText(recipeIngredient.getAmount());
        holder.mTextIngrDesc.setText(recipeIngredient.getData());
        holder.mTextIngrContent.setText(recipeIngredient.getContent());

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextIngdetailName, mIngdetailName, mTextIngdetailAmount, mTextIngrDesc,mTextIngrContent;
        CardView mCardView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextIngdetailName = itemView.findViewById(R.id.tv_ingr_detail_name);
            mIngdetailName = itemView.findViewById(R.id.ingr_detail_name);
            mTextIngdetailAmount = itemView.findViewById(R.id.ingr_detail_amount);
            mTextIngrDesc = itemView.findViewById(R.id.ingr_detail_desc);
            mTextIngrContent = itemView.findViewById(R.id.ingr_detail_content);

            mCardView = itemView.findViewById(R.id.card_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    RecipeIngredient getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}