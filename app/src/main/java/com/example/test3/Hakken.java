package com.example.test3;

import android.widget.ImageView;

import java.io.Serializable;

public class Hakken implements Serializable {
    private int resourceId;
    private String name;

    public Hakken() {
    }

    public Hakken(int resourceId , String name) {
        this.resourceId = resourceId;
        this.name = name;
    }


    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
