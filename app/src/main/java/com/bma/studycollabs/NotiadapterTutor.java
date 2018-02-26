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

/**
 * Created by NANTHA on 11/20/2017.
 */

public class NotiadapterTutor extends RecyclerView.Adapter<NotiadapterTutor.MyViewHolder> {

    private ArrayList<NotiTutor> res_list;
    private Context context;
    String str_tok;

    public NotiadapterTutor(Context applicationContext, ArrayList<NotiTutor> sche_lis, String str_token){
        this.context = applicationContext;
        this.res_list = sche_lis;
        this.str_tok = str_token;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noti_tutor, parent, false);
        return new MyViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(final NotiadapterTutor.MyViewHolder holder, final int position) {

        holder.txt_title.setText(res_list.get(position).getTitle());
        holder.txt_date.setText(res_list.get(position).getDate());
        holder.txt_cat.setText(res_list.get(position).getcat());
        holder.txt_scat.setText(res_list.get(position).getscat());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = res_list.get(position).getId();
                Bundle b_searchth = new Bundle();
                b_searchth.putString("sid", id);
                b_searchth.putString("token",str_tok);
                Intent in = new Intent(context, ViewQuestion.class);
                in.putExtras(b_searchth);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);

            }
        });

    }

    @Override
    public int getItemCount() {
        return res_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout sche_container;
        TextView txt_title,txt_date,txt_cat,txt_scat;


        public MyViewHolder(final View itemView) {
            super(itemView);
            sche_container = (RelativeLayout) itemView.findViewById(R.id.item_container_noti_tutor);

            txt_title = (TextView)itemView.findViewById(R.id.title);
            txt_date = (TextView)itemView.findViewById(R.id.date);
            txt_cat = (TextView)itemView.findViewById(R.id.cat);
            txt_scat = (TextView)itemView.findViewById(R.id.scat);

        }
    }
}