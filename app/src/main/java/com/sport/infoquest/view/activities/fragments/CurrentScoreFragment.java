package com.sport.infoquest.view.activities.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sport.infoquest.R;
import com.sport.infoquest.activity.BaseFragment;
import com.sport.infoquest.activity.QR;
import com.sport.infoquest.adapter.QRListAdapter;
import com.sport.infoquest.entity.CurrentScore;
import com.sport.infoquest.entity.CurrentUser;
import com.sport.infoquest.entity.Game;
import com.sport.infoquest.entity.IdHint;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.Factory;
import com.sport.infoquest.util.JSONResponse;
import com.sport.infoquest.util.RestService;
import com.sport.infoquest.util.StatusCode;
import com.sport.infoquest.util.Utils;
import com.sport.infoquest.view.activities.HomeActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.sport.infoquest.enums.Drawer.HOME;
import static com.sport.infoquest.enums.Drawer.SCAN_QR;

/**
 * Created by Ionut on 18/04/2017.
 */

public class CurrentScoreFragment extends BaseFragment {

    private static final String TAG = "CurrentScoreFragment";
    private View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.frame_current_score, container, false);
        checkScore();
        return rootView;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void checkScore() {
        showProgressDialog();
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot usersSnapshot = dataSnapshot.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        CurrentScore currentScore = new CurrentScore();
                        GenericTypeIndicator<CurrentUser> genericTypeUser = new GenericTypeIndicator<CurrentUser>() {};
                        CurrentUser currentUser = usersSnapshot.getValue(genericTypeUser);

                        if (currentUser.isOnTrack()){
                            DataSnapshot scoreSnapshot = dataSnapshot.child("scores").child("currentScore").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            GenericTypeIndicator<CurrentScore> genericTypeScore = new GenericTypeIndicator<CurrentScore>() {};
                            currentScore = scoreSnapshot.getValue(genericTypeScore);
                            updateUi(currentScore);
                        } else {
                            updateUiNoGames();
                        }
                        stopProgressDialog();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                        stopProgressDialog();
                    }
                });

    }

    private void updateUiNoGames() {
        Toast.makeText(getActivity(), "Nu aveti un joc in desfasurare!", Toast.LENGTH_LONG);
    }

    public void onBackPressed() {
        // currently visible tab Fragment
        HomeFragment myFragment = (HomeFragment) getFragmentManager().findFragmentByTag(HOME.getName());
        if (myFragment != null && myFragment.isVisible()) {

            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.container, myFragment);
            transaction.commit();
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
        }

    }

    private void updateUi(final  CurrentScore currentScore){
        if (currentScore != null) {
            TextView score = (TextView) rootView.findViewById(R.id.scoreValue);
            score.setText(String.valueOf(currentScore.getCurrentScore()));
            TextView correct = (TextView) rootView.findViewById(R.id.correctQuestionValue);
            correct.setText(String.valueOf(currentScore.getCorrectAnswer()));
            TextView wrong = (TextView) rootView.findViewById(R.id.wrongQuestionValue);
            wrong.setText(String.valueOf(currentScore.getWrongAnswer()));

        } else {
            Toast.makeText(getContext(), "Nu aveti joc in desfasurare pentru a verifica scorul curent.", Toast.LENGTH_LONG).show();
        }
    }
}