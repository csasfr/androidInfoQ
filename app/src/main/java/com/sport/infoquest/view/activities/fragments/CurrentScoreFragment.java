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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sport.infoquest.R;
import com.sport.infoquest.activity.QR;
import com.sport.infoquest.adapter.QRListAdapter;
import com.sport.infoquest.entity.Game;
import com.sport.infoquest.entity.IdHint;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.Factory;
import com.sport.infoquest.util.JSONResponse;
import com.sport.infoquest.util.RestService;
import com.sport.infoquest.util.StatusCode;
import com.sport.infoquest.util.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import static com.sport.infoquest.enums.Drawer.HOME;
import static com.sport.infoquest.enums.Drawer.SCAN_QR;

/**
 * Created by Ionut on 18/04/2017.
 */

public class CurrentScoreFragment extends Fragment {

    private static final String FRAGMENT = "CurrentScoreFragment";
    private View rootView;
    private Game currentGame;

    public ProgressDialog pDialog;
    JSONResponse response;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.frame_current_score, container, false);
        try {
            Factory.createUser(RestService.getUserInformation(User.getInstance().getUsername()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        currentGame = User.getInstance().getCurrentGame();
        if (currentGame != null) {
            TextView score = (TextView) rootView.findViewById(R.id.scoreValue);
            score.setText(User.getInstance().getCurrentScore());
        } else {
            Toast.makeText(getContext(), "Nu aveti joc in desfasurare pentru a verifica scorul curent.", Toast.LENGTH_LONG).show();
        }

        return rootView;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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


    public class LoadAsync extends AsyncTask<String, Void, JSONResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Va rugam sa asteptati...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected JSONResponse doInBackground(String... params) {
            try {
                //Factory.createUser(RestService.getUserInformation(User.getInstance().getUsername()));
                return response = RestService.postExitGame(User.getInstance().getUsername());
            } catch (JSONException | IOException e) {
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
            return null;
        }

        protected void onPostExecute(JSONResponse response) {
            if (response.getResponseCode() != StatusCode.OK.getCode()) {
                pDialog.dismiss();

                //scanned = User.getInstance().getScannedQuestions().split(",");
            } else {
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
            Utils.removeFragment(SCAN_QR.getName(), getFragmentManager());

        }
    }

}