package com.bma.studycollabs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NANTHA on 11/8/2017.
 */

public class Commentadaptersub extends RecyclerView.Adapter<Commentadaptersub.MyViewHolder> {

    private ArrayList<SubComments> comm_list;
    private Context context;
    String str_tok;
    String str_post_id,str_comm_id;
    RecyclerView srecyclerView;

    public Commentadaptersub(Context applicationContext, ArrayList<SubComments> comm_list, String str_tok, String str_post_id) {
        this.context = applicationContext;
        this.comm_list = comm_list;
        this.str_tok = str_tok;
        this.str_post_id = str_post_id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comme_lis_sub, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.txt_name.setText(comm_list.get(position).getName());
        holder.txt_comment.setText(comm_list.get(position).getcomment());
        holder.txt_date.setText(comm_list.get(position).getPosteddate());


        Glide.with(context)
                .load(comm_list.get(position).getThumbnailUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.img);


        if (comm_list.get(position).getratingshow().equals("false")){
            holder.ratingBar.setVisibility(View.GONE);
            Log.d("RATINGGGG",comm_list.get(position).getratingshow());
        }else if (comm_list.get(position).getratingshow().equals("true")){
            holder.ratingBar.setVisibility(View.VISIBLE);
            if (!comm_list.get(position).getrating().equals("null")) {
                holder.ratingBar.setRating(Float.parseFloat(comm_list.get(position).getrating()));
                Log.d("RATINGGGG", comm_list.get(position).getratingshow());
            }
        }

        if (comm_list.get(position).getratingdisabled().equals("true")){
            holder.ratingBar.setIsIndicator(true);
        }else {
            holder.ratingBar.setIsIndicator(false);
        }

        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(context,String.valueOf(rating),Toast.LENGTH_SHORT).show();
                str_comm_id = comm_list.get(position).getCommetid();
                String str_rating = String.valueOf(rating);
                postrating(str_post_id,str_comm_id,str_rating);

                Log.d("OSTTIIDDD",str_post_id);
                Log.d("COMMENTID",str_comm_id);
                Log.d("STRRATINGG",str_rating);

            }
        });

    }

    private void postrating(final String str_post_id, final String str_comm_id, final String str_rating) {
       // final String regUrl = "http://studycollab.com/api/v1/index.php/Dashboard/ratingComment";
        StringRequest putRequest = new StringRequest(Request.Method.PUT, Constant.RATINGCOMMENT,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Responsserating", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equals("success")){
                                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            message = "Please check your username / Password";
                            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("questionID", str_post_id);
                params.put("commentID", str_comm_id);
                params.put("rating", str_rating);
                return params;
            }

            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", str_tok);
                return headers;
            }
        };

        AppData.getInstance().addToRequestQueue(putRequest);

    }

    @Override
    public int getItemCount() {
        return comm_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout sche_container;
        ImageView img;
        TextView txt_name,txt_comment,txt_date;
        private RatingBar ratingBar;

        public MyViewHolder(final View itemView) {
            super(itemView);
            sche_container = (RelativeLayout) itemView.findViewById(R.id.sub_comm_container);

            img = (ImageView)itemView.findViewById(R.id.img) ;
            txt_name = (TextView)itemView.findViewById(R.id.name);
            txt_comment = (TextView)itemView.findViewById(R.id.comment);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rating);
            txt_date = (TextView)itemView.findViewById(R.id.date);

        }
    }
}
