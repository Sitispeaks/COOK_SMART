package com.amply.recipefinder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeProcedure {

    @SerializedName("step")
    @Expose
    private String step;
    @SerializedName("desc")
    @Expose
    private String desc;

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



//    private String step;
//    private String desc;
//
//
//    public String getStep() {
//        return step;
//    }
//
//    public void setStep(String step) {
//        this.step = step;
//    }
//
//    public String getDesc() {
//        return desc;
//    }
//
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }



}
