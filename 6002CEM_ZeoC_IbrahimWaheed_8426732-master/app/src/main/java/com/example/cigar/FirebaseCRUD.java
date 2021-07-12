package com.example.cigar;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class FirebaseCRUD {
    ///acesss gets


    private FirebaseAuth mAuth;
// ...

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://zeocigar-default-rtdb.firebaseio.com/");
    CigarModel m = new CigarModel();

    // called model
    public FirebaseCRUD() {


    }

    //Warning this will overwrite it is a create method not update
    // this method is for Google Sign In not local
    public void CreateNewUser(String userID, String name, String email) {
        String localuserID =  FirebaseAuth.getInstance().getCurrentUser().getUid();
      DatabaseReference checkusers = mDatabase.child("users").child(localuserID);
        checkusers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    DatabaseMapper model = new DatabaseMapper(email,name);
                    checkusers.setValue(model);
                    CreateCigarImages(localuserID,mDatabase);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    // method for local register if you don't want to use gmail sign in
    public void CreateNewUserLocal(FirebaseUser user){

       String localuserID =  FirebaseAuth.getInstance().getCurrentUser().getUid();
       DatabaseReference checkusers = mDatabase.child("users").child(localuserID);
       checkusers.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(!snapshot.exists()){
                   DatabaseMapper model = new DatabaseMapper(user.getEmail(),null);
                   checkusers.setValue(model);
                   CreateCigarImages(localuserID,mDatabase);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });



    }











    // create a array where the images are set in the firebase database
    public void CreateCigarImages(String userID,DatabaseReference mDatabase){
        ArrayList<Integer> imagesID = new ArrayList<>();
        imagesID.add(R.drawable.cigar_1);
        imagesID.add(R.drawable.cigar_2);
        imagesID.add(R.drawable.cigar_3);


        DatabaseReference cigar_images_access = mDatabase.child("users").child(userID);
        cigar_images_access.child("cigar_images").setValue(imagesID);

    }


    public void UpdateAddCigars(String userID, String CigarBoxName, String addcigar) {
        Map<String, Object> appendData = m.toJSON();
        String cigartag = mDatabase.child("users").child(userID).child(CigarBoxName).push().getKey();
        appendData.put(cigartag, addcigar);
        // create word list to send warning to user on username and email
        mDatabase.child("users").child(userID).child("boxes").child(CigarBoxName).updateChildren(appendData);

    }


    public void readBoxes(String userID, SmokeFrag.FirebaseCallback callback) {
        DatabaseReference mBoxes = mDatabase.child("users").child(userID).child("boxes");
        ArrayList<String> data = new ArrayList();
        mBoxes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){
                    //Log.d("testfromread",ds.getKey());
               data.add(ds.getKey());


                }
                callback.onResponse(data);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void readBoxesWithoutCallBack(String userID) {
        DatabaseReference mBoxes = mDatabase.child("users").child(userID).child("boxes");
        ArrayList<String> data = new ArrayList();
        mBoxes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){
                    //Log.d("testfromread",ds.getKey());
                    String Value = ds.getValue().toString();
                    data.add(Value);
                    Log.d("ReadboxeasTEst",Value);


                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}







