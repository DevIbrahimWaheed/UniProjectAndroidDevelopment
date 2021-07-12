package com.example.cigar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;



import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public String idGrabber;
    private SignInButton signInButton;
    public GoogleSignInClient mGooglesignin;
    private String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN = 1;

    // navbar
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInButton = findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGooglesignin = GoogleSignIn.getClient(this, googleSignInOptions);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v ) {
                signIn();
            }
        });
        // logic for toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.login_page);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawerLayout ,toolbar,R.string.navbar_draw_open,R.string.navbar_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.FirstFragment_01,
                    new LoginFragment()).commit();
            navigationView.setCheckedItem(R.id.menu_logintosystem);}
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_reg:
                getSupportFragmentManager().beginTransaction().replace(R.id.FirstFragment_01,
                        new RegisterFragment()).commit();
                break;
            case R.id.menu_logintosystem:
                getSupportFragmentManager().beginTransaction().replace(R.id.FirstFragment_01,
                        new LoginFragment()).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }


    public void signIn() {
        Intent signInIntent = mGooglesignin.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completetask) {
        try {
            GoogleSignInAccount acc = completetask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this, "SignIN done", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);

        } catch (ApiException e) {
            Toast.makeText(LoginActivity.this, "SignIN not succeed", Toast.LENGTH_SHORT).show();
           // FirebaseGoogleAuth(null);
        }
    }


    private void FirebaseGoogleAuth(GoogleSignInAccount accdata) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(accdata.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("Connected","YEs Connect to Firebase");
                    FirebaseUser user = mAuth.getCurrentUser();


                    updateUI(user);
                }
                else { Toast.makeText(LoginActivity.this, "Firebase Unsuccessfully", Toast.LENGTH_SHORT).show();
                    updateUI(null);}
            }

        });
    }

    // this method below updates the UI with google information in next code used this to for fragment navbar style
    private  void  updateUI(FirebaseUser firebaseUser ){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null){
            String userName  = account.getDisplayName();
            String idToken = FirebaseAuth.getInstance().getUid();
            String userEmail = account.getEmail();
            Log.d("email", userEmail);
            Log.d("token", idToken);
            Log.d("name", userName);
            FirebaseCRUD crud = new FirebaseCRUD();
            crud.CreateNewUser(idToken,userName,userEmail);

            //for image

        }


    }



    public void backBtn(View view){
        Intent i = new Intent(LoginActivity.this, HomeActivity.class);

        startActivity(i);

        // close this activity

        finish();

    }





}

