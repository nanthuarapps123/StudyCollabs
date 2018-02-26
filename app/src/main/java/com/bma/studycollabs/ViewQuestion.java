package com.bma.studycollabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewQuestion extends AppCompatActivity {
    String str_q_no;
    TextView txt_date,txt_cat,txt_arrow,txt_scat,txt_tit,txt_des;
    Button btn_post_comm;
    EditText ed_post_commet;
    String q_id;
    String str_token,str_comm;
    private ProgressDialog pDialog;
    private ListView lv_comments;
    ArrayList<HashMap<String, String>> contactList;
    private List<Comments> commentlist = new ArrayList<Comments>();
    private CustomListAdapter adapter;
    String str_post_id;
    ArrayList<String> vq_name,vq_imagpath,vq_comment,vq_cmd_id,vq_rating_show,vq_disabled,vq_rating;
    ArrayList<String> recom_name,recom_postedDate,recom_userid,recom_imagath,recom_comment,recom_comid,recom_rating,recom_ratingshow,recom_ratingdisabled;
    String str_rurl;
    String str_comm_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent in = getIntent();
        Bundle b_searchth = in.getExtras();
        q_id = b_searchth.getString("sid");
        str_token = b_searchth.getString("token");

        Log.d("SSsID",q_id);
        Log.d("SSsID",str_token);

        //str_rurl = "http://studycollab.com/api/v1/index.php/Dashboard/Viewquestion";

        txt_date = (TextView)findViewById(R.id.vq_txt_date);
        txt_cat = (TextView)findViewById(R.id.vq_txt_cat);
        txt_arrow = (TextView)findViewById(R.id.arrow);
        txt_scat = (TextView)findViewById(R.id.vq_txt_subcat);
        txt_tit = (TextView)findViewById(R.id.vq_txt_tit);
        txt_des = (TextView)findViewById(R.id.vq_txt_des);

        ed_post_commet = (EditText)findViewById(R.id.ed_comment);

        btn_post_comm = (Button)findViewById(R.id.btn_post_comm);

        vq_name = new ArrayList<String>();
        vq_imagpath = new ArrayList<String>();
        vq_comment = new ArrayList<String>();
        vq_cmd_id = new ArrayList<String>();
        vq_rating_show = new ArrayList<String>();
        vq_disabled = new ArrayList<String>();
        vq_rating = new ArrayList<String>();

        recom_name = new ArrayList<String>();
        recom_postedDate = new ArrayList<String>();
        recom_userid = new ArrayList<String>();
        recom_imagath = new ArrayList<String>();
        recom_comment = new ArrayList<String>();
        recom_comid = new ArrayList<String>();
        recom_rating = new ArrayList<String>();
        recom_ratingshow = new ArrayList<String>();
        recom_ratingdisabled = new ArrayList<String>();




//        addAddressValues();

        if (new CheckNetwork(ViewQuestion.this).isNetworkAvailable()) {
            new Viewquest(q_id,str_token).execute();
        }else {
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }



        btn_post_comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_post_commet.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Put your comment first",Toast.LENGTH_SHORT).show();
                }else {
                    str_comm = ed_post_commet.getText().toString();
//                    new Postcommet(str_comm,q_id,str_token).execute();
                    postcomm(str_comm);
                }

            }
        });
    }

    private void postcomm(final String str_comment) {
//            String str_comment = str_comm;

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
                                     Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                     JSONObject commentobj = jsonObject.getJSONObject("comments");
                                     String id = commentobj.getString("id");
                                     String postedDate = commentobj.getString("postedDate");
                                     String comment = commentobj.getString("comment");
                                     String user_Id = commentobj.getString("user_Id");
                                     String rating = commentobj.getString("rating");

                                     JSONObject userobj = commentobj.getJSONObject("user");
                                     String userid = userobj.getString("id");
                                     String user_id = userobj.getString("user_id");
                                     String name = userobj.getString("name");
                                     String email = userobj.getString("email");
                                     String mobile = userobj.getString("mobile");
                                     String phone = userobj.getString("phone");
                                     String image = userobj.getString("image");

                                     String ratingShow = commentobj.getString("ratingShow");
                                     String ratingDisabled = commentobj.getString("ratingDisabled");

                                     str_comm_id = id;
                                     vq_cmd_id.add(id);
                                     vq_comment.add(comment);
                                     vq_name.add(name);
                                     vq_imagpath.add(Constant.root+image);
                                     vq_rating.add(rating);
                                     vq_disabled.add(ratingDisabled);
                                     vq_rating_show.add(ratingShow);
                                     initViews(str_comm_id);
                                     ed_post_commet.setText("");
                                 }else {
                                     Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                message = "Please check your username / Password";
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            } else if (error instanceof AuthFailureError) {
                                message = "The server could not be found. Please try again after some time!!";
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                message = "Parsing error! Please try again after some time!!";
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NoConnectionError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            } else if (error instanceof TimeoutError) {
                                message = "Connection TimeOut! Please check your internet connection.";
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String> ();
                    params.put("questionID", q_id);
                    params.put("comment", str_comment);
                    params.put("parent_comment_id", "");
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Log.d("HEDERD","CALEED");
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", str_token);
                    return headers;
                }
            };

            AppData.getInstance().addToRequestQueue(putRequest);

    }


    private void initViews(String str_comm_id){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_comments);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        ArrayList<Comments> sche_lis = prepareData1();
        Commentadapter commentadapter = new Commentadapter(getApplicationContext(),sche_lis,str_token,str_post_id,q_id,str_comm_id);
        recyclerView.setAdapter(commentadapter);
    }

    private ArrayList<Comments> prepareData1(){
        ArrayList<Comments> comm_list = new ArrayList<>();
        for(int i=0;i<vq_comment.size();i++){
            Comments comments = new Comments();
            comments.setname(vq_name.get(i));
            comments.setThumbnailUrl(vq_imagpath.get(i));
            comments.setcomment(vq_comment.get(i));
            comments.setpostid(str_post_id);
            comments.setcommetid(vq_cmd_id.get(i));
            comments.setrating(vq_rating.get(i));
            comments.setratingshow(vq_rating_show.get(i));
            comments.setratingdisabled(vq_disabled.get(i));
            comm_list.add(comments);
        }
        return comm_list;
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
            subcomm_list.add(subComments);
        }
        recom_name.clear();
        recom_postedDate.clear();
        recom_userid.clear();
        recom_imagath.clear();
        recom_comment.clear();
        recom_comid.clear();
        recom_rating.clear();
        recom_ratingshow.clear();
        recom_ratingdisabled.clear();
        return subcomm_list;

    }



    public class Viewquest extends AsyncTask<String, Void, String> {
        String str_rec_comm, str_rec_qid, str_rec_token;


        public Viewquest(String q_id, String str_token) {
            str_rec_qid = q_id;
            str_rec_token = str_token;
            Log.d("STR_REC_QID", str_rec_qid);
            Log.d("STR_REC_TOKEN", str_rec_token);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewQuestion.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("questionID", str_rec_qid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestBody = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(Constant.VIEWQUESTION, requestBody, str_rec_token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("AFTERRESONSqes", s);

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

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



                    JSONObject commobj = commarray.getJSONObject(i);

                    String commid = commobj.getString("id");
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

                        recom_name.add(recomusername);
                        recom_postedDate.add(recomosteddate);
                        recom_userid.add(recomuserid);
                        recom_imagath.add(Constant.root+recomuserimag);
                        recom_comment.add(recomcomment);
                        recom_comid.add(recomid);
                        recom_rating.add(recomrating);
                        recom_ratingshow.add(recomratingshow);
                        recom_ratingdisabled.add(recomratingdisabled);

                    }

                    str_comm_id = commid;
                    vq_cmd_id.add(commid);
                    vq_comment.add(comm);
                    vq_name.add(ucname);
                    vq_imagpath.add(Constant.root+ucimage);
                    vq_rating.add(commrating);
                    vq_disabled.add(commrdis);
                    vq_rating_show.add(commrshow);
                    initViews(str_comm_id);

                }


                txt_date.setText(postedDate);
                txt_cat.setText(catname);
                txt_scat.setText(scatname);
                txt_tit.setText(title);
                txt_des.setText(description);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
