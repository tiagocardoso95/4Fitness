package com.example.tcard.cmproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.tcard.cmproject.Hydration.HydrationActivity;
import com.example.tcard.cmproject.UserStats.UserStats;
import com.example.tcard.cmproject.Utility.DB;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button hydrationManagerButton, runningTrackerButton;
    private TextView idText, NameText, EmailText;

    private FirebaseAuth fbAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setStatusBarColor(Color.BLACK);
        Toolbar toolbar = findViewById(R.id.homeToolbar);
        toolbar.setTitle("4F");
        Spinner spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.HomeOptions, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        fbAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        idText = findViewById(R.id.userID);
        NameText = findViewById(R.id.userName);
        EmailText = findViewById(R.id.userEmail);

        hydrationManagerButton = findViewById(R.id.hydrationManager_button);
        runningTrackerButton = findViewById(R.id.runningTracker_button);

        //Get User stats from DB;
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = DB.getInstance().getUserStatsTable().child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserStats stats = dataSnapshot.getValue(UserStats.class);
                Log.i("Error",stats.getHeight()+" | "+stats.getName() + ">---------------");
                UserStats.UpdateInstance(stats);

                //Teste para ir buscar os dados do utilizador FireBase e Facebook
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    for (UserInfo profile : user.getProviderData()) {
                        // UID specific to the provider
                        idText.setText("ID: "+profile.getUid());

                        // Name, email address
                        String name = profile.getDisplayName();
                        Log.i("HOME_DBGET","Gets name from DB");
                        if(name == null) {
                            name = UserStats.GetInstance().getName();
                            if(name == null) {
                                name = "fukin fix the db";
                            }
                        }
                        NameText.setText(name);
                        EmailText.setText("Email: "+profile.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });



        //Teste para ir buscar os dados do utilizador google
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            idText.setText("ID_Google: "+acct.getId());
            NameText.setText("Name_Google: "+acct.getDisplayName());
            EmailText.setText("Email_Google: "+acct.getEmail());
        }

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = (String)parent.getItemAtPosition(position);
        switch (text){
            case "Logout":
                fbAuth.signOut();
                LoginManager.getInstance().logOut();
                mGoogleSignInClient.signOut();
                showMessage("You are Logged out!");
                changeToSignUp();
                break;
            case "Options":
                Log.i("HOME_ACTIVITY","Options not implemented");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
