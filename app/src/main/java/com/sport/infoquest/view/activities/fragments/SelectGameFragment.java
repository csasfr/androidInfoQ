package com.sport.infoquest.view.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sport.infoquest.R;
import com.sport.infoquest.activity.BaseFragment;
import com.sport.infoquest.adapter.CustomAdapter;
import com.sport.infoquest.entity.Game;
import com.sport.infoquest.entity.ListOfGames;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.Factory;
import com.sport.infoquest.util.RestService;
import com.sport.infoquest.util.Utils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static com.sport.infoquest.enums.Drawer.SCAN_QR;
import static com.sport.infoquest.enums.Drawer.START_GAME;

/**
 * Created by Ionut on 14/03/2017.
 */

public class SelectGameFragment extends BaseFragment {

    private static final String TAG = "SelectGameFragment";
    private boolean showDialog = true;
    private User user = User.getInstance();
    ArrayList<Game> dataModels;
    private CustomAdapter adapter;
    private ListView listView;
    private View rootView;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private Query queryRef;
    private Map<String, String> currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.fragment_select_game, container, false);
        showProgressDialog();
        dataModels = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("games").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                            Log.d(TAG, "" + dataSnapshot.getChildrenCount());

                            GenericTypeIndicator<ArrayList<Game>> genericType = new GenericTypeIndicator<ArrayList<Game>>() {};
                            ArrayList<Game> gamesList = dataSnapshot.getValue(genericType);
                            User.getInstance().setGameList(gamesList);
                            // addListenerForGame();
                            updateUI(gamesList);
                            Log.d(TAG, "" + gamesList.size());

                            stopProgressDialog();
                        } else {
                            stopProgressDialog();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                        stopProgressDialog();
                    }
                });

        return rootView;
    }

    private void updateUI(final ArrayList<Game> dataModels) {

        if (ListOfGames.getInstance().getGames() == null) {

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

                    processSelectedGame(selectedGame);
                }

                private void processSelectedGame(final Game selectedGame) {
                    showProgressDialog();
                    databaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    currentUser = (Map) dataSnapshot.getValue();
                                    boolean isCurrentGame = false;
                                    for (Map.Entry<String, String> entry:currentUser.entrySet()){
                                        if (entry.getKey().contains("currentTrack")){
                                            if(entry.getValue().equals(selectedGame.getName())){
                                                isCurrentGame = true;
                                            }
                                        }
                                    }
                                    if (isCurrentGame){
                                        User.getInstance().setCurrentTrack(selectedGame.getName());
                                        User.getInstance().setSelectedGame(selectedGame);
                                        Fragment fragment = new ScanQRFragment();
                                        Utils.addFragment(fragment, SCAN_QR.getName(), getFragmentManager());
                                    } else {
                                        User.getInstance().setCurrentTrack(selectedGame.getName());
                                        User.getInstance().setSelectedGame(selectedGame);
                                        Fragment fragment = new StartGameFragment();
                                        Utils.addFragment(fragment, START_GAME.getName(), getFragmentManager());
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
        }
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

    public void addListenerForGame(){
        databaseReference = FirebaseDatabase.getInstance().getReference("games");

        queryRef = databaseReference.orderByKey();
        childEventListener = new ChildEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                Game newGame = snapshot.getValue(Game.class);
                User.getInstance().getGameList().add(newGame);
                updateUI(User.getInstance().getGameList());
                Log.d(TAG, "New game added");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //  ChatPost newPost = dataSnapshot.getValue(ChatPost.class);
                //   ChatPost topic = new ChatPost(newPost.id, newPost.topic);
                //  addChatTopic(topic);
                //   System.out.println("CHANGED Child Topic: " + newPost.topic);

                //   chatAdapter.updateItem(newPost);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // ChatPost newPost = dataSnapshot.getValue(ChatPost.class);
                // ChatPost topic = new ChatPost(newPost.id, newPost.topic);
                //  chatTopicsList.add(topic);
                //  System.out.println("REMOVED Child Topic: " + newPost.topic);

                //  chatAdapter.removeItem(newPost);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        queryRef.addChildEventListener(childEventListener);
    }

    public void removeListenerForGame(){
        if (childEventListener != null) {
            queryRef.removeEventListener(childEventListener);
        }
    }



}