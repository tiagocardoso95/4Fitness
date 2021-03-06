package com.example.tcard.cmproject.UserStats;

import com.google.firebase.database.DatabaseReference;

public class UserStats {
    private static UserStats instance;

    private String id;
    private float weight;
    private float height;

    private String name;

    public UserStats(float weight, float height, String name,String id) {
        this.weight = weight;
        this.height = height;
        this.name = name;
        this.id = id;
        if(instance == null){
            instance = this;
        }
    }
    public static UserStats GetInstance(){
        return instance;
    }
    public static void UpdateInstance(UserStats stats){
        instance = stats;
    }

    public UserStats(){

    }

    public  float getWeight(){
        return weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float CalculateBMI(){
        return 0.0f;
    }
}
