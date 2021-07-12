package com.example.cigar;


import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.security.acl.LastOwnerException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SmokeFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SmokeFrag extends Fragment   {



    Spinner mBoxesSpinner ;
    public RecyclerView mRecycleView;
    public String id;
  



    public static SmokeFrag newInstance(String param1, Integer param2) {
        SmokeFrag fragment = new SmokeFrag();
        Bundle args = new Bundle();
        //args.putString("name",param1);
      //  args.putInt("postion",param2);
        fragment.setArguments(args);
        return fragment;
    }






    public SmokeFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_smoke, container, false);
        FirebaseCRUD crudOperation = new FirebaseCRUD();


        updateView(inflater, container, view, crudOperation);


        return view;
    }

    // the method is incharge of the smoke room views/fragmenets
    public void updateView(LayoutInflater inflater, ViewGroup container, View view, FirebaseCRUD crud) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

              id = user.getUid();

            Log.d("IDfromAUTH",id);

            Log.d("SmokeFrag", "Not Null");
            mBoxesSpinner =  (Spinner) view.findViewById(R.id.choose_boxes);
            CallBackReadBoxes(id, view, crud, mBoxesSpinner);
           //CallBackReadContent(id, mRecycleView);


            // callback to grab async data


        } else {
            Log.d("SmokeFrag", "Null");
            Toast.makeText(getActivity(), "Create Account to add Data", Toast.LENGTH_SHORT).show();

        }
    }


    
    
    //watch notfication
    public void WatchSendMessage() {
    }

    
    // the method update the spinner to have the boxes names inside so you can  move around teh different boxes
    public void CallBackReadBoxes(String iduser, View view, FirebaseCRUD crud,Spinner boxes) {
        mRecycleView = (RecyclerView)view.findViewById(R.id.simple_recyle);
        ArrayList<String> ftDropDown = new ArrayList<>();
        crud.readBoxes(iduser, new FirebaseCallback() {
            @Override
            public void onResponse(ArrayList<String> data) {
                Log.d("what is in data",data.toString());


                try {

                Log.d("dropdown items",data.toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice, data);
                boxes.setAdapter(adapter);
                boxes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = boxes.getSelectedItem().toString();
                        Log.d("itemTESST", item);
                        // calls the adapter to update the recycle view
                        CallBackReadContent(iduser, item, mRecycleView);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                }
                catch (NullPointerException e){
                    Log.e("e","works");
                }
                        }});




    }

    // this method read the boxes again to get the update value so the recycle viiew can be updated
    public void CallBackReadContent(String id, String cigarBoxName,RecyclerView mRecycleView) {
        UpdateRecViewData(id,cigarBoxName, new FirebaseCallback() {
            @Override
            public void onResponse(ArrayList<String> cigaritems) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://zeocigar-default-rtdb.firebaseio.com/");
                DatabaseReference mBoxes = mDatabase.child("users").child(id).child("boxes").child(cigarBoxName);
                ArrayList<String> updateItems = new ArrayList<>();
                mBoxes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       for (DataSnapshot ds : snapshot.getChildren()){
                           Log.d("callback",ds.getValue().toString());
                           updateItems.add(ds.getValue().toString());

                       }

                        Log.d("Newcallback",updateItems.toString());
                        Log.d("fromFristCallback",cigaritems.toString());

                        RecAdpater apd = new RecAdpater(getContext(), updateItems, id ,cigarBoxName);

                        mRecycleView.setAdapter(apd);
                        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });
    }


    // the is the inital update view which will be called in callback
    public void UpdateRecViewData(String userID, String CigarBoxName, FirebaseCallback callback) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://zeocigar-default-rtdb.firebaseio.com/");
        DatabaseReference mBoxes = mDatabase.child("users").child(userID).child("boxes").child(CigarBoxName);
        ArrayList<String> data = new ArrayList();
        mBoxes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    Log.d("onDataChange", (String) ds.getValue());
                   // data.add(ds.getValue().toString());

                }
                callback.onResponse(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    // interface method to add the array data so the callback so the data can be used in the methdo where we need the data
    public interface FirebaseCallback {
        void onResponse(ArrayList<String> name);

    }
}






