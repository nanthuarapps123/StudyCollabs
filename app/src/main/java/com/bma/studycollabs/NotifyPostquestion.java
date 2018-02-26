package com.bma.studycollabs;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotifyPostquestion extends AppCompatActivity {
    String str_token;
    String regurl;
    ArrayList<String> al_id,al_title,al_date,al_cat,al_scat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_postquestion);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle bu_tutor_alert = getIntent().getExtras();
        str_token = bu_tutor_alert.getString("toksept");

        al_id = new ArrayList<String>();
        al_title = new ArrayList<String>();
        al_cat = new ArrayList<String>();
        al_scat = new ArrayList<String>();
        al_date = new ArrayList<String>();

//        regurl = "http://studycollab.com/mobile/api/index.php/Dashboard/getQuestionNotify";

        getreso();
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_notifi_post);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());//
        recyclerView.setLayoutManager(layoutManager);//
        ArrayList<NotiTutor> noti_li = prepareData();//
        NotiadapterTutor notiadapter = new NotiadapterTutor(getApplicationContext(),noti_li,str_token);
        recyclerView.setAdapter(notiadapter);
    }

    private ArrayList<NotiTutor> prepareData(){
        ArrayList<NotiTutor> noti_list = new ArrayList<>();
        for(int i=0;i<al_title.size();i++){
            NotiTutor noti = new NotiTutor();
            noti.setid(al_id.get(i));
            noti.setTitle(al_title.get(i));
            noti.setcat(al_cat.get(i));
            noti.setscat(al_scat.get(i));
            noti.setDate(al_date.get(i));
            Log.d("Preparedatachecknot",al_title.get(i));
            noti_list.add(noti);
        }
        return noti_list;
    }

    private void getreso() {
        StringRequest putRequest = new StringRequest(Request.Method.GET, Constant.SCHEDULENOTIFICATION,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Responssernotitut", response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);


                            JSONArray jsonArray = jsonObject.getJSONArray("NotificationCount");
                            for (int i=0 ; i<jsonArray.length();i++){
                                JSONObject jsonObjectsche = jsonArray.getJSONObject(i);
                                String id = jsonObjectsche.getString("id");
                                String title = jsonObjectsche.getString("title");
                                String user_id = jsonObjectsche.getString("user_id");
                                String postedDate = jsonObjectsche.getString("postedDate");
                                String category = jsonObjectsche.getString("category");
                                String subCategory = jsonObjectsche.getString("subCategory");

                                al_id.add(id);
                                al_date.add(postedDate);
                                al_title.add(title);
                                al_cat.add(category);
                                al_scat.add(subCategory);
                                initViews();

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
//                        Log.d("Errorr.Responsee", error.toString());
//                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", str_token);
                return headers;
            }
        };

        AppData.getInstance().addToRequestQueue(putRequest);
    }
}
