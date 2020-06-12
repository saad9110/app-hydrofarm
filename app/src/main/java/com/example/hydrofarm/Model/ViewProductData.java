package com.example.hydrofarm.Model;

import com.google.gson.annotations.SerializedName;

public class ViewProductData
{
    @SerializedName("id")
    String id;
    @SerializedName("category_id")
    String category_id;
    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;
    @SerializedName("price")
    String price;
    @SerializedName("photo")
    String photo;
    @SerializedName("counter")
    String counter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }
}
