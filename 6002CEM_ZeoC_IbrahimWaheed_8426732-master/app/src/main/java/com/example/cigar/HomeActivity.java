package com.example.cigar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , EditDialog.EditDialogListener  {
    private DrawerLayout drawerLayout;
    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Button editItem;
    private static final int REQUEST_IMAGE_CAPTURE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        frameLayout = (FrameLayout) findViewById(R.id.FirstFragment_01);
        fragment = new SmokeFrag();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        editItem = findViewById(R.id.EditItem);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.home_page);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navbar_draw_open, R.string.navbar_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            fragmentTransaction.replace(R.id.FirstFragment_01, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();

            navigationView.setCheckedItem(R.id.menu_smoke_room);
        }


        //tab switch case
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new SmokeFrag();
                        break;
                    case 1:
                        fragment = new EditSmokeRoomFrag();
                        break;

                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.FirstFragment_01, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                encodeBitmapAndSaveToFirebase(imageBitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://zeocigar-default-rtdb.firebaseio.com/");
        DatabaseReference mBoxes = mDatabase.child("users").child(id).child("images").push();
        mBoxes.setValue(imageEncoded);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_smoke_room:
                getSupportFragmentManager().beginTransaction().replace(R.id.FirstFragment_01,
                        new SmokeFrag()).commit();
                break;
            case R.id.menu_login_in:

                Intent i = new Intent(HomeActivity.this, LoginActivity.class);

                startActivity(i);

                // close this activity

                finish();

                break;
            case R.id.menu_websmoke:
                getSupportFragmentManager().beginTransaction().replace(R.id.FirstFragment_01,
                        new ScrapFrag()).commit();
                break;
            case R.id.menu_takecamera:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    LaunchCamera();
                    Log.d("cameramenu","works!");
                }
                else{
                    Toast.makeText(this,"Create Account First",Toast.LENGTH_SHORT).show();
                }

             break;
            case R.id.menu_takecamera_gallery:
                Intent CameraArea = new Intent(HomeActivity.this , CameraArea.class);
                startActivity(CameraArea);
                finish();
                break;

            case R.id.menu_localCigarsStores:
                Intent localStore = new Intent(HomeActivity.this , LocalStoreCigarMaps.class);
                startActivity(localStore);
                finish();
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    public void LaunchCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);


    }
    // very crude way to applytext to the smokefrag using the editdialog.....
    // the interface method will then have the set varaible which can be used within the method
    @Override
    public void applyText(String cigarname, int pos, String cigarboxes) {
        ArrayList<String> key = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id;
        id = user.getUid();
        Log.d("ApplyTExt", cigarname);
        Log.d("gotthegoods",cigarboxes + " postion " + pos );
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://zeocigar-default-rtdb.firebaseio.com/");
        mDatabase.child("users").child(id).child("boxes").child(cigarboxes).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DataSnapshot> task) {
                 DataSnapshot dataSnapshot = task.getResult();
                 for(DataSnapshot ds: dataSnapshot.getChildren()){
                     key.add(ds.getKey());
                 }
                 Query edit = mDatabase.child("users").child(id).child("boxes").child(cigarboxes).child(key.get(pos));
                 edit.getRef().setValue(cigarname);
                 Log.d("key",key.toString());
             }
         });





    }





}










