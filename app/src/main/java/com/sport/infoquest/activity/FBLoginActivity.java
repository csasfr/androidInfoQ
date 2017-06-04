package com.sport.infoquest.activity;

import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sport.infoquest.R;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.Factory;
import com.sport.infoquest.view.activities.HomeActivity;
import com.sport.infoquest.view.activities.fragments.CreateAccountActivity;

import java.util.HashMap;
import java.util.Map;

public class FBLoginActivity extends BaseActivity {
    private static final String TAG = "FBLoginActivity";
    private String usernameText, passwordText;
    private EditText usernameEditText, passwordEditText;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_redesign);

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

        Button login_button = (Button) findViewById(R.id.btnLogin);
        Button create_account = (Button) findViewById(R.id.btnNewAccount);
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        login_button.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                usernameText = "csasfr@yahoo.com";//usernameEditText.getText().toString().trim();
                passwordText = "1qaz2wsx";//passwordEditText.getText().toString().trim();
                mAuth.signInWithEmailAndPassword(usernameText, passwordText)
                        .addOnCompleteListener(FBLoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                    stopProgressDialog();
                                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                                    startActivity(intent);

                                } else {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    Toast.makeText(getApplicationContext(), R.string.auth_failed,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }));

        create_account.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CreateAccountActivity.class);
                startActivity(intent);
            }
        }));
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
