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

public class RequestSchedue extends AppCompatActivity {
    String str_tok;
    String req_url;
    ArrayList<String> al_tutor_userid,al_title,al_schdule_date,al_start_time,al_end_time,al_appiont_type,al_status,al_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_schedue);

        Bundle bu_re_sc = getIntent().getExtras();
        str_tok = bu_re_sc.getString("str_tok");

        al_tutor_userid = new ArrayList<String>();
        al_title = new ArrayList<String>();
        al_schdule_date = new ArrayList<String>();
        al_start_time = new ArrayList<String>();
        al_end_time = new ArrayList<String>();
        al_appiont_type = new ArrayList<String>();
        al_status = new ArrayList<String>();
        al_location = new ArrayList<String>();


        //req_url = "http://studycollab.com/api/v1/index.php/Schedule/getRequestSchedule";
        makejsonarray1();
    }

    private void initViews(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_req_sche);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Requsets> sche_lis = prepareData1();
        Requestadater requestadapter = new Requestadater(getApplicationContext(),sche_lis);
        recyclerView.setAdapter(requestadapter);
    }

    private ArrayList<Requsets> prepareData1(){
        ArrayList<Requsets> req_list = new ArrayList<>();
        for(int i=0;i<al_tutor_userid.size();i++){
            Requsets requsets = new Requsets();
            requsets.setuid(al_tutor_userid.get(i));
            requsets.settitle(al_title.get(i));
            requsets.setsdate(al_schdule_date.get(i));
            requsets.setstime(al_start_time.get(i));
            requsets.setetime(al_end_time.get(i));
            requsets.setatype(al_appiont_type.get(i));
            requsets.setstatus(al_status.get(i));
            requsets.setlocation(al_location.get(i));
            Log.d("Preparedatacheck",al_tutor_userid.get(i));
            req_list.add(requsets);
        }
        return req_list;
    }

    private void makejsonarray1() {
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constant.GET_REQ_SCHEDULE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("RERERRreqsch","Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Accepted");
                    for (int i = 0 ;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String tutor_userid = jsonObject1.getString("tutor_userid");
                        String title = jsonObject1.getString("title");
                        String schdule_date = jsonObject1.getString("schdule_date");
                        String start_time = jsonObject1.getString("start_time");
                        String end_time = jsonObject1.getString("end_time");
                        String user_id = jsonObject1.getString("user_id");
                        String appiont_type = jsonObject1.getString("appiont_type");
                        String location = jsonObject1.getString("location");
                        Log.e("LOCATIONCHECKK",location);
                        String status = jsonObject1.getString("status");

                        al_tutor_userid.add(tutor_userid);
                        al_title.add(title);
                        al_schdule_date.add(schdule_date);
                        al_start_time.add(start_time);
                        al_end_time.add(end_time);
                        al_appiont_type.add(appiont_type);
                        al_status.add(status);
                        al_location.add(location);

                        initViews();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

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
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("HEDERD","CALEED");
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", str_tok);
                return headers;
            }

        };
        // Adding request to request queue
        AppData.getInstance().addToRequestQueue(strReq);
    }

}
