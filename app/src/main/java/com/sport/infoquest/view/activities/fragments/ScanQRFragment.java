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
import android.widget.Toast;

import com.sport.infoquest.R;
import com.sport.infoquest.activity.QR;
import com.sport.infoquest.activity.ScanQR;
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
import java.util.HashMap;
import java.util.List;

import static com.sport.infoquest.enums.Drawer.HOME;
import static com.sport.infoquest.enums.Drawer.SCAN_QR;

/**
 * Created by Ionut on 14/03/2017.
 */

public class ScanQRFragment extends Fragment {

    private static final String FRAGMENT = "ScanQRFragment";
    private View rootView;
    private Button qrButton;
    private Button checkScore;
    private Button exitButton;
    private String questionId;
    private String isFinish;
    private Game currentGame;

    public static QRListAdapter adapter;
    List<IdHint> dataIdHint;
    ListView listView;

    public ProgressDialog pDialog;
    JSONResponse response;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.frame_scanqr, container, false);
        currentGame = User.getInstance().getCurrentGame();

        Intent intent = getActivity().getIntent();
        questionId = (String) intent.getSerializableExtra("questionId");
        isFinish = (String) intent.getSerializableExtra("finishGame");
        listView = (ListView) rootView.findViewById(R.id.hintList);
        qrButton = (Button) rootView.findViewById(R.id.qrButton);
        checkScore = (Button) rootView.findViewById(R.id.checkScore);
        exitButton = (Button) rootView.findViewById(R.id.exitGame);


        if (savedInstanceState != null) {
            adapter = (QRListAdapter) savedInstanceState.getSerializable("QRAdapterList");
            listView.setAdapter(adapter);
        } else {
            dataIdHint = currentGame.getIdHintList();
            if (questionId != null && questionId != "") {
                if (User.getInstance().getMarked() != null) {
                    User.getInstance().getMarked().add(questionId);
                }
            }
            adapter = new QRListAdapter(dataIdHint, getContext(), User.getInstance().getMarked());
            listView.setAdapter(adapter);
        }


        qrButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getContext(), QR.class);
                startActivity(i);
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Alerta");
                builder.setMessage(R.string.exit_game);
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new LoadAsync().execute();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        checkScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Factory.createUser(RestService.getUserInformation(User.getInstance().getUsername()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), "Scor total: " + User.getInstance().getCurrentScore(), Toast.LENGTH_LONG).show();
            }
        });
        if (isFinish != null && isFinish != "") {
            if (isFinish.equals("true")) {
                Toast.makeText(getContext(), "Felicitari! Ai terminat jocul cu scorul x.", Toast.LENGTH_LONG).show();
            }
        }
        return rootView;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("QRAdapterList", adapter);
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
