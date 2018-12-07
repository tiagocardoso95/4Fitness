package com.example.tcard.cmproject.RunningTracker;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcard.cmproject.R;

public class RunningTrackerActivity extends AppCompatActivity implements SensorEventListener {

    private Context thisContext;

    private TextView timeTextView;
    private Button start_stop_button;

    private SensorManager sensorManager;
    private TextView stepsTextView;
    private boolean isRunning;

    private Chronometer chronometer;
    private Thread threadChrono;

    private String timeAtStop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_tracker);
        getWindow().setStatusBarColor(Color.BLACK);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepsTextView = findViewById(R.id.stepsTimeView);
        isRunning = false;

        thisContext = this;
        timeTextView = findViewById(R.id.timeTextView);

        start_stop_button = findViewById(R.id.startstopButton);

        start_stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chronometer == null){
                    start_stop_button.setText("STOP");
                    chronometer = new Chronometer(thisContext);
                    onResume();
                    threadChrono = new Thread(chronometer);
                    threadChrono.start();
                    chronometer.start();
                }else{
                    chronometer.stop();
                    chronometer.cleanTimeString();
                    timeAtStop = String.valueOf(timeTextView);
                    start_stop_button.setText("START");
                    onPause();
                    threadChrono.interrupt();
                    threadChrono = null;
                    chronometer = null;
                    updateUI(timeAtStop);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(this, "Step Counting sensor not found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(isRunning){
            stepsTextView.setText(String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    private void updateUI(String time) {
    }

    public void updateTimerText(final String time){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timeTextView.setText(time);
            }
        });
    }
}
