package com.sport.infoquest.view.activities;

import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.sport.infoquest.R;
import com.sport.infoquest.activity.BaseActivity;
import com.sport.infoquest.adapter.NavDrawerListAdapter;
import com.sport.infoquest.entity.NavDrawerItem;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.enums.Drawer;
import com.sport.infoquest.util.Factory;
import com.sport.infoquest.util.Utils;
import com.sport.infoquest.view.activities.fragments.CurrentScoreFragment;
import com.sport.infoquest.view.activities.fragments.HomeFragment;
import com.sport.infoquest.view.activities.fragments.ScanQRFragment;
import com.sport.infoquest.view.activities.fragments.SelectGameFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sport.infoquest.enums.Drawer.BUY_COINS;
import static com.sport.infoquest.enums.Drawer.CURRENT_GAME;
import static com.sport.infoquest.enums.Drawer.CURRENT_SCORE;
import static com.sport.infoquest.enums.Drawer.GENERAL_SCORE;
import static com.sport.infoquest.enums.Drawer.HOME;
import static com.sport.infoquest.enums.Drawer.LOGOUT;
import static com.sport.infoquest.enums.Drawer.SCAN_QR;
import static com.sport.infoquest.enums.Drawer.SELECT_GAME;

/**
 * Created by Ionut on 14/03/2017.
 */

public class HomeActivity extends AppCompatActivity {

    private static final String ACTIVITY = "HomeActivity";
    private static final String TAG = "HomeActivity";
    private static final String LOADING_MESSAGE_DIALOG = "loading_message";
    private static final String TITLE_DIALOG = "title_dialog";

    private HomeFragment homeFragment;
    private Toolbar toolbar;
    private NavDrawerListAdapter navDrawerListAdapter;
    private TypedArray navDrawerIcons, navDrawerIconsActive, iconList;
    private ArrayList<NavDrawerItem> navDrawerItems;
    boolean doubleBackToExitPressedOnce = false;

    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private String[] navDrawerTitles;

    private ImageView userPic;
    private TextView userName;
    private Bitmap image;

    private int currentPosition;
    private LinearLayout linearLayout;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private Map<String, String> currentUser;
    public ProgressDialog progressDialog;
    public FirebaseRemoteConfig mFirebaseRemoteConfig;

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        showProgressDialog();
        setContentView(R.layout.activity_menu_new);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser fbUser = mAuth.getCurrentUser();

