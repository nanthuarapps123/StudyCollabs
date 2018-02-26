package com.bma.studycollabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by NANTHA on 11/8/2017.
 */


public class Commentadapter extends RecyclerView.Adapter<Commentadapter.MyViewHolder> {
    private ArrayList<Comments> comm_list;

    private Context context;
    String str_tok;
    private String str_post_id,str_comm_id,q_id;
    private ArrayList<String> recom_name,recom_postedDate,recom_userid,recom_imagath,recom_comment,recom_comid,recom_rating,recom_ratingshow,recom_ratingdisabled;
    private ArrayList<SubComments> sub_sche_lis;
    private String str_co_id;


    public Commentadapter(Context applicationContext, ArrayList<Comments> comm_list, String str_tok, String str_post_id,String q_id,String str_comm_id) {

        Log.d("calledcalledconstr","commadnd");
        this.context = applicationContext;
        this.comm_list = comm_list;
        this.str_tok = str_tok;
        this.str_post_id = str_post_id;
        this.q_id = q_id;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comme_lis, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.e("calledcalledonbindhol","commadnd");
        holder.txt_name.setText(comm_list.get(position).getName());
        holder.txt_comment.setText(comm_list.get(position).getcomment());
//        final String comm_id = comm_list.get(position).getCommetid();

        RequestOptions myOptions = new RequestOptions()
                .fitCenter()
                .override(100, 100);

        Glide.with(context)
                .load(comm_list.get(position).getThumbnailUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.img);




        if (comm_list.get(position).getratingshow().equals("false")){
            holder.ratingBar.setVisibility(View.GONE);
        }else if (comm_list.get(position).getratingshow().equals("true")){
            holder.ratingBar.setVisibility(View.VISIBLE);
            if (!comm_list.get(position).getrating().equals("null")) {
                holder.ratingBar.setRating(Float.parseFloat(comm_list.get(position).getrating()));
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

                if (new CheckNetwork(context).isNetworkAvailable()) {
                    postrating(str_post_id,str_comm_id,str_rating);
                }else {
                    Toast.makeText(context,"Please check your internet connection",Toast.LENGTH_SHORT).show();
                }


                            }
        });

