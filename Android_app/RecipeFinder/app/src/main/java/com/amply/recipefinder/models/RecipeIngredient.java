package com.amply.recipefinder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeIngredient {

    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("ingr")
    @Expose
    private String ingr;
    @SerializedName("content")
    @Expose
    private String content;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIngr() {
        return ingr;
    }

    public void setIngr(String ingr) {
        this.ingr = ingr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



//    private String data;
//    private String amount;
//    private String ingr;
//    private String content;
//
//
//    public String getData() {
//        return data;
//    }
//
//    public void setData(String data) {
//        this.data = data;
//    }
//
//    public String getAmount() {
//        return amount;
//    }
//
//    public void setAmount(String amount) {
//        this.amount = amount;
//    }
//
//    public String getIngr() {
//        return ingr;
//    }
//
//    public void setIngr(String ingr) {
//        this.ingr = ingr;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }


}
