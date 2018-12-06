package com.example.tcard.cmproject.Utility;

import com.example.tcard.cmproject.UserStats.UserStats;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DB {
    private static DB instance;

    private UserStats curUser;

    private FirebaseDatabase database;
    private DatabaseReference userStatsTable;

    public DB(){
        if(instance == null){
            instance = this;
        }
        database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference();
        userStatsTable = ref.child("users");
    }

    public static DB getInstance(){
        return instance;
    }

    public static void Instantiate(){
        if(instance == null){
            instance = new DB();
        }
    }
    public FirebaseDatabase getDatabase() {
        return database;
    }

    public DatabaseReference getUserStatsTable() {
        return userStatsTable;
    }

    public UserStats getCurUser() {
        return curUser;
    }
}
