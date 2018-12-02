package com.example.tcard.cmproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.view.View.*;

public class SignUpActivity extends AppCompatActivity {

    private EditText userEmail, userPassword, userConfirmPassword;
    private ProgressBar loadingProgress;
    private Button signUpButton, signInButton;


    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);

        userEmail = findViewById(R.id.email_textField);
        userPassword = findViewById(R.id.password_textField);
        userConfirmPassword = findViewById(R.id.confirmpassword_textField);
        loadingProgress = findViewById(R.id.progressBar);
        signUpButton = findViewById(R.id.signup_button);
        signInButton = findViewById(R.id.signIn_button);
        loadingProgress.setVisibility(INVISIBLE);

        fbAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                signUpButton.setVisibility(view.INVISIBLE);
                loadingProgress.setVisibility(view.VISIBLE);
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String confirmPassword = userConfirmPassword.getText().toString();

                if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    showMessage("Please verify all fields!");
                    signUpButton.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }else if(!confirmPassword.equals(password)){
                    showMessage("Passwords do not match!");
                    signUpButton.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }else{
                    CreateUserAccount(email,password);
                }
            }
        });

        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToSignInPage();
            }
        });

    }

    private void CreateUserAccount(String email, String password) {
        fbAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user account created successfully
                            showMessage("Account created!");
                            changeToHomePage();

                        }else{
                            showMessage(task.getException().getMessage());
                            signUpButton.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void changeToHomePage() {
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void changeToSignInPage() {
        Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
        startActivity(intent);
        finish();
    }

    //Simple method to display toast message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}
