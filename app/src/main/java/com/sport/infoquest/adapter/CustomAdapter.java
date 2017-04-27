package com.sport.infoquest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sport.infoquest.R;
import com.sport.infoquest.entity.Game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ionut on 03/03/2017.
 */

public class CustomAdapter extends ArrayAdapter<Game> implements View.OnClickListener, Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Game> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder implements Serializable {
        private static final long serialVersionUID = 1L;
        TextView txtName;
    }

    public CustomAdapter(ArrayList<Game> data, Context context) {
        super(context, R.layout.row_item_list, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Game dataModel = (Game) object;

        switch (v.getId()) {
            case R.id.item_info:
                Toast.makeText(getContext(), "Works !", Toast.LENGTH_LONG);
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Game dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_list, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.item_name);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.layout.up_from_botton : R.layout.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(dataModel.getName());
        // viewHolder.info.setOnClickListener(this);
        //viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}