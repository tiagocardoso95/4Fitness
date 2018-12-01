package com.example.tcard.cmproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;


public class SplashScreenActivity extends AppCompatActivity {

    private int SLEEP_TIMER = 3; //3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SplashScreen FullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        SplashScreenLauncher splashScreenLauncher = new SplashScreenLauncher();
        splashScreenLauncher.start();
    }

    //Class Responsible for launching the Splash Screen during n seconds
    private class SplashScreenLauncher extends Thread{
        public void run(){
            try{
                //Shows the splash screen for the desired amount of time in milliseconds(1000ms = 1 second)
                sleep(1000 * SLEEP_TIMER);
            }catch(InterruptedException e){
                e.printStackTrace();
            }

            //Intent used to change between activities
            Intent intent = new Intent(SplashScreenActivity.this, SignUpActivity.class);
            startActivity(intent);

            //Finishes the splash screen action after its done running
            SplashScreenActivity.this.finish();
        }
    }
}
