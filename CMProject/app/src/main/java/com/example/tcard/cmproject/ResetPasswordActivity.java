package com.example.tcard.cmproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {


    private EditText email_textField;
    private Button sendButton;

    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reset_password);
        getWindow().setStatusBarColor(Color.BLACK);

        email_textField = findViewById(R.id.email_textField);
        sendButton = findViewById(R.id.resetPassword_button);

        fbAuth = FirebaseAuth.getInstance();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = email_textField.getText().toString();

                if(email.isEmpty()){
                     showMessage("Please insert an email!");
                }else{
                    resetPassword(email);
                }
            }
        });

    }

    private void resetPassword(String email) {
        fbAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    showMessage("Email has been sent, check your inbox!");
                    changeToSignInPage();
                }else{
                    showMessage(task.getException().getMessage());
                }
            }
        });
    }

    //Simple method to display toast message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    private void changeToSignInPage() {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
        finish();
    }
}
