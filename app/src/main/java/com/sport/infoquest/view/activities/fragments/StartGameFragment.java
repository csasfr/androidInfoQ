package com.sport.infoquest.view.activities.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sport.infoquest.R;
import com.sport.infoquest.activity.BaseFragment;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.Utils;

import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.sport.infoquest.enums.Drawer.SCAN_QR;
import static com.sport.infoquest.util.GameName.EDENLAND;

/**
 * Created by Ionut on 14/03/2017.
 */

public class StartGameFragment extends BaseFragment {
    private static final String TAG = "StartGameFragment";
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_start_game, container, false);
        TextView gameName = (TextView) rootView.findViewById(R.id.gameName);

        if (User.getInstance().getSelectedGame().getName().equals(EDENLAND.getNameValue())) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.edenImage);
            imageView.setVisibility(VISIBLE);
            gameName.setVisibility(INVISIBLE);
        }

        TextView coins = (TextView) rootView.findViewById(R.id.coins);
        gameName.setText(User.getInstance().getSelectedGame().getName());
        coins.setText("" + User.getInstance().getSelectedGame().getCost());

        Button newGame = (Button) rootView.findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showProgressDialog();
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, String> currentUser = (Map) dataSnapshot.getValue();
                                boolean hasCredit = false;
                                int finalCredit = 0;
                                for (Map.Entry<String, String> entry:currentUser.entrySet()) {
                                    if (entry.getKey().contains("credit")) {
                                        String value = String.valueOf(entry.getValue());
                                        int currentUserCredit = Integer.valueOf(value);
                                        finalCredit = currentUserCredit - User.getInstance().getSelectedGame().getCost();
                                        if (finalCredit > 0) {
                                            hasCredit = true;
                                        }
                                    }
                                }
                                if (hasCredit){
                                    FirebaseDatabase.getInstance().getReference().child("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("credit")
                                            .setValue(finalCredit);

                                    FirebaseDatabase.getInstance().getReference().child("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("currentTrack")
                                            .setValue(User.getInstance().getSelectedGame().getName());
                                    FirebaseDatabase.getInstance().getReference().child("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("onTrack")
                                            .setValue(true);

                                    Fragment fragment = new ScanQRFragment();
                                    Utils.addFragment(fragment, SCAN_QR.getName(), getFragmentManager());
                                } else {
                                    Utils.showMessage(getContext(), "Credit insuficient pentru joc!");
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
        });

        return rootView;
    }

}
