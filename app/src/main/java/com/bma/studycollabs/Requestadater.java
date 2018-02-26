package com.bma.studycollabs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by NANTHA on 11/8/2017.
 */

public class Requestadater extends RecyclerView.Adapter<Requestadater.MyViewHolder> {

    private ArrayList<Requsets> sche_lis;
    private Context context;

    public Requestadater(Context applicationContext, ArrayList<Requsets> sche_lis) {
        this.context = applicationContext;
        this.sche_lis = sche_lis;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sche_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txt_name.setText(sche_lis.get(position).getuid());
        holder.txt_title.setText(sche_lis.get(position).gettitle());
        holder.txt_date.setText(sche_lis.get(position).getdate());
        holder.txt_stime.setText(sche_lis.get(position).getstime());
        holder.txt_etime.setText(sche_lis.get(position).getetime());
        holder.txt_atype.setText(sche_lis.get(position).getAtype());
        holder.txt_status.setText(sche_lis.get(position).getStatus());
        holder.txt_location.setText(sche_lis.get(position).getLocation());



    }

    @Override
    public int getItemCount() {
        return sche_lis.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_name,txt_title,txt_date,txt_stime,txt_etime,txt_atype,txt_status,txt_location;
        public RelativeLayout sche_container;

        public MyViewHolder(View itemView) {
            super(itemView);
            sche_container = (RelativeLayout) itemView.findViewById(R.id.item_sche_container);
            txt_name = (TextView)itemView.findViewById(R.id.name);
            txt_title = (TextView)itemView.findViewById(R.id.title);
            txt_date = (TextView)itemView.findViewById(R.id.date);
            txt_stime = (TextView)itemView.findViewById(R.id.stime);
            txt_etime = (TextView)itemView.findViewById(R.id.etime);
            txt_atype = (TextView)itemView.findViewById(R.id.atype);
            txt_status = (TextView)itemView.findViewById(R.id.status);
            txt_location = (TextView)itemView.findViewById(R.id.location);

        }
    }

}
