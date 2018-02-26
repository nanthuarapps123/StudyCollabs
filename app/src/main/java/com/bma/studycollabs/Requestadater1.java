package com.bma.studycollabs;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by NANTHA on 11/8/2017.
 */

public class Requestadater1 extends RecyclerView.Adapter<Requestadater1.MyViewHolder> {

    private ArrayList<Requsets> sche_lis;
    private Context context;
    private String id;
    int al_pos;
    String str_tok;


    public Requestadater1(Context applicationContext, ArrayList<Requsets> sche_lis,String str_tok) {
        this.context = applicationContext;
        this.sche_lis = sche_lis;
        this.str_tok = str_tok;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sche_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Log.d("++++++++++++++++","===========================");
        Log.d("++++++++++++++++",sche_lis.get(position).getid());
        holder.txt_name.setText(sche_lis.get(position).getuid());
        holder.txt_title.setText(sche_lis.get(position).gettitle());
        holder.txt_date.setText(sche_lis.get(position).getdate());
        holder.txt_stime.setText(sche_lis.get(position).getstime());
        holder.txt_etime.setText(sche_lis.get(position).getetime());
        holder.txt_atype.setText(sche_lis.get(position).getAtype());
        holder.txt_status.setText(sche_lis.get(position).getStatus());
        holder.txt_location.setText(sche_lis.get(position).getLocation());
        Log.d("++++++++++++++++","===========================");
      if (holder.txt_status.getText().equals("Pending")){
          holder.txt_btn_rej.setVisibility(View.VISIBLE);
          holder.txt_btn_acc.setVisibility(View.VISIBLE);

      }
      else{
          holder.txt_btn_rej.setVisibility(View.GONE);
          holder.txt_btn_acc.setVisibility(View.GONE);
      }

        Log.d("+++++Endofbuttonclick","======endofbuttonclick=======");
    }

    @Override
    public int getItemCount() {
        return sche_lis.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_name,txt_title,txt_date,txt_stime,txt_etime,txt_atype,txt_status,txt_location;
        TextView txt_btn_acc,txt_btn_rej;
        public RelativeLayout sche_container;

        public MyViewHolder(final View itemView) {
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
            txt_btn_acc = (TextView)itemView.findViewById(R.id.btn_accept);
            txt_btn_rej = (TextView)itemView.findViewById(R.id.btn_reject);


            txt_btn_acc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String temp = sche_lis.get(getAdapterPosition()).getid();
                    Log.d("CHECKKKKPOSITIONNN",temp);
                    al_pos = Integer.parseInt(temp);
                    final String sid = String.valueOf(al_pos);

                    if (new CheckNetwork(context).isNetworkAvailable()) {
                        new appaporve(sid,"Accepted").execute();
                    }else {
                        Toast.makeText(context,"Please check your internet connection",Toast.LENGTH_SHORT).show();
                    }


                }
            });

            txt_btn_rej.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String temp = sche_lis.get(getAdapterPosition()).getid();
                    Log.d("CHECKKKKPOSITIONNN",temp);
                    al_pos = Integer.parseInt(temp);
                    String sid = String.valueOf(al_pos);

                    if (new CheckNetwork(context).isNetworkAvailable()) {
                        new appaporve(sid,"Rejected").execute();
                    }else {
                        Toast.makeText(context,"Please check your internet connection",Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }
    }

    public class appaporve extends AsyncTask<String, Void, String> {
        String id,approve;
        //String jsonurl = "http://studycollab.com/api/v1/index.php/Schedule/approveAppointment";

        public appaporve(String id,String approve) {
            this.id = id;
            this.approve = approve;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("id", id);
                    jsonObject.put("status", approve);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestBody = jsonObject.toString();
                Log.d("REQYESTBODY",requestBody);
                return jsonStr = HttpUtils.makeRequest1(Constant.APPROVEAPPOINMENT, requestBody,str_tok );
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "RESULTappcheck------------>" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                if (status.equals("success")){
                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
