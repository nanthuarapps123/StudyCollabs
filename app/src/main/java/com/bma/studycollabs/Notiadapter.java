package com.bma.studycollabs;

/**
 * Created by NANTHA on 11/20/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by NANTHA on 11/20/2017.
 */

public class Notiadapter extends RecyclerView.Adapter<Notiadapter.MyViewHolder> {

    private ArrayList<Noti> res_list;
    private Context context;
    String str_tok;

    public Notiadapter(Context applicationContext, ArrayList<Noti> sche_lis, String str_token){
        this.context = applicationContext;
        this.res_list = sche_lis;
        this.str_tok = str_token;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_res, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final Notiadapter.MyViewHolder holder, int position) {
        holder.txt_name.setText(res_list.get(position).getUserid());
        holder.txt_link.setText(res_list.get(position).getTitle());
        holder.txt_des.setText(res_list.get(position).getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in_my_cal = new Intent(context,MyCalender.class);
                Bundle bu_my_cal = new Bundle();
                bu_my_cal.putString("toksept",str_tok);
                in_my_cal.putExtras(bu_my_cal);
                in_my_cal.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in_my_cal);
            }
        });

    }

    @Override
    public int getItemCount() {
        return res_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout sche_container;
        TextView txt_name,txt_link,txt_des;


        public MyViewHolder(final View itemView) {
            super(itemView);
            sche_container = (RelativeLayout) itemView.findViewById(R.id.comm_container);
            txt_name = (TextView)itemView.findViewById(R.id.name);
            txt_link = (TextView)itemView.findViewById(R.id.link);
            txt_des = (TextView)itemView.findViewById(R.id.des);



        }
    }
}

