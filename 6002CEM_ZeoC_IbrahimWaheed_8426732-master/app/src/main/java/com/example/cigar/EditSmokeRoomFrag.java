package com.example.cigar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditSmokeRoomFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditSmokeRoomFrag extends Fragment implements View.OnClickListener {
    Button senddata;
    FirebaseCRUD crudOperation = new FirebaseCRUD();


    public static EditSmokeRoomFrag newInstance(String param1, String param2) {
        EditSmokeRoomFrag fragment = new EditSmokeRoomFrag();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_edit_smoke_room, container, false);
        senddata = (Button)view.findViewById(R.id.send_data_to_db) ;
        senddata.setOnClickListener(this);




        return  view;
    }





    @Override
    public void onClick(View v) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
      if (user != null) {
          EditText createBoxes = (EditText) getView().findViewById(R.id.create_boxes);
          String createBoxes_string = createBoxes.getText().toString();
          EditText createCigars = (EditText) getView().findViewById(R.id.create_cigars);
          String createCigars_string = createCigars.getText().toString();
          Log.d("ButtonDataFromEditSmoke", createBoxes_string + "   " + createCigars_string);
          String id = user.getUid();

          crudOperation.UpdateAddCigars(id,createBoxes_string,createCigars_string);
          //create if cond for listview click

      }
      else{
          Log.d("onClick","Firebase Authtication");
          Toast.makeText(getActivity(), "Create account", Toast.LENGTH_SHORT).show();
      }


    }
}