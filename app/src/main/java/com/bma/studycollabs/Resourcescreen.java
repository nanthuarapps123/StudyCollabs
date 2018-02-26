package com.bma.studycollabs;

import android.content.Intent;
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

public class Resourcescreen extends AppCompatActivity {

    String regurl;
    String str_token;
    ArrayList<String> al_id,al_name,al_link,al_des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resourcescreen);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle bu_re_sc = getIntent().getExtras();
        str_token = bu_re_sc.getString("toksept");

        Log.d("======tokenres====",str_token);

        al_id = new ArrayList<String>();
        al_name = new ArrayList<String>();
        al_link = new ArrayList<String>();
        al_des = new ArrayList<String>();

        //regurl = "http://studycollab.com/mobile/api/index.php/Dashboard/resources";
        getreso();

    }


    private void initViews() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_resource);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Reso> sche_lis = prepareData1();
//        Commentadapter commentadapter = new Commentadapter(getApplicationContext(),sche_lis,str_token,str_post_id);
        Resadater resadater = new Resadater(getApplicationContext(),sche_lis,str_token);
        recyclerView.setAdapter(resadater);
    }

    private ArrayList<Reso> prepareData1(){
        ArrayList<Reso> res_list = new ArrayList<>();
        for(int i=0;i<al_name.size();i++){
            Reso reso = new Reso();
            reso.setname(al_name.get(i));
            reso.setlink(al_link.get(i));
            reso.setdes(al_des.get(i));
            Log.d("Preparedatacheckres",al_name.get(i));
            res_list.add(reso);
        }
        return res_list;
    }

    private void getreso() {
        StringRequest putRequest = new StringRequest(Request.Method.GET, Constant.GETRESOURCES,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Responsseresource", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String link = jsonObject.getString("link");
                                String description = jsonObject.getString("description");

                                al_id.add(id);
                                al_name.add(name);
                                al_link.add(link);
                                al_des.add(description);

                                initViews();

                                Log.d("ID+NAME+LINK+DES","\n"+id+"\n"+name+"\n"+link+"\n"+description);
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
