package com.bma.studycollabs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by NANTHA on 11/20/2017.
 */

public class Resadater extends RecyclerView.Adapter<Resadater.MyViewHolder> {

    private ArrayList<Reso> res_list;
    private Context context;
    String str_tok;

    public Resadater(Context applicationContext, ArrayList<Reso> sche_lis, String str_token){
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
    public void onBindViewHolder(Resadater.MyViewHolder holder, int position) {
        holder.txt_name.setText(res_list.get(position).getname());
        holder.txt_link.setText(res_list.get(position).getlink());
        holder.txt_des.setText(res_list.get(position).getdes());
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
