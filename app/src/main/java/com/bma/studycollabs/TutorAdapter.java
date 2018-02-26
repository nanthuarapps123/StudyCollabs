package com.bma.studycollabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;


/**
 * Created by NANTHA on 11/1/2017.
 */

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.MyViewHolder> {

    private ArrayList<Tutor> tutorList;
    private Context context;
    private String str_tok;
    private String str_uname;

    public TutorAdapter(Context context, ArrayList<Tutor> tutorList,String str_tok,String str_uname){
        this.context = context;
        this.tutorList = tutorList;
        this.str_tok = str_tok;
        this.str_uname = str_uname;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_find_tu, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.name.setText(tutorList.get(position).getuser_id());
        holder.type.setText(tutorList.get(position).getttype());
        holder.sub.setText(tutorList.get(position).getcat());


        RequestOptions myOptions = new RequestOptions()
                .fitCenter()
                .override(100, 100);

        Glide.with(context)
                .load(tutorList.get(position).getimgurl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.img);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp_uname = holder.name.getText().toString();
                Intent i = new Intent(context,Calenderuser.class);
                Bundle bu_taap_tok = new Bundle();
                bu_taap_tok.putString("str_tok",str_tok);
                bu_taap_tok.putString("str_uname",temp_uname);
                i.putExtras(bu_taap_tok);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView img;
        public TextView name,type,sub;
        public RelativeLayout container;

        public MyViewHolder(View itemView) {
            super(itemView);
            container = (RelativeLayout) itemView.findViewById(R.id.item_container);
            img = (ImageView) itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.name);
            type = (TextView) itemView.findViewById(R.id.type);
            sub = (TextView) itemView.findViewById(R.id.subject);

        }
    }
}
