package com.example.cigar;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class RecAdpater extends RecyclerView.Adapter<RecAdpater.MyViewHolder>  {
    ArrayList<String> data1;
    Context context;
    String iduser;
    String cigarBoxID;
    getPosNumber listener;



    ArrayList<String> images = new ArrayList<>();
    int Postions;
    public RecAdpater(Context ct , ArrayList<String> cigars, String userID, String cigarBoxes ){
        context =ct;
        data1 = cigars;
        cigarBoxID = cigarBoxes;
        iduser = userID;


        // add the images to array



        // testing purposes
        Log.d("Images Ids",images.toString());
        Log.d("fromRecAdpater",cigars.toString());
        Log.d("fromRecAdpater",userID);
        Log.d("fromRecAdpater",cigarBoxes);
       // images = img;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_cigar_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> key = new ArrayList<>();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://zeocigar-default-rtdb.firebaseio.com/");
                 DatabaseReference mboxs = mDatabase.child("users").child(iduser).child("boxes").child(cigarBoxID);
                 mboxs.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {

                         for(DataSnapshot items : snapshot.getChildren()) {
                             key.add(items.getKey());



                         }
                         Query del = mboxs.child(key.get(position));
                         del.getRef().removeValue();

                         }




                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });

            }
        });
        holder.cigar_text.setText(data1.get(position));
        Log.d("imgs",images.toString());


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://zeocigar-default-rtdb.firebaseio.com/");
        Task data = mDatabase.child("users").child(iduser).child("cigar_images").get();
        data.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                DataSnapshot data = (DataSnapshot) task.getResult();
                for (DataSnapshot ds : data.getChildren()){
                    holder.myImages.setImageResource(((Long) ds.getValue()).intValue());
                };

            }
        });

        // calls the dialog to edit text
        holder.editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditor(v, holder.getAdapterPosition(),cigarBoxID);
                
            }
        });



    }

    @Override
    public int getItemCount() {
        //Log.d("count", String.valueOf(data1.size()));
        return data1.size();
    }

    // open the dialog
    public void openEditor(View v , int pos , String CigarBox ){
        AppCompatActivity app =(AppCompatActivity) v.getContext();
        EditDialog editDialog = new EditDialog(pos,cigarBoxID);
        editDialog.show(app.getSupportFragmentManager(),"edit");


    }




    public interface getPosNumber{
        void onItemClick(int pos);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView cigar_text;
        ImageView myImages;
        Button deleteItem;
        Button editItem;
        TextView editCigarText;
        TextView Title;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        cigar_text = itemView.findViewById(R.id.cigars_names);
        deleteItem = itemView.findViewById(R.id.deleteItem);
        myImages = itemView.findViewById(R.id.cigar_images);
        editItem= itemView.findViewById(R.id.EditItem);





    }


    }
}

