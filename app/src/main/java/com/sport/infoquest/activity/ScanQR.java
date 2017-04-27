package com.sport.infoquest.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sport.infoquest.R;
import com.sport.infoquest.adapter.CustomAdapter;
import com.sport.infoquest.adapter.QRListAdapter;
import com.sport.infoquest.entity.Game;
import com.sport.infoquest.entity.IdHint;
import com.sport.infoquest.entity.ListOfGames;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.Factory;
import com.sport.infoquest.util.JSONResponse;
import com.sport.infoquest.util.RestService;
import com.sport.infoquest.util.StatusCode;
import com.sport.infoquest.util.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.sport.infoquest.R.id.qrButton;
import static com.sport.infoquest.util.GameName.EDENLAND;

public class ScanQR extends Activity {
    public static QRListAdapter adapter;
    public ProgressDialog pDialog;
    List<String> dataModels;
    List<IdHint> dataIdHint;
    HashMap<String, String> idHintMap;
    ListView listView;
    JSONResponse response;
    private static final String DATA_MODEL_KEY = "dataModel";
    private static final String RESUME_KEY = "resume";
    private static final String START_KEY = "start";
    private static final String CREATE_KEY = "create";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Game currentGame = User.getInstance().getCurrentGame();
        setContentView(R.layout.activity_scan_qr_redesigned);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //idHintMap = currentGame.getHashMap();
//        dataModels = Arrays.asList(currentGame.getHint().split(";"));
        Intent intent = getIntent();
        final String questionId = (String) intent.getSerializableExtra("questionId");
        String isFinish = (String) intent.getSerializableExtra("finishGame");
        listView = (ListView) findViewById(R.id.hintList);
        Button qrButton = (Button) findViewById(R.id.qrButton);
        Button checkScore = (Button) findViewById(R.id.checkScore);
        Button exitButton = (Button) findViewById(R.id.exitGame);

        if (savedInstanceState != null) {
           // dataIdHint = savedInstanceState.getParcelableArrayList(DATA_MODEL_KEY);
           // adapter = new QRListAdapter(dataIdHint, getApplicationContext(), User.getInstance().getMarked());
        } else {
            dataIdHint = currentGame.getIdHintList();
            if (questionId != null && questionId != "") {
                if (User.getInstance().getMarked() != null) {
                    User.getInstance().getMarked().add(questionId);
                }
            }
            adapter = new QRListAdapter(dataIdHint, getApplicationContext(), User.getInstance().getMarked());
        }
        listView.setAdapter(adapter);

        qrButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), QR.class);
                startActivity(i);
                //finish();
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScanQR.this);
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

                Toast.makeText(getApplicationContext(), "Scor total: " + User.getInstance().getCurrentScore(), Toast.LENGTH_LONG).show();
            }
        });
        if (isFinish != null && isFinish != "") {
            if (isFinish.equals("true")) {
                Toast.makeText(getApplicationContext(), "Felicitari! Ai terminat jocul cu scorul x.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
      //  ArrayList<IdHint> dataModelArrayList = new ArrayList<>(dataIdHint);
      //  savedInstanceState.putParcelableArrayList(DATA_MODEL_KEY, dataModelArrayList);
    }


    public class LoadAsync extends AsyncTask<String, Void, JSONResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScanQR.this);
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
            finish();

        }
    }

    public class CheckScoreAsync extends AsyncTask<String, Void, JSONResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScanQR.this);
            pDialog.setMessage("Se verifica scorul...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected JSONResponse doInBackground(String... params) {
            try {
                return RestService.getUserInformation(User.getInstance().getUsername());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
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



