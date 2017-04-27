package com.sport.infoquest.view.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.sport.infoquest.R;
import com.sport.infoquest.adapter.CustomAdapter;
import com.sport.infoquest.entity.Game;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.JSONResponse;
import com.sport.infoquest.util.RestService;
import com.sport.infoquest.util.StatusCode;
import com.sport.infoquest.util.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.sport.infoquest.enums.Drawer.SCAN_QR;

/**
 * Created by Ionut on 14/03/2017.
 */

public class CurrentGameFragment extends Fragment {

    private static final String FRAGMENT = "CurrenttGameFragment";
    private boolean showDialog = true;
    private User user = User.getInstance();
    ArrayList<Game> dataModels;
    JSONResponse response;
    private CustomAdapter adapter;
    private ListView listView;
    private View rootView;

    public CurrentGameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //rootView = inflater.inflate(R.layout.fragment_new_game, container, false);
        dataModels = new ArrayList<>();

        rootView = inflater.inflate(R.layout.xx_fragment_home, container, false);
        Game selectedGame = User.getInstance().getCurrentGame();
        if (selectedGame != null) {
            User.getInstance().setMarked(new ArrayList<>(Arrays.asList(User.getInstance().getScannedQuestions().split(","))));
            FragmentManager manager = getFragmentManager();
            Fragment fragment = manager.findFragmentByTag(SCAN_QR.getName());
            if (fragment != null) {
                Utils.replaceFragment(fragment, SCAN_QR.getName(), getFragmentManager());
            } else {
                Toast.makeText(getContext(), "Nu aveti un joc in desfasurare!", Toast.LENGTH_LONG);
            }
        }

        return rootView;
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