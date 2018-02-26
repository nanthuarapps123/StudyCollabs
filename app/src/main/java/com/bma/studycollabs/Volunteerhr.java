package com.bma.studycollabs;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Volunteerhr extends AppCompatActivity {
    String str_r_pt_tok,jsonurl;
    RatingBar ratingBar;
    TextView txt_vol_hrs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteerhr);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle bu_vol_hrs = getIntent().getExtras();
        str_r_pt_tok = bu_vol_hrs.getString("toksept");

        ratingBar = (RatingBar)findViewById(R.id.user_rating) ;
        txt_vol_hrs = (TextView)findViewById(R.id.txt_vol_hrs);

        //jsonurl = "http://studycollab.com/api/v1/index.php/Dashboard/getRatingVolunteerHours";

        makejsonarray1();

    }

    private void makejsonarray1() {
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constant.VOLUNTEER_HOURS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RATINGVOLHOURS",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String userRating = jsonObject.getString("userRating");
                    String voluteerhrs = jsonObject.getString("volunteerHours");
                    if (userRating.equals("") || voluteerhrs.equals("") || userRating.equals("null") || voluteerhrs.equals("null")){
                        txt_vol_hrs.setText("You don't have volunteer hours");
                        ratingBar.setRating(0);
                    }else {
                        txt_vol_hrs.setText("User Volunteer hours : "+userRating);
                        ratingBar.setRating(Float.parseFloat(voluteerhrs));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("HEDERD","CALEED");
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", str_r_pt_tok);
                return headers;
            }
        };
        // Adding request to request queue
        AppData.getInstance().addToRequestQueue(strReq);
    }
}
