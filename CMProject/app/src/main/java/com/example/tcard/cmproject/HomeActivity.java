package com.example.tcard.cmproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcard.cmproject.Hydration.HydrationActivity;
import com.example.tcard.cmproject.UserStats.UserStats;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;


public class HomeActivity extends AppCompatActivity {

    private Button signOutButton, hydrationManagerButton, runningTrackerButton;
    private TextView idText, NameText, EmailText;

    private FirebaseAuth fbAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setStatusBarColor(Color.BLACK);

        fbAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signOutButton = findViewById(R.id.signOut_button);

        idText = findViewById(R.id.userID);
        NameText = findViewById(R.id.userName);
        EmailText = findViewById(R.id.userEmail);

        hydrationManagerButton = findViewById(R.id.hydrationManager_button);
        runningTrackerButton = findViewById(R.id.runningTracker_button);

        //Teste para ir buscar os dados do utilizador FireBase e Facebook
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // UID specific to the provider
                idText.setText("ID: "+profile.getUid());

                // Name, email address
                String name = profile.getDisplayName();
                if(name == null) {
                 // name = UserStats.GetInstance().getName();
                    name = "fukin fix the db";
                }
                NameText.setText("Name: " + name);
                EmailText.setText("Email: "+profile.getEmail());
            }
        }

        //Teste para ir buscar os dados do utilizador google
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            idText.setText("ID_Google: "+acct.getId());
            NameText.setText("Name_Google: "+acct.getDisplayName());
            EmailText.setText("Email_Google: "+acct.getEmail());
        }


        signOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fbAuth.signOut();
                    LoginManager.getInstance().logOut();
                    mGoogleSignInClient.signOut();
                    showMessage("You are Logged out!");
                    changeToSignUp();
                }
            });

        hydrationManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToHydrationManager();
            }
        });

        runningTrackerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToRunningTracker();
            }
        });

    }

    private void changeToSignUp() {
        Intent intent = new Intent(HomeActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    private void changeToHydrationManager(){
        Intent intent = new Intent(HomeActivity.this, HydrationActivity.class);
        startActivity(intent);
        finish();
    }
    private void changeToRunningTracker(){
        //Intent intent = new Intent(HomeActivity.this, RunningTracker.class);
        //startActivity(intent);
        //finish();
    }
}
