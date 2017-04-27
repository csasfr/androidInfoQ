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

import com.sport.infoquest.R;
import com.sport.infoquest.activity.ScanQR;
import com.sport.infoquest.entity.Game;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.JSONResponse;
import com.sport.infoquest.util.RestService;
import com.sport.infoquest.util.StatusCode;
import com.sport.infoquest.util.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.sport.infoquest.enums.Drawer.SCAN_QR;
import static com.sport.infoquest.util.GameName.EDENLAND;

/**
 * Created by Ionut on 14/03/2017.
 */

public class StartGameFragment extends Fragment {
    private static final String FRAGMENT = "StartGameFragment";
    private boolean showDialog = true;
    private ListView lv;
    private View rootView;
    private JSONResponse response;

    private User presenter = User.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_start_game, container, false);
        Game currentGame = User.getInstance().getSelectedGame();
        TextView gameName = (TextView) rootView.findViewById(R.id.gameName);
        if (currentGame.getName().equals(EDENLAND.getNameValue())) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.edenImage);
            imageView.setVisibility(VISIBLE);
            gameName.setVisibility(INVISIBLE);
        }
        TextView coins = (TextView) rootView.findViewById(R.id.coins);

        gameName.setText(User.getInstance().getSelectedGame().getName());
        //minutes.setText(User.getInstance().getSelectedGame().getTime());
        coins.setText(User.getInstance().getSelectedGame().getCost());
        Button newGame = (Button) rootView.findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String gameName = User.getInstance().getSelectedGame().getName();
                String userName = User.getInstance().getUsername();
                if (checkCredit(userName, gameName)) {
                    this.setCurrentGame();
                    Fragment fragment = new ScanQRFragment();
                    addFragment(fragment, SCAN_QR.getName());
                } else {
                    Utils.showMessage(getContext(), "Credit insuficient pentru joc!");
                }
            }

            private void setCurrentGame() {
                User.getInstance().setCurrentGame(User.getInstance().getSelectedGame());
            }


        });
        return rootView;
    }

    private void addFragment(Fragment fragment, String stackName) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, fragment, stackName);
        transaction.addToBackStack(stackName);
        transaction.commit();
    }

    private boolean checkCredit(String user, String gameName) {
        try {
            response = RestService.postStartGameWithCreditCheck(user, gameName);
            if (response.getResponseCode() != StatusCode.OK.getCode()) {
                return false;
            } else {
                Log.i(FRAGMENT, "200 - New game started for user " + User.getInstance().getUsername() + " and game " + User.getInstance().getSelectedGame().getName());
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}