        holder.txt_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btn_post_com.setVisibility(View.VISIBLE);
                holder.ed_comm.setVisibility(View.VISIBLE);
                Toast.makeText(context,comm_list.get(position).getCommetid(),Toast.LENGTH_SHORT).show();
                Toast.makeText(context,comm_list.get(position).getcomment(),Toast.LENGTH_SHORT).show();
            }
        });

        holder.btn_post_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = holder.ed_comm.getText().toString();
                String comm_id = comm_list.get(position).getCommetid();
                if (command.equals("")){
                    Toast.makeText(context,"Please enter your comment",Toast.LENGTH_SHORT).show();
                }else {
                    postcomm(command,comm_id,holder);
                }
            }
        });

        Log.e("QUESTIOINID",q_id);
        Log.e("QUESTIOINID",str_tok);
        Log.e("BINDHOLDERCOMMAID",comm_list.get(position).getCommetid());
        str_co_id = comm_list.get(position).getCommetid();

        if (new CheckNetwork(context).isNetworkAvailable()) {
            new Viewquest(q_id,str_tok,str_co_id,holder).execute();
        }else {
            Toast.makeText(context,"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }

        Log.d("VIEWQUESTFINISH","TREU");


    }

    private void postcomm(final String command, final String comm_id, final MyViewHolder holderr) {

        //String regUrl = "http://studycollab.com/api/v1/index.php/Dashboard/addComment";
        StringRequest putRequest = new StringRequest(Request.Method.PUT, Constant.POSTCOMMAND,

                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("postcommandresponse", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equals("success")){
                                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                                JSONObject commentobj = jsonObject.getJSONObject("comments");
                                String id = commentobj.getString("id");
                                String postedDate = commentobj.getString("postedDate");
                                String comment = commentobj.getString("comment");
                                String user_Id = commentobj.getString("user_Id");
                                String rating = commentobj.getString("rating");

                                JSONObject userobj = commentobj.getJSONObject("user");
                                String commuserid = userobj.getString("id");
                                String user_id = userobj.getString("user_id");
                                String name = userobj.getString("name");
                                String email = userobj.getString("email");
                                String mobile = userobj.getString("mobile");
                                String phone = userobj.getString("phone");
                                String image = userobj.getString("image");

                                String ratingShow = commentobj.getString("ratingShow");
                                String ratingDisabled = commentobj.getString("ratingDisabled");

                                recom_name.add(name);
                                recom_postedDate.add(postedDate);
                                recom_userid.add(user_id);
                                recom_imagath.add(Constant.root + image);
                                recom_comment.add(comment);
                                recom_comid.add(id);
                                recom_rating.add(rating);
                                recom_ratingshow.add(ratingShow);
                                recom_ratingdisabled.add(ratingDisabled);


                                sub_sche_lis = prepareData2();
                                holderr.srecyclerView.setHasFixedSize(true);
                                holderr.srecyclerView.setNestedScrollingEnabled(false);
                                holderr.srecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                                Log.e("adaptersubcomm",String.valueOf(sub_sche_lis.size()));
                                Commentadaptersub commentadaptersub = new Commentadaptersub(context, sub_sche_lis,str_tok,str_post_id);
                                holderr.srecyclerView.setAdapter(commentadaptersub);
                                holderr.srecyclerView.setNestedScrollingEnabled(false);
                                holderr.ed_comm.setText("");

                            }else {
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
                        // error
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
                Map<String, String>  params = new HashMap<String, String> ();
                params.put("questionID", q_id);
                params.put("comment", command);
                params.put("parent_comment_id", comm_id);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("HEDERD","CALEED");
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", str_tok);
                return headers;
            }
        };

        AppData.getInstance().addToRequestQueue(putRequest);

    }


    private void postrating(final String str_post_id, final String str_comm_id, final String str_rating) {
        //final String regUrl = "http://studycollab.com/api/v1/index.php/Dashboard/ratingComment";
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

    public class Viewquest extends AsyncTask<String, Void, String> {

        String str_rec_comm, str_rec_qid, str_rec_token,str_comm_id;
        private ProgressDialog pDialog;
        //String str_rurl="http://studycollab.com/mobile/api/index.php/Dashboard/Viewquestion";
        MyViewHolder holder;

        public Viewquest(String q_id, String str_token, String str_commid,MyViewHolder holder) {
            str_rec_qid = q_id;
            str_rec_token = str_token;
            str_comm_id = str_commid;
            this.holder = holder;
            Log.d("STR_REC_QID", str_rec_qid);
            Log.d("STR_REC_TOKEN", str_rec_token);
            Log.e("PREEXECUTEEWORK","TRUEE");
            Log.e("STRCOMMIDDDDDDD",str_commid);

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("PREEXECUTEEWORK","TRUEE");
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                Log.e("DOINBACKGROUNDD","TRUEE");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("questionID", str_rec_qid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestBody = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(Constant.VIEWQUESTION, requestBody, str_rec_token);
            } catch (Exception e) {
                Log.e("InputStreamnmnm", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("POSTEXECUTEWORK","TRUEE");
            Log.e("AFTERRESONSqescommaada", s);

            try {
                JSONObject response = new JSONObject(s);
                String qid = response.getString("id");
                str_post_id = qid;
                String postedDate = response.getString("postedDate");

                JSONObject udobj = response.getJSONObject("user");

                String uid = udobj.getString("id");
                String unameid = udobj.getString("user_id");
                String name = udobj.getString("name");
                String qpuimg = udobj.getString("image");

                String title = response.getString("title");
                String description = response.getString("description");

                JSONObject catobj = response.getJSONObject("category");

                String catid = catobj.getString("id");
                String catname = catobj.getString("name");

                JSONObject scatobj = response.getJSONObject("subCategory");

                String scatid = scatobj.getString("id");
                String scatname = scatobj.getString("name");


                JSONArray commarray = response.getJSONArray("comments");

                for (int i = 0;i<commarray.length();i++) {

                    recom_name.clear();
                    recom_postedDate.clear();
                    recom_userid.clear();
                    recom_imagath.clear();
                    recom_comment.clear();
                    recom_comid.clear();
                    recom_rating.clear();
                    recom_ratingshow.clear();
                    recom_ratingdisabled.clear();

                    JSONObject commobj = commarray.getJSONObject(i);
                    String commid = commobj.getString("id");
                    Log.d("COMMENTIDDD",commid);
                    Log.d("COMMENTIEEE",str_comm_id);
                    String commpostedDate = commobj.getString("postedDate");
                    String comm = commobj.getString("comment");

                    String commuser_Id = commobj.getString("user_Id");
                    String commrating = commobj.getString("rating");

                    JSONObject usercommobj = commobj.getJSONObject("user");

                    String ucid = usercommobj.getString("id");
                    String ucuser_id = usercommobj.getString("user_id");
                    String ucname = usercommobj.getString("name");
                    String ucimage = usercommobj.getString("image");

                    String commrshow = commobj.getString("ratingShow");
                    String commrdis = commobj.getString("ratingDisabled");

//                    JSONArray recommarray = commobj.getJSONArray("recomments");
                              Log.d("COMMENTLOOP","ENTERED");
//                    if (commid.equals(str_comm_id)){
                            Log.d("RECOMMENTLOOP","ENTERED");

                        JSONArray recommarray = commobj.getJSONArray("recomments");
                        for (int k = 0 ; k<recommarray.length();k++){

                            JSONObject recomobj = recommarray.getJSONObject(k);
                            String recomid = recomobj.getString("id");
                            String recomosteddate = recomobj.getString("postedDate");
                            String recomcomment = recomobj.getString("comment");
                            String recomuserid = recomobj.getString("user_Id");
                            String recomrating = recomobj.getString("rating");
                            JSONObject recomuserjsonobj = recomobj.getJSONObject("user");
                            String recomusername = recomuserjsonobj.getString("name");
                            String recomuserimag = recomuserjsonobj.getString("image");


                            String recomratingshow = recomobj.getString("ratingShow");
                            String recomratingdisabled = recomobj.getString("ratingDisabled");


                            Log.d("2COMMENTIDDD",commid);
                            Log.d("2COMMENTIEEE",str_comm_id);

                            if (commid.equals(str_comm_id)) {
                                Log.d("RECOMMENTLOOPLOOP","ENTERED");

                                recom_name.add(recomusername);
                                recom_postedDate.add(recomosteddate);
                                recom_userid.add(recomuserid);
                                recom_imagath.add(Constant.root + recomuserimag);
                                recom_comment.add(recomcomment);
                                recom_comid.add(recomid);
                                recom_rating.add(recomrating);
                                recom_ratingshow.add(recomratingshow);
                                recom_ratingdisabled.add(recomratingdisabled);


                                sub_sche_lis = prepareData2();
                                holder.srecyclerView.setHasFixedSize(true);
                                holder.srecyclerView.setNestedScrollingEnabled(false);
                                holder.srecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                                Log.e("adaptersubcomm",String.valueOf(sub_sche_lis.size()));
                                Commentadaptersub commentadaptersub = new Commentadaptersub(context, sub_sche_lis,str_tok,str_post_id);
                                holder.srecyclerView.setAdapter(commentadaptersub);
                                holder.srecyclerView.setNestedScrollingEnabled(false);


                        }

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public int getItemCount() {
        return comm_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout sche_container;
        ImageView img;
        TextView txt_name,txt_comment,txt_reply;
        private RatingBar ratingBar;
        EditText ed_comm;
        Button btn_post_com;
        public RecyclerView srecyclerView;

        public MyViewHolder(final View itemView) {
            super(itemView);
            Log.e("calledcalledviewholder","commadnd");
            sche_container = (RelativeLayout) itemView.findViewById(R.id.comm_container);
            img = (ImageView)itemView.findViewById(R.id.img) ;
            txt_name = (TextView)itemView.findViewById(R.id.name);
            txt_comment = (TextView)itemView.findViewById(R.id.comment);
            txt_reply = (TextView)itemView.findViewById(R.id.txt_reply);
            ed_comm = (EditText)itemView.findViewById(R.id.ed_comme);
            btn_post_com = (Button)itemView.findViewById(R.id.btn_post_com);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rating);
            srecyclerView = (RecyclerView)itemView.findViewById(R.id.recycler_subcommentsonee);

            recom_name = new ArrayList<String>();
            recom_postedDate = new ArrayList<String>();
            recom_userid = new ArrayList<String>();
            recom_imagath = new ArrayList<String>();
            recom_comment = new ArrayList<String>();
            recom_comid = new ArrayList<String>();
            recom_rating = new ArrayList<String>();
            recom_ratingshow = new ArrayList<String>();
            recom_ratingdisabled = new ArrayList<String>();

        }
    }



    private void initViews() {
         sub_sche_lis = prepareData2();
         Log.e("AFETRESCHLISSIZE",String.valueOf(sub_sche_lis.size()));
    }

    public ArrayList<SubComments> prepareData2(){
        ArrayList<SubComments> subcomm_list = new ArrayList<>();
        subcomm_list.clear();
        for(int i=0;i<recom_comment.size();i++){
            SubComments subComments = new SubComments();
            subComments.setname(recom_name.get(i));
            subComments.setThumbnailUrl(recom_imagath.get(i));
            subComments.setcomment(recom_comment.get(i));
            subComments.setposteddate(recom_postedDate.get(i));
            subComments.setpostid(str_post_id);
            subComments.setcommetid(recom_comid.get(i));
            subComments.setrating(recom_rating.get(i));
            subComments.setratingshow(recom_ratingshow.get(i));
            subComments.setratingdisabled(recom_ratingdisabled.get(i));
            Log.d("Preparedatacheckrecomm",recom_comment.get(i)+""+String.valueOf(i));
            subcomm_list.add(subComments);
            Log.e("SUMCOMMEANDDDSIZE",String.valueOf(subcomm_list.size()));
        }
        return subcomm_list;
    }
}