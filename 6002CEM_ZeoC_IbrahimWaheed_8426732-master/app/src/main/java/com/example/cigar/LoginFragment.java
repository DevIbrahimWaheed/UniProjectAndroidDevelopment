package com.example.cigar;

import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // Create the EditText
    EditText edit_email_text;
    EditText edit_password_text;
    Button enter_data;
    TextView validate_email;
    TextView validate_password;
    Button signout;
    Switch log_reg;

    private FirebaseAuth mAuth;
    // TODO: Rename parameter arguments, choose names that match

    public LoginFragment() {
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
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        v = inflater.inflate(R.layout.fragment_login,container,false);

        mAuth =FirebaseAuth.getInstance();
        enter_data = v.findViewById(R.id.enter_log);
        edit_email_text = v.findViewById(R.id.editTextTextEmailAddress2);
        edit_password_text = v.findViewById(R.id.editTextTextPassword2);
        validate_email = v.findViewById(R.id.vaildate_email2);
        validate_password = v.findViewById(R.id.vaildate_password2);
        signout = v.findViewById(R.id.logout2);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    signOut();
                    FirebaseAuth.getInstance().signOut();
                }else{
                    Toast.makeText(getActivity(),"you are not login...",Toast.LENGTH_SHORT).show();
                }




            }
        });

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




                        Log.d(TAG, "onClick:" + getPassword + "" + getEmail);


                        loginAuthLocal(getEmail, getPassword);

                    }
                }

            });



        return v;


    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();
        GoogleSignInClient googleSignInClient=GoogleSignIn.getClient(getContext(),gso);
        googleSignInClient.signOut();

    }

    public void loginAuthLocal(String email ,String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "You have login.",
                            Toast.LENGTH_SHORT).show();


                }else {
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
            Log.d(TAG, "You already login");

        }
        else {
            Log.d(TAG, "onStart: Session not loaded on app");
        }

    }







    public void backBtn(View view) {
        Intent i = new Intent(getActivity(), HomeActivity.class);

        startActivity(i);
    }
}