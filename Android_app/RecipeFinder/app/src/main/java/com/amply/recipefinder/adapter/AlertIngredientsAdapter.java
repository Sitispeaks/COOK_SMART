package com.amply.recipefinder.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amply.recipefinder.R;

import java.util.List;
import java.util.Random;

public class AlertIngredientsAdapter extends RecyclerView.Adapter<AlertIngredientsAdapter.ViewHolder> {

    private List<String> mData;
    private List<String> mDataSearched;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public AlertIngredientsAdapter(Context context, List<String> data, List<String> dataSearched) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mDataSearched = dataSearched;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.alert_each_ingr_design, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mData.get(position).contains("vegetable")) {
            mData.get(position).replace("vegetable ", "");
        } else if (mData.get(position).contains("fruit")) {
            mData.get(position).replace("fruit ", "");
        } else if (mData.get(position).contains("raw")) {
            mData.get(position).replace("raw ", "");
        }

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        int color1 = color - 500;

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{color, color1});
        gd.setCornerRadius(0f);

        holder.mImgBack.setBackgroundColor(color);

        holder.mTextName.setText(mData.get(position).substring(0, 1).toUpperCase());
        holder.myTextView.setText(mData.get(position));


        holder.mTextAddRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.mTextAddRemove.getText().toString().trim().equals("Add")) {
//                    holder.mTextAddRemove.setText("Remove");
                    if (mClickListener != null) mClickListener.onItemAddClick(position);
                }
//                else if (holder.mTextAddRemove.getText().toString().trim().equals("Remove")) {
//                    holder.mTextAddRemove.setText("Add");
//                    if (mClickListener != null) mClickListener.onItemRemoveClick(position);
//                }


            }
        });

    }


    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView, mTextName, mTextAddRemove;
        CardView mCardView;
        ImageView mImgBack;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.ingr_item);
            mTextAddRemove = itemView.findViewById(R.id.tv_alert_add_ingr);
            mTextName = itemView.findViewById(R.id.tv_ingr_name);
            mCardView = itemView.findViewById(R.id.card_view);
            mImgBack = itemView.findViewById(R.id.img_each_ingrs_back);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onItemAddClick(int position);

    }

    public void clear() {
        int size = mData.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                mData.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

}