package com.example.tcard.cmproject.RunningTracker;

import android.content.Context;

public class Chronometer implements Runnable {

    public static final long MILISECS_TO_MINUTES = 60000;
    public static final long MILISECS_TO_HOURS = 3600000;


    private Context runningTrackerContext;
    private long startTime;

    private String time;
    private boolean isRunning;

    public Chronometer(Context context){
        runningTrackerContext = context;
    }

    public void start(){
        startTime = System.currentTimeMillis();
        isRunning = true;
    }

    public void stop(){
        isRunning = false;
    }

    @Override
    public void run() {
        while(isRunning){
            long timePassed = System.currentTimeMillis() - startTime;

            int millisecs = (int) timePassed % 1000;
            int seconds = (int) ((timePassed / 1000) % 60);
            int minutes = (int) ((timePassed / MILISECS_TO_MINUTES) % 60);
            int hours = (int) ((timePassed / MILISECS_TO_HOURS) % 24);

            time = String.format("%02d:%02d:%02d:%03d",hours,minutes,seconds,millisecs);
            ((RunningTrackerActivity)runningTrackerContext).updateTimerText(time);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void cleanTimeString() {
        ((RunningTrackerActivity)runningTrackerContext).updateTimerText("00:00:00:000");
    }
}
