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

        userStatsTable = database.getReference("users");
    }

    public static DB getInstance(){
        return instance;
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
