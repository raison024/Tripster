package com.example.tripster;

public class Place {
    private int id;
    private String name;
    private String desc;
    private byte[] image;
    private float rating;
    private int favourite;

    public Place(int id, String name, String desc, byte[] image, float rating, int favourite) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.rating = rating;
        this.favourite = favourite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }
}
