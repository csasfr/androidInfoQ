package com.sport.infoquest.view.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.sport.infoquest.R;
import com.sport.infoquest.activity.BaseFragment;
import com.sport.infoquest.entity.CurrentScore;
import com.sport.infoquest.entity.CurrentUser;

import static com.sport.infoquest.enums.Drawer.HOME;

/**
 * Created by Ionut Neagu on 21/06/2017.
 */

public class LeaderBoardFragment extends BaseFragment {
    private static final String TAG = "LeaderBoardFragment";
    private View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_current_score, container, false);
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