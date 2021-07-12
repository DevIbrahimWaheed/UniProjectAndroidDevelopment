package com.example.cigar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable(){
            public void run() {

                Intent i = new Intent(MainActivity.this, HomeActivity.class);

                startActivity(i);

                // close this activity
                FirebaseCRUD crud = new FirebaseCRUD();
                crud.readBoxesWithoutCallBack("test");
                finish();

            }
        },2*1000);

    }


}