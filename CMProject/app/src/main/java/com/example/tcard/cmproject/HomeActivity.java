package com.example.tcard.cmproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;


public class HomeActivity extends AppCompatActivity {

    private Button signOutButton;
    private TextView idText, NameText, EmailText;

    private FirebaseAuth fbAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setStatusBarColor(Color.BLACK);

        fbAuth = FirebaseAuth.getInstance();

        signOutButton = findViewById(R.id.signOut_button);

        idText = findViewById(R.id.userID);
        NameText = findViewById(R.id.userName);
        EmailText = findViewById(R.id.userEmail);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // UID specific to the provider
                idText.setText("ID: "+profile.getUid());

                // Name, email address
                NameText.setText("Name: "+profile.getDisplayName());
                EmailText.setText("Email: "+profile.getEmail());
            }

            signOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fbAuth.signOut();
                    LoginManager.getInstance().logOut();
                    showMessage("You are Logged out!");
                    changeToSignUp();
                }
            });
        }
    }

    private void changeToSignUp() {
        Intent intent = new Intent(HomeActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}
