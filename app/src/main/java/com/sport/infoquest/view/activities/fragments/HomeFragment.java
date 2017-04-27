package com.sport.infoquest.view.activities.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sport.infoquest.R;
import com.sport.infoquest.view.activities.listeners.OnBackPressListener;

import static com.sport.infoquest.enums.Drawer.HOME;

/**
 * Created by Ionut on 14/03/2017.
 */

public class HomeFragment extends Fragment {

    private static final String FRAGMENT = "HomeFragment";

    private View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.frame_main, container, false);
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
}
