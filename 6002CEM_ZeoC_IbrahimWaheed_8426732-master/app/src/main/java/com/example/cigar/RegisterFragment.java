package com.example.cigar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    // Create the EditText
    EditText edit_email_text;
    EditText edit_password_text;
    Button enter_data;
    TextView validate_email;
    TextView validate_password;



    private FirebaseAuth mAuth;
    // TODO: Rename parameter arguments, choose names that match

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        View v;
        v = inflater.inflate(R.layout.fragment_register,container,false);

        mAuth =FirebaseAuth.getInstance();
        enter_data = v.findViewById(R.id.enter_reg);
        edit_email_text = v.findViewById(R.id.editTextTextEmailAddress);
        edit_password_text = v.findViewById(R.id.editTextTextPassword);
        validate_email = v.findViewById(R.id.vaildate_email);
        validate_password = v.findViewById(R.id.vaildate_password);



            enter_data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String getPassword = edit_password_text.getText().toString();
                    String getEmail = edit_email_text.getText().toString();
                    if(getEmail.isEmpty()){
                        Toast.makeText(getContext(), "Email must not be empty.",
                                Toast.LENGTH_SHORT).show();
                    }
                    if (getPassword.length() < 6) {
                        Toast.makeText(getContext(), "Password Length must be more than 6.",
                                Toast.LENGTH_SHORT).show();

                    } else {

                        Log.d(TAG, "onClick:" + getPassword + "----" + getEmail);


                        createAuthLocal(getEmail, getPassword);

                    }
                }


            });


        return v;


    }

    public void createAuthLocal(String email , String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener( getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    validate_password.setText("Account Is Register");
                    FirebaseUser user = mAuth.getCurrentUser();
                    FirebaseCRUD crud = new FirebaseCRUD();
                    crud.CreateNewUserLocal(user);

                }
                else{
                    // If sign in fails, display a message to the user.
                    try{
                        throw task.getException();
                    }
                        catch(FirebaseAuthWeakPasswordException e) {
                            edit_password_text.setError(getString(R.string.error_weak_password));
                            edit_password_text.requestFocus();
                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            edit_email_text.setError(getString(R.string.error_invalid_email));
                            edit_email_text.requestFocus();
                        } catch(FirebaseAuthUserCollisionException e) {
                           edit_email_text.setError(getString(R.string.error_user_exists));
                            edit_email_text.requestFocus();
                        } catch(Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }

        });
    }


    // check current session
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){

        }

    }




    // validation for email
    public static boolean isValidEmail(CharSequence target) {
        if (!TextUtils.isEmpty(target)) {
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(target).matches()){
            return false;
        }
        else {
            return true;
        }
    }



    public void backBtn(View view) {
        Intent i = new Intent(getActivity(), HomeActivity.class);

        startActivity(i);
    }
}