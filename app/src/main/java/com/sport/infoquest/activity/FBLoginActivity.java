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
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.sport.infoquest.R;
import com.sport.infoquest.util.FirebaseUtility;
import com.sport.infoquest.view.activities.fragments.CreateAccountActivity;

public class FBLoginActivity extends BaseActivity {
    private static final String TAG = "FBLoginActivity";
    private String usernameText, passwordText;
    private EditText usernameEditText, passwordEditText;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUtility firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_redesign);
        firebase = FirebaseUtility.getInstance();
        firebase.initFirebase();
        this.initRemoteConfiguration();

        Button login_button = (Button) findViewById(R.id.btnLogin);
        Button create_account = (Button) findViewById(R.id.btnNewAccount);
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        mAuth = firebase.getmAuth();
        mAuthListener = firebase.getmAuthListener();

        usernameText = usernameEditText.getText().toString().trim();
        passwordText = passwordEditText.getText().toString().trim();

        login_button.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgressDialog();
                        mAuth.signInWithEmailAndPassword(usernameText, passwordText)
                                .addOnCompleteListener(FBLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        if (!task.isSuccessful()) {
                                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                                            Toast.makeText(getApplicationContext(), R.string.auth_failed,
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                        // ...
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

    private void initRemoteConfiguration() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_default);
        long cacheExpiration = 3600; // 1 hour in seconds.

        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Fetch Succeeded");
                            mFirebaseRemoteConfig.activateFetched();
                            firebase.setmFirebaseRemoteConfig(mFirebaseRemoteConfig);
                        } else {
                            Log.d(TAG, "Fetch Failed");
                        }
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
