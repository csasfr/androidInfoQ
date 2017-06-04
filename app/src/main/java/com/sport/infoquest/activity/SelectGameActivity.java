package com.sport.infoquest.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sport.infoquest.R;
import com.sport.infoquest.adapter.CustomAdapter;
import com.sport.infoquest.entity.Game;
import com.sport.infoquest.entity.ListOfGames;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.Factory;
import com.sport.infoquest.util.JSONResponse;
import com.sport.infoquest.util.RestService;
import com.sport.infoquest.util.Status;
import com.sport.infoquest.util.StatusCode;
import com.sport.infoquest.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Deprecated
public class SelectGameActivity extends Activity {
    public RestService restService;
    public JSONArray allGames;
    ArrayList<Game> dataModels;
    ListView listView;
    JSONResponse response;
    private static CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        dataModels = new ArrayList<>();
        listView = (ListView) findViewById(R.id.gameList);
        if (ListOfGames.getInstance().getGames() == null) {

        } else {
            for (Game game : ListOfGames.getInstance().getGames().values()) {
                dataModels.add(game);
                //this.createButton(game);
            }
            adapter = new CustomAdapter(dataModels, getApplicationContext());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Game selectedGame = dataModels.get(position);
                    if (this.isCurrentGame(selectedGame)) {
                        User.getInstance().setSelectedGame(dataModels.get(position));
                        User.getInstance().setCurrentGame(dataModels.get(position));
                        User.getInstance().setMarked(new ArrayList<String>(Arrays.asList(User.getInstance().getScannedQuestions().split(","))));

                       // Intent i = new Intent(getApplicationContext(), ScanQR.class);
                       // startActivity(i);
                        finish();
                    } else {
                        User.getInstance().setSelectedGame(dataModels.get(position));
                        Intent i = new Intent(getApplicationContext(), StartGame.class);
                        startActivity(i);
                    }
                }

                private boolean isCurrentGame(Game selectedGame) {
                    try {
                        response = RestService.postIsCurrentGame(User.getInstance().getUsername(), selectedGame.getName());
                        if (response.getResponseCode() == StatusCode.OK.getCode()) {
                            return true;
                        } else {
                            return false;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
        }


    }

//    private void createButton(final Game game) {
//        final Button button = new Button(SelectGameActivity.this);
//        button.setText(game.getName());
//        button.setId(Integer.valueOf(game.getId()));
//        LinearLayout ll = (LinearLayout) findViewById(R.id.linearButton);
//        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
//        ll.addView(button, lp);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Bundle localBundle = new Bundle();
//                final User user = User.getInstance();
//                if (user.hasCredit(game) && !user.isOnTrack()) {
//                    if (user.payCredit(game)) {
//                        localBundle.putString("GameName", String.valueOf(game.getName()));
//                        Intent i = new Intent(getApplicationContext(), StartGame.class);
//                        i.putExtras(localBundle);
//                        startActivity(i);
//                    } else {
//                        Utils.showMessage(getApplicationContext(), "A aparut o eroare de conexiune. Va rugam incercati mai tarziu!");
//                    }
//                } else if (!user.hasCredit(game)) {
//                    Utils.showMessage(getApplicationContext(), "Credit insuficient pentru continuarea traseului! Va rugam sa reincarcati creditul!");
//                } else if (user.isOnTrack()) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectGameActivity.this);
//                    builder.setTitle("Avertizare");
//                    builder.setMessage("Sunteti deja in activitatea " + user.getCurrentTrack());
//                    builder.setCancelable(true);
//                    builder.setNegativeButton("Inchide", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            finish();
//                        }
//                    });
//                    builder.setPositiveButton("Confirma", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            if (user.payCredit(game)) {
//                                user.setCurrentTrack(game.getName());
//                                user.setCurrentGame(game);
//                                user.setOnTrack(true);
//                                user.setCredit(String.valueOf(Integer.valueOf(user.getCredit()) - Integer.valueOf("1")));
//                                Intent i = new Intent(getApplicationContext(), StartGame.class);
//                                startActivity(i);
//                            } else {
//                                Utils.showMessage(getApplicationContext(), "A aparut o eroare de conexiune. Va rugam incercati mai tarziu!");
//                            }
//
//
//                        }
//                    });
//                    AlertDialog alert = builder.create();
//                    alert.show();
//                }
//            }
//        });
//    }
}
