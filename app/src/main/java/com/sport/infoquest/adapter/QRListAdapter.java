package com.sport.infoquest.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.design.widget.CoordinatorLayout;
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
import com.sport.infoquest.entity.IdHint;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ionut on 03/03/2017.
 */

public class QRListAdapter extends ArrayAdapter<IdHint> implements View.OnClickListener, Serializable {

    private List<IdHint> dataSet;
    private List<String> marked = new ArrayList<>();
    public transient Context mContext;
    CoordinatorLayout coordinatorLayout;


    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        ImageView info;
    }


    public QRListAdapter(List<IdHint> data, Context context, List<String> marked) {
        super(context, R.layout.row_item_qr, data);
        this.dataSet = data;
        this.mContext = context;
        this.marked = marked;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        String dataModel = (String) object;

        switch (v.getId()) {
            case R.id.item_info:
                Utils.showMessage(getContext(), "Mesaj de informare");
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        IdHint dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_qr, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);
            //viewHolder.txtName.setPaintFlags(viewHolder.txtName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.layout.up_from_botton : R.layout.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        if (!marked.isEmpty()) {
            for (String mark : marked) {
                if (dataModel.getId().equals(mark)) {
                    //viewHolder.txtName.setText(dataModel.getHint());
                    viewHolder.txtName.setPaintFlags(viewHolder.txtName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    break;
                }
            }
        }
        viewHolder.txtName.setText(dataModel.getHint());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}