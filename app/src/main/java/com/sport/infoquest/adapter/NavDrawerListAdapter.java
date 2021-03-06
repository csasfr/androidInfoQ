package com.sport.infoquest.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sport.infoquest.R;
import com.sport.infoquest.entity.NavDrawerItem;

import java.util.ArrayList;


public class NavDrawerListAdapter extends BaseAdapter {

    private int mSelectedItem;
    private String[] sections;
    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getSelectedItem() {
        return mSelectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        mSelectedItem = selectedItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.navdrawer_listitem, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        txtTitle.setText(navDrawerItems.get(position).getTitle());

        if (position == mSelectedItem) {
            txtTitle.setTextColor(convertView.getResources().getColor(R.color.drawer_item_text_selected));
        } else {
            txtTitle.setTextColor(convertView.getResources().getColor(R.color.drawer_item_text));
        }

        return convertView;
    }

}