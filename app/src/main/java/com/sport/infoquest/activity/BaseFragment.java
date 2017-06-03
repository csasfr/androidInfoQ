package com.sport.infoquest.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

/**
 * Created by Ionut Neagu on 03/06/2017.
 */

public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    private static final String LOADING_MESSAGE_DIALOG = "loading_message";
    private static final String TITLE_DIALOG = "title_dialog";
    public ProgressDialog progressDialog = new ProgressDialog(getContext());
    public FirebaseRemoteConfig mFirebaseRemoteConfig;
    public FirebaseRemoteConfigSettings configSettings;

    public void showProgressDialog(){
        this.initProgressDialog();
        progressDialog.show();
    }

    public void stopProgressDialog(){
        progressDialog.dismiss();
    }

    public void initProgressDialog(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        progressDialog.setMax(100);
        progressDialog.setMessage(mFirebaseRemoteConfig.getString(LOADING_MESSAGE_DIALOG));
        progressDialog.setTitle(mFirebaseRemoteConfig.getString(TITLE_DIALOG));

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, " -> onViewCreated");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, " -> onDestroy");
    }

}
