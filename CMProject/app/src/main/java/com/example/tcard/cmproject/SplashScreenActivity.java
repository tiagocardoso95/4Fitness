package com.example.tcard.cmproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;


public class SplashScreenActivity extends AppCompatActivity {

    private int SLEEP_TIMER = 3; //3 seconds
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.BLACK);
        fbAuth = FirebaseAuth.getInstance();

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
            checkIfUserLoggedIn();
        }
    }

    //Method that checks if a User is Logged in and redirects him to the next page
    private void checkIfUserLoggedIn(){
        Intent intent;
        if(fbAuth.getCurrentUser() != null){
            intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            SplashScreenActivity.this.finish();
        }else{
            intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
            SplashScreenActivity.this.finish();
        }
    }
}
