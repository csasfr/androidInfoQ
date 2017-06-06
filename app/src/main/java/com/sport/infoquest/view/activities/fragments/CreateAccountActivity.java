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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sport.infoquest.R;
import com.sport.infoquest.activity.BaseActivity;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.Factory;

/**
 * Created by Ionut Neagu on 03/06/2017.
 */

public class CreateAccountActivity extends BaseActivity{
    public static final String TAG = "CreateAccountActivity";
    private String usernameText, passwordText;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;
    private EditText emailAddressEditText;
    private EditText passwordEditText, repeatPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_account);

        emailAddressEditText = (EditText)findViewById(R.id.create_email_address);
        passwordEditText = (EditText)findViewById(R.id.password_insert_new_accout);
        repeatPasswordEditText = (EditText)findViewById(R.id.password_repeat_insert_new_accout);
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
                usernameText = emailAddressEditText.getText().toString().trim();
                passwordText = passwordEditText.getText().toString().trim();
                if (validate()) {
                    createAccount(usernameText, passwordText);
                } else {
                    Toast.makeText(getBaseContext(), "Date invalide, va rugam sa le corectati!", Toast.LENGTH_LONG).show();
                }
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

                        if (task.isSuccessful()) {
                            Log.d(TAG, " -> User successfully created");
                            FirebaseUser fbUser = mAuth.getCurrentUser();
                            User user = Factory.createUser(fbUser);
                            databaseReference.child("users").child(user.getUid()).setValue(user);
                            sendEmailVerification(fbUser);
                        } else {
                            Log.d(TAG, " -> User couldn't be created");
                            Toast.makeText(CreateAccountActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }


                });

    }

    private void sendEmailVerification(final FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // Re-enable button

                        if (task.isSuccessful()) {
                            Log.e(TAG, "sendEmailVerification -> Success !");
                            Toast.makeText(getApplicationContext(), "An email for confirmation was sent. Please check your inbox to activate your account!.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getApplicationContext(), "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        stopProgressDialog();
                    }
                });
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailAddressEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String passwordRepeat = repeatPasswordEditText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailAddressEditText.setError(String.valueOf(R.string.sign_up_email_error));
            valid = false;
        } else {
            emailAddressEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            passwordEditText.setError(String.valueOf(R.string.sign_up_password_error));
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        if (passwordRepeat.isEmpty() || !passwordRepeat.equals(password)) {
            repeatPasswordEditText.setError(String.valueOf(R.string.sign_up_repeat_password_error));
            valid = false;
        } else {
            repeatPasswordEditText.setError(null);
        }

        return valid;
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
