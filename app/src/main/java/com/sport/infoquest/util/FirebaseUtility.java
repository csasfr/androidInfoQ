package com.sport.infoquest.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.sport.infoquest.R;

import java.util.concurrent.Executor;

/**
 * Created by Ionut Neagu on 03/06/2017.
 */

public class FirebaseUtility {
    private static final String TAG = "FirebaseUtility";
    public static FirebaseUtility instance = null;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseRemoteConfigSettings configSettings;

    public static FirebaseUtility getInstance (){
        if (instance == null) {
           return instance = new FirebaseUtility();
        } else {
            return instance;
        }
    }

    public void initFirebase (){
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
    }


    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public FirebaseAuth.AuthStateListener getmAuthListener() {
        return mAuthListener;
    }

    public FirebaseRemoteConfig getmFirebaseRemoteConfig() {
        return mFirebaseRemoteConfig;
    }

    public FirebaseRemoteConfigSettings getConfigSettings() {
        return configSettings;
    }


    public void setmFirebaseRemoteConfig(FirebaseRemoteConfig mFirebaseRemoteConfig) {
        this.mFirebaseRemoteConfig = mFirebaseRemoteConfig;
    }

    public void setConfigSettings(FirebaseRemoteConfigSettings configSettings) {
        this.configSettings = configSettings;
    }
}