        databaseReference.child("users").child(fbUser.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentUser = (Map) dataSnapshot.getValue();
                        updateUI(currentUser);
                        stopProgressDialog();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                        stopProgressDialog();
                    }
                });


        int iconId;
        String icId=null;
        if (icId != null) {
            iconId = Integer.valueOf(icId);
        } else {
            iconId = 1;
        }
        iconList = getResources().obtainTypedArray(R.array.icon_list);
        userPic = (ImageView) findViewById(R.id.selection_profile_pic);
        userName = (TextView) findViewById(R.id.userFullName);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bitmap userIcon = BitmapFactory.decodeResource(getResources(), iconList.getResourceId(iconId, -1));
        image = Utils.getRoundedShape(userIcon, 100);
        userPic.setImageBitmap(image);
        userPic.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        navDrawerTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navDrawerIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        navDrawerIconsActive = getResources().obtainTypedArray(R.array.nav_drawer_icons_active);


        navDrawerItems = new ArrayList<>();
        addDrawerItemActive(HOME);
        addDrawerItem(SELECT_GAME);
        addDrawerItem(CURRENT_GAME);
        addDrawerItem(GENERAL_SCORE);
        addDrawerItem(CURRENT_SCORE);
        addDrawerItem(BUY_COINS);
        addDrawerItem(LOGOUT);
        navDrawerIcons.recycle();
        iconList.recycle();
        drawerList = (ListView) findViewById(R.id.drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

           /* Set nav drawer width to be half of the screen */
        int width = getResources().getDisplayMetrics().widthPixels / 2;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) linearLayout.getLayoutParams();
        params.width = width;
        linearLayout.setLayoutParams(params);


        navDrawerListAdapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        drawerList.setAdapter(navDrawerListAdapter);

        drawerList.setOnItemClickListener(new SlideMenuClickListener());

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerToggle.syncState();
        drawerLayout.openDrawer(Gravity.LEFT);
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        drawerList.setItemChecked(currentPosition, true);
                    }
                }
        );
        if (savedInstanceState == null) {
            // withholding the previously created fragment from being created again
            // On orientation change, it will prevent fragment recreation
            // its necessary to reserving the fragment stack inside each tab
            initScreen();

        } else {
            // restoring the previously created fragment
            // and getting the reference
            homeFragment = (HomeFragment) getSupportFragmentManager().getFragments().get(HOME.getPosition());
        }

    }

    private void updateUI(final Map<String, String> currentUser) {

        for (Map.Entry<String, String> entry:currentUser.entrySet()){
            if (entry.getKey().contains("username")){
                userName.setText(entry.getValue());
                return;
            }
        }
    }

    private void initScreen() {
        homeFragment = new HomeFragment();
        addFragment(homeFragment, HOME.getName());
    }

    private void selectItem(int position) {
        currentPosition = position;
        Fragment fragment = null;
        switch (position) {
            case 0:
                popBackStackTo(HOME.getName());
                setMenuPositionAndColor(position);
                break;
            case 1:
                Log.d(TAG, " -> SelectGameFragment is selected ...");
                fragment = new SelectGameFragment();
                addFragment(fragment, SELECT_GAME.getName());
                break;
            case 2:
                if (User.getInstance().getCurrentGame() != null) {
                    fragment = new ScanQRFragment();
                    replaceFragment(fragment, SCAN_QR.getName());
                } else {
                    Toast.makeText(HomeActivity.this, "Nu aveti un joc in desfasurare!", Toast.LENGTH_LONG);
                }
                break;
            case 3: //implement logout from App
                finish();
                break;
            case 4: //implement logout from App
                if (User.getInstance().getCurrentGame() != null) {
                    fragment = new CurrentScoreFragment();
                    replaceFragmentIfExist(fragment, CURRENT_SCORE.getName());
                } else {
                    Toast.makeText(HomeActivity.this, "Nu aveti un joc in desfasurare!", Toast.LENGTH_LONG);
                }
                break;
            case 5:
//                fragment = new Comments();
//                addFragment(fragment, BUY_COINS.getName());
                break;
            case 6: //implement logout from App
                finish();
                break;
        }

        if (fragment != null) {
            setMenuPositionAndColor(position);
        } else {
            Log.i(ACTIVITY, "Fragment was null!");
        }
    }

    private void addFragment(Fragment fragment, String stackName) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, fragment, stackName);
        transaction.addToBackStack(stackName);
        transaction.commit();
    }

    private void replaceFragment(Fragment fragment, String stackName) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    private void replaceFragmentIfExist(Fragment fragment, String stackName) {
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(stackName, 0);

        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.addToBackStack(stackName);
            ft.commit();
        }
    }

    private void popBackStackTo(String fragmentName) {
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack(fragmentName, 0);
    }


    public void setMenuPositionAndColor(int position) {
        navDrawerIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        navDrawerIconsActive = getResources().obtainTypedArray(R.array.nav_drawer_icons_active);
        for (int i = 0; i < navDrawerItems.size(); i++) {
            navDrawerItems.set(i, new NavDrawerItem(navDrawerTitles[i], navDrawerIcons.getResourceId(i, -1)));
        }
        navDrawerItems.set(position, new NavDrawerItem(navDrawerTitles[position], navDrawerIconsActive.getResourceId(position, -1)));
        drawerList.setItemChecked(position, true);
        drawerList.setSelection(position);
        navDrawerListAdapter.setSelectedItem(position);
        setTitle(navDrawerTitles[position]);
        drawerLayout.closeDrawer(linearLayout);

    }

    private void addDrawerItem(Drawer drawer) {
        int position = drawer.getPosition();
        navDrawerItems.add(new NavDrawerItem(navDrawerTitles[position], navDrawerIcons.getResourceId(position, -1)));
    }

    private void addDrawerItemActive(Drawer drawer) {
        int position = drawer.getPosition();
        navDrawerItems.add(new NavDrawerItem(navDrawerTitles[position], navDrawerIconsActive.getResourceId(position, -1)));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPosition);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        HomeFragment myFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HOME.getName());
        if (myFragment != null && myFragment.isVisible()) {
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack(HOME.getName(), 0);
            setMenuPositionAndColor(HOME.getPosition());
            //super.onBackPressed();
        } else {
            // carousel handled the back pressed task
            // do not call super
        }
    }

    public void showProgressDialog(){
        this.initProgressDialog();
        progressDialog.show();
    }

    public void stopProgressDialog(){
        progressDialog.dismiss();
    }

    public void initProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage(mFirebaseRemoteConfig.getString(LOADING_MESSAGE_DIALOG));
        progressDialog.setTitle(mFirebaseRemoteConfig.getString(TITLE_DIALOG));

    }

}