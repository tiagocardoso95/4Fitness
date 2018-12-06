package com.example.tcard.cmproject.Hydration;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tcard.cmproject.R;
import com.example.tcard.cmproject.UserStats.UserStats;
import com.example.tcard.cmproject.Utility.DB;

public class HydrationActivity extends AppCompatActivity implements SensorEventListener {

    private DB database;

    private TextView temperature,weight,humidity,ammount;
    private SensorManager mSensorManager;
    private Sensor mTemperature, mHumidity;

    private float temperaturef;
    private UserStats stats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = DB.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        temperature = findViewById(R.id.temperatureT);
        weight = findViewById(R.id.weightT);
        humidity = findViewById(R.id.humidityT);
        ammount =  findViewById(R.id.ammountT);

        temperaturef = 0.0f;

        stats = UserStats.GetInstance();

        ImageView droplet = (ImageView) findViewById(R.id.dropImg);
        droplet.setImageResource(getResources().getIdentifier("@drawable/droplet",null,this.getPackageName()));

        weight.setText(weight.getText()+": \n +"+stats.getWeight());

        //GET sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mHumidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);


        //Calculate ammount
        // get temperature
        // get humidity

    }
    @Override
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this,mTemperature,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    private float calculateWater(){
        float amt = (stats.getWeight()*0.033f);
        if(temperaturef > 25.0f){
            amt *= 1.25;
        }

        return amt;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        temperaturef = event.values[0];
        float hum = event.values[1];
        humidity.setText(humidity.getText()+": \n"+hum+"");
        temperature.setText(temperature.getText()+": \n"+temperaturef+" ºC");
        ammount.setText(ammount.getText()+": \n"+calculateWater()+" L");
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
