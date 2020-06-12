package com.example.hydrofarm.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ViewProductModel
{
    @SerializedName("data")
    List<ViewProductData> data;

    public List<ViewProductData> getData() {
        return data;
    }

    public void setData(List<ViewProductData> data) {
        this.data = data;
    }
}
