package com.sport.infoquest.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sport.infoquest.R;
import com.sport.infoquest.entity.Game;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.Factory;
import com.sport.infoquest.util.JSONResponse;
import com.sport.infoquest.util.RestService;
import com.sport.infoquest.util.StatusCode;
import com.sport.infoquest.util.Utils;

import org.json.JSONException;

import java.io.IOException;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.sport.infoquest.util.GameName.EDENLAND;

public class StartGame extends Activity {
    JSONResponse response;
    public ProgressDialog pDialog;
    private static final String TAG_NAME = "StartGame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (User.getInstance().getSelectedGame() == null) {
            this.finish();
        }

        Game currentGame = User.getInstance().getSelectedGame();
        setContentView(R.layout.activity_start_game);
        TextView gameName = (TextView) findViewById(R.id.gameName);
        if (currentGame.getName().equals(EDENLAND.getNameValue())) {
            ImageView imageView = (ImageView)findViewById(R.id.edenImage);
            imageView.setVisibility(VISIBLE);
            gameName.setVisibility(INVISIBLE);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //TextView minutes = (TextView) findViewById(R.id.minute);
        TextView coins = (TextView) findViewById(R.id.coins);


        gameName.setText(User.getInstance().getSelectedGame().getName());
        //minutes.setText(User.getInstance().getSelectedGame().getTime());
        coins.setText(User.getInstance().getSelectedGame().getCost());
        Button newGame = (Button) findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String gameName = User.getInstance().getSelectedGame().getName();
                String userName = User.getInstance().getUsername();
                if (checkCredit(userName, gameName)) {
                    this.setCurrentGame();
                    Intent i = new Intent(getApplicationContext(), ScanQR.class);
                    startActivity(i);
                    finish();
                } else {
                    Utils.showMessage(getApplicationContext(), "Credit insuficient pentru joc!");
                    finish();
                }
            }

            private void setCurrentGame() {
                User.getInstance().setCurrentGame(User.getInstance().getSelectedGame());
            }


        });
    }

    private boolean checkCredit(String user, String gameName) {
        try {
            response = RestService.postStartGameWithCreditCheck(user, gameName);
            if (response.getResponseCode() != StatusCode.OK.getCode()) {
                return false;
            } else {
                Log.i(TAG_NAME, "200 - New game started for user " + User.getInstance().getUsername() + " and game " + User.getInstance().getSelectedGame().getName());
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public class LoadAsync extends AsyncTask<String, Void, JSONResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StartGame.this);
            pDialog.setMessage("Va rugam sa asteptati...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected JSONResponse doInBackground(String... params) {
            try {
                return response = RestService.getUserInformation(User.getInstance().getUsername());
            } catch (JSONException | IOException e) {
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
            return null;
        }

        protected void onPostExecute(JSONResponse response) {
            if ((pDialog != null) && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            Factory.createUser(response);
        }
    }
}
