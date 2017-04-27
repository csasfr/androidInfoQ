package com.sport.infoquest.view.activities.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sport.infoquest.R;
import com.sport.infoquest.activity.ScanQR;
import com.sport.infoquest.activity.StartGame;
import com.sport.infoquest.adapter.CustomAdapter;
import com.sport.infoquest.entity.Game;
import com.sport.infoquest.entity.ListOfGames;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.Factory;
import com.sport.infoquest.util.JSONResponse;
import com.sport.infoquest.util.RestService;
import com.sport.infoquest.util.StatusCode;
import com.sport.infoquest.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.sport.infoquest.enums.Drawer.SCAN_QR;
import static com.sport.infoquest.enums.Drawer.SELECT_GAME;
import static com.sport.infoquest.enums.Drawer.START_GAME;

/**
 * Created by Ionut on 14/03/2017.
 */

public class SelectGameFragment extends Fragment {

    private static final String FRAGMENT = "SelectGameFragment";
    private boolean showDialog = true;
    private User user = User.getInstance();
    ArrayList<Game> dataModels;
    private CustomAdapter adapter;
    private ListView listView;
    private View rootView;

    public SelectGameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_new_game, container, false);
        dataModels = new ArrayList<>();

        if (ListOfGames.getInstance().getGames() == null) {
            try {
                Factory.createGame(RestService.getAllGames());
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        } else {
            for (Game game : ListOfGames.getInstance().getGames().values()) {
                dataModels.add(game);
            }
            adapter = new CustomAdapter(dataModels, getContext());
            setAdapterOnView(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Game selectedGame = dataModels.get(position);
                    if (Utils.isCurrentGame(selectedGame)) {
                        User.getInstance().setSelectedGame(dataModels.get(position));
                        User.getInstance().setCurrentGame(dataModels.get(position));
                        User.getInstance().setMarked(new ArrayList<>(Arrays.asList(User.getInstance().getScannedQuestions().split(","))));

                        Fragment fragment = new ScanQRFragment();
                        Utils.addFragment(fragment, SCAN_QR.getName(), getFragmentManager());
                    } else {
                        User.getInstance().setSelectedGame(dataModels.get(position));
                        Fragment fragment = new StartGameFragment();
                        Utils.addFragment(fragment, START_GAME.getName(), getFragmentManager());
                    }
                }


            });
        }
        return rootView;
    }

    public void setAdapterOnView(CustomAdapter adapter) {
        listView = (ListView) rootView.findViewById(R.id.gameList);
        listView.setDivider(null);
        listView.setAdapter(adapter);
        showDialog = true;

    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putSerializable("SelectGameFragment", adapter);
    }


}