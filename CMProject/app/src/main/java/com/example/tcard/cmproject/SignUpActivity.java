package com.example.tcard.cmproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import static android.view.View.*;

public class SignUpActivity extends AppCompatActivity {

    private EditText userEmail, userPassword, userConfirmPassword;
    private ProgressBar loadingProgress;
    private Button signUpEmailButton, signInEmailButton, signInFacebookButton;

    private CallbackManager mCallbackManager;

    private FirebaseAuth fbAuth;
    private static final String TAG = "FACELOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);
        getWindow().setStatusBarColor(Color.BLACK);

        userEmail = findViewById(R.id.email_textField);
        userPassword = findViewById(R.id.password_textField);
        userConfirmPassword = findViewById(R.id.confirmpassword_textField);
        loadingProgress = findViewById(R.id.progressBar);
        signUpEmailButton = findViewById(R.id.signup_button);
        signInEmailButton = findViewById(R.id.signIn_button);
        signInFacebookButton = findViewById(R.id.signUp_facebook_button);
        loadingProgress.setVisibility(INVISIBLE);

        userEmail.getBackground().setColorFilter(Color.parseColor("#1780D6"), PorterDuff.Mode.SRC_IN);
        userPassword.getBackground().setColorFilter(Color.parseColor("#1780D6"), PorterDuff.Mode.SRC_IN);
        userConfirmPassword.getBackground().setColorFilter(Color.parseColor("#1780D6"), PorterDuff.Mode.SRC_IN);

        mCallbackManager = CallbackManager.Factory.create();
        fbAuth = FirebaseAuth.getInstance();

        signInFacebookButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(SignUpActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                        // ...
                    }
                });
            }
        });

        signUpEmailButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                signUpEmailButton.setVisibility(view.INVISIBLE);
                loadingProgress.setVisibility(view.VISIBLE);
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String confirmPassword = userConfirmPassword.getText().toString();

                if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    showMessage("Please verify all fields!");
                    signUpEmailButton.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }else if(!confirmPassword.equals(password)){
                    showMessage("Passwords do not match!");
                    signUpEmailButton.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }else{
                    CreateUserAccountWithEmail(email,password);
                }
            }
        });

        signInEmailButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToSignInPage();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = fbAuth.getCurrentUser();
        if(currentUser != null){
            updateUI();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI() {
        showMessage("You are Logged in using facebook!");
        changeToHomePage();
    }

    private void CreateUserAccountWithEmail(String email, String password) {
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
                            signUpEmailButton.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void changeToHomePage() {
        Intent intent = new Intent(SignUpActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void changeToSignInPage() {
        Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
        startActivity(intent);
        finish();
    }

    //Simple method to display toast message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}
