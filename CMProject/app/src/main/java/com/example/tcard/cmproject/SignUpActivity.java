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

import com.example.tcard.cmproject.UserStats.UserStats;
import com.example.tcard.cmproject.Utility.DB;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.Arrays;

import static android.view.View.*;

public class SignUpActivity extends AppCompatActivity {

    private EditText userEmail, userPassword, userConfirmPassword;
    private ProgressBar loadingProgress;
    private Button signUpEmailButton, signInEmailButton, signUpFacebookButton, signUpGoogleButton;

    private CallbackManager mCallbackManager;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth fbAuth;

    private int RC_SIGN_IN = 1;
    private static final String TAG = "SIGNUPTAG";

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
        signUpFacebookButton = findViewById(R.id.signUp_facebook_button);
        signUpGoogleButton = findViewById(R.id.signUp_google_button);
        loadingProgress.setVisibility(INVISIBLE);

        userEmail.getBackground().setColorFilter(Color.parseColor("#1780D6"), PorterDuff.Mode.SRC_IN);
        userPassword.getBackground().setColorFilter(Color.parseColor("#1780D6"), PorterDuff.Mode.SRC_IN);
        userConfirmPassword.getBackground().setColorFilter(Color.parseColor("#1780D6"), PorterDuff.Mode.SRC_IN);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mCallbackManager = CallbackManager.Factory.create();
        fbAuth = FirebaseAuth.getInstance();

        signUpFacebookButton.setOnClickListener(new OnClickListener() {
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

        signUpGoogleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
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

    //Google SignIn
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //Checks if its a facebook activity or a google and treats the activity accordingly
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }else{
            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Google Sign In on FireBase
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            changeToHomePage();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            showMessage("Login with Google Failed!");
                        }
                    }
                });
    }

    //Facebook Sign In
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
                            changeToHomePage();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Creates a user with an email and password and automatically signs him in
    private void CreateUserAccountWithEmail(String email, String password) {
        fbAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user account created successfully
                            showMessage("Account created!");
                            /*Create stats*/
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String ID = null;
                            ID = user.getUid();
                            System.out.println(ID +"user ID");
                            UserStats stats = new UserStats(0.0f,0.0f,"Undefined",ID);
                            DB.getInstance().getUserStatsTable().child(ID).setValue(stats);

                            changeToHomePage();
                        }else{
                            showMessage(task.getException().getMessage());
                            signUpEmailButton.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    //Changes the Activity to Home Page
    private void changeToHomePage() {
        Intent intent = new Intent(SignUpActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    //Changes the Activity to Sign In Page
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
