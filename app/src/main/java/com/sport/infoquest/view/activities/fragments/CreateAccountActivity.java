package com.sport.infoquest.view.activities.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sport.infoquest.R;
import com.sport.infoquest.activity.BaseActivity;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.Factory;
import com.sport.infoquest.util.FirebaseUtility;

/**
 * Created by Ionut Neagu on 03/06/2017.
 */

public class CreateAccountActivity extends BaseActivity{
    public static final String TAG = "CreateAccountActivity";
    private String usernameText, passwordText;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;
    private EditText emailAddress;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_account);

        emailAddress = (EditText)findViewById(R.id.create_email_address);
        password = (EditText)findViewById(R.id.password_insert_new_accout);
        EditText repeatPassword = (EditText)findViewById(R.id.password_repeat_insert_new_accout);
        Button create_account = (Button) findViewById(R.id.btnCreateAccount);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        create_account.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameText = emailAddress.getText().toString().trim();
                passwordText = password.getText().toString().trim();
              createAccount(usernameText, passwordText);
            }
        }));
    }

    private void createAccount(final String email, final String password) {
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            Log.d(TAG, " -> User successfully created");
                            FirebaseUser fbUser = mAuth.getCurrentUser();
                            User user = Factory.createUser(fbUser);
                            databaseReference.child("users").child(user.getUid()).setValue(user);
                        } else {

                            Log.d(TAG, " -> User couldn't be created");
                            Toast.makeText(CreateAccountActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();

                        }
                        stopProgressDialog();
                    }


                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
