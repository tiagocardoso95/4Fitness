package com.example.tcard.cmproject.Hidration;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.tcard.cmproject.R;
import com.example.tcard.cmproject.UserStats.UserStats;
import com.example.tcard.cmproject.Utility.DB;

public class HidrationActivity extends AppCompatActivity {

    private DB database;

    private TextView temperature,weight,humidity,ammount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = DB.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        temperature = findViewById(R.id.temperatureString);
        weight = findViewById(R.id.weightT);
        humidity = findViewById(R.id.humidityT);
        ammount =  findViewById(R.id.ammountT);

        UserStats stats = database.getCurUser();

        weight.setText(weight.getText()+": \n +"+stats.getWeight());

        //Calculate ammount
        // get temperature
        // get humidity

    }
}
