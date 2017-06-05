package com.sport.infoquest.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.sport.infoquest.R;

/**
 * Created by Ionut Neagu on 03/06/2017.
 */

public class BaseActivity extends Activity{
    private static final String TAG = "BaseActivity";
    private static final String LOADING_MESSAGE_DIALOG = "loading_message";
    private static final String TITLE_DIALOG = "title_dialog";
    public ProgressDialog progressDialog;
    public FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        initRemoteConfiguration();
        Log.d(TAG, "-> onCreate");
    }

    public void showProgressDialog(){
        this.initProgressDialog();
        progressDialog.show();
    }

    public void stopProgressDialog(){
        progressDialog.dismiss();
    }

    public void initProgressDialog(){
        progressDialog.setMax(100);
        progressDialog.setMessage(mFirebaseRemoteConfig.getString(LOADING_MESSAGE_DIALOG));
        progressDialog.setTitle(mFirebaseRemoteConfig.getString(TITLE_DIALOG));

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
                        } else {
                            Log.d(TAG, "Fetch Failed");
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(this.getClass().getName(), " -> onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(this.getClass().getName(), " -> onResume");
    }

    @Override
    protected void onPause() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
        Log.d(this.getClass().getName(), " -> onPause");
    }

    @Override
    protected void onStop() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
        Log.d(this.getClass().getName(), " -> onStop");
    }

    @Override
    protected void onDestroy() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
        Log.d(TAG, " -> onDestroy");
    }
}
