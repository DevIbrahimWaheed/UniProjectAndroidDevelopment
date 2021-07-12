package com.example.cigar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class EditDialog extends AppCompatDialogFragment {
    private EditText editText;
    private EditDialogListener listener;
    int postion;
    String cigarbox;



    public EditDialog(int pos , String CigarBox ){
        this.postion = pos;
        this.cigarbox = CigarBox;


    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_editor,null);
        builder.setView(view).setTitle("Edited").setNegativeButton("cancel", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String cigarname = editText.getText().toString();
                listener.applyText(cigarname,postion,cigarbox);



            }
        });
        editText = view.findViewById(R.id.cigar_name_editText);
        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement EditDialogListener");
        }
    }

    public void show(int commit, String edit) {
    }

    public void show(Context context, String edit) {
    }


    // the interface method will hold the varaibles which wil be used in the home activty
    public  interface  EditDialogListener{
        void applyText(String cigarname , int postion, String cigarbox );

    }


}
