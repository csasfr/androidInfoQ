package com.sport.infoquest.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sport.infoquest.R;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.Factory;
import com.sport.infoquest.util.JSONResponse;
import com.sport.infoquest.util.RestService;
import com.sport.infoquest.util.Utils;
import com.sport.infoquest.view.activities.HomeActivity;

import org.json.JSONException;

import java.io.IOException;

public class LoginActivity extends Activity {
    JSONResponse response;
    private String username, password;
    private EditText usernameEditText, passwordEditText;
    public ProgressDialog pDialog;
    boolean doubleBackToExitPressedOnce = false;
    boolean isEnabledLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_redesign);
        Button login_button = (Button) findViewById(R.id.btnLogin);
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            login_button.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEnabledLogin) {
                        username = "csasfr@yahoo.com";//usernameEditText.getText().toString().trim();
                        password = "111111";//passwordEditText.getText().toString().trim();
                        if (Utils.isEntryValid(username, password)) {
                            new LoadAsync().execute(username, password);
                        } else {
                            Utils.showMessage(getApplicationContext(), "Introduceti credentialele !");
                        }
                    } else {
                        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }));
        } else {
            Utils.showMessage(getApplicationContext(), "Aplicatia nu poate fi rulata");
        }

    }

    public class LoadAsync extends AsyncTask<String, String, JSONResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Se initializeaza autentificarea...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected JSONResponse doInBackground(String... params) {
            try {
                return response = RestService.getValidation(params[0], params[1]);
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
            if (Utils.checkStatusResponse(response)) {
                try {
                    Factory.createUser(RestService.getUserInformation(username));
                    Factory.createGame(RestService.getAllGames());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                finish();
            } else {
                Utils.showMessage(getApplicationContext(), "Credentiale Invalide!");
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Apasati inca o data pentru a iesi din aplicatie", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
