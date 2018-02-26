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

public class Notification extends AppCompatActivity {

    String regurl;
    String str_token;
    ArrayList<String> al_id,al_title,al_userid,al_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle bu_not_sche = getIntent().getExtras();
        str_token = bu_not_sche.getString("toksept");

        Log.d("======tokenot====",str_token);

        al_id = new ArrayList<String>();
        al_title = new ArrayList<String>();
        al_userid = new ArrayList<String>();
        al_date = new ArrayList<String>();

//        regurl = "http://studycollab.com/mobile/api/index.php/Dashboard/getQuestionNotify";



        getreso();

    }


    private void initViews() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_notification);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Noti> noti_li = prepareData1();
//        Commentadapter commentadapter = new Commentadapter(getApplicationContext(),sche_lis,str_token,str_post_id);
        Notiadapter notiadapter = new Notiadapter(getApplicationContext(),noti_li,str_token);
        recyclerView.setAdapter(notiadapter);
    }

    private ArrayList<Noti> prepareData1(){
        ArrayList<Noti> noti_list = new ArrayList<>();
        for(int i=0;i<al_title.size();i++){
            Noti noti = new Noti();
            noti.setTitle(al_title.get(i));
            noti.setUserid(al_userid.get(i));
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
                        Log.d("Responssernoti", response);
                        try {

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("NotificationCount");
                                for (int i=0 ; i<jsonArray.length();i++){

                                }
                                JSONArray jsonArray1 = jsonObject.getJSONArray("scheduleNotification");
                                for (int j = 0;j<jsonArray1.length();j++){
                                    JSONObject jsonObjectsche = jsonArray1.getJSONObject(j);
                                    String id = jsonObjectsche.getString("id");
                                    String title = jsonObjectsche.getString("title");
                                    String user_id = jsonObjectsche.getString("user_id");
                                    String postedDate = jsonObjectsche.getString("postedDate");

                                    al_id.add(id);
                                    al_title.add(title);
                                    al_userid.add(user_id);
                                    al_date.add(postedDate);
                                    initViews();
                                    Log.d("+++NOTIFICATION++++++","\ttttttt"+id+"\tttttt"+title+"\tttttt"+user_id+"\tttttt"+postedDate);
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
