package com.bma.studycollabs;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by NANTHA on 10/11/2017.
 */

import com.bma.studycollabs.R;
import com.bma.studycollabs.AppData;
import com.bma.studycollabs.Comments;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Comments> commentitem;
    ImageLoader imageLoader = AppData.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Comments> movieItems) {
        this.activity = activity;
        this.commentitem = movieItems;
    }

    @Override
    public int getCount() {
        return commentitem.size();
    }

    @Override
    public Object getItem(int location) {
        return commentitem.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_comments, null);

        if (imageLoader == null)
            imageLoader = AppData.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView name = (TextView) convertView.findViewById(R.id.text_name);
        TextView commet = (TextView) convertView.findViewById(R.id.text_comment);
        TextView call = (TextView) convertView.findViewById(R.id.text_makecall);


        // getting movie data for the row
        Comments c = commentitem.get(position);
        thumbNail.setImageUrl(c.getThumbnailUrl(), imageLoader);
        name.setText(c.getName());
        commet.setText(c.getcomment());
        return convertView;
    }

}