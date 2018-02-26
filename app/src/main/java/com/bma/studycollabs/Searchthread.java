package com.bma.studycollabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bma.boommenu.BoomButtons.OnBMClickListener;
import com.bma.boommenu.BoomMenuButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Searchthread extends AppCompatActivity {
    ListView lv_all_ques;
    ArrayAdapter<String> stringArrayAdapteraq;
    ArrayList<String> stringArrayListaq;
    ArrayList<HashMap<String, String>> all_ques_list;
    String jsonurl;
    ListAdapter adapter;
    ArrayList<HashMap<String, String>> post_List;
    String ques_id;
    HashMap<String, String> contact;
    EditText ed_search;
    String str_r_pt_tok;




    private static String TAG = Searchthread.class.getSimpleName();
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchthread);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle bu_po_thr = getIntent().getExtras();
        str_r_pt_tok = bu_po_thr.getString("toksept");


        Log.d("Seaerch_thread_token",str_r_pt_tok);

        ed_search = (EditText)findViewById(R.id.ed_overall_search);
        lv_all_ques = (ListView)findViewById(R.id.list_all_ques);
        //jsonurl = "http://studycollab.com/mobile/api/index.php/Dashboard/searchQuestion";
        lv_all_ques=(ListView)findViewById(R.id.list_all_ques);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        post_List = new ArrayList<>();
        makejsonarray1();
        lv_all_ques.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String s_id = ((TextView) view.findViewById(R.id.txt_id)).getText().toString();
                Toast.makeText(Searchthread.this, s_id, Toast.LENGTH_SHORT).show();

                Bundle b_searchth = new Bundle();
                b_searchth.putString("sid", s_id);
                b_searchth.putString("token", str_r_pt_tok);
                Intent in = new Intent(getApplicationContext(), ViewQuestion.class);
                in.putExtras(b_searchth);
                startActivity(in);
            }
        });



        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((SimpleAdapter)Searchthread.this.adapter).getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void makejsonarray1() {
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constant.SEARCHQUESTION, new Response.Listener<String>() {



            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    // Log.d("RERssssERR",": " + jsonArray.length());
                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String pdate = jsonObject.getString("postedDate");
                        String title = jsonObject.getString("title");
                        String des = jsonObject.getString("description");
                        String catid = jsonObject.getString("categoryId");
                        String subcatid = jsonObject.getString("subcategoryId");

                        JSONObject catobj = jsonObject.getJSONObject("category");

                        String cid = catobj.getString("id");
                        String cname = catobj.getString("name");

                        JSONObject subcatobj = jsonObject.getJSONObject("subCategory");

                        String scatid = subcatobj.getString("id");
                        String scatname = subcatobj.getString("name");

                        ques_id = id;

                        contact = new HashMap<>();
                        contact.put("id",id);
                        contact.put("pdate",pdate);
                        contact.put("title",title);
                        contact.put("des",des);
                        contact.put("cname", cname);
                        contact.put("scatname", scatname);
                        post_List.add(contact);

                        Log.d("RERssssERR",": " + id);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("RERERR","Response: " + response);
                adapter = new SimpleAdapter(Searchthread.this, post_List, R.layout.list_post, new String[]{"pdate", "title", "des", "cname", "scatname", "id"}, new int[]{R.id.tv_date, R.id.tv_title, R.id.tv_descrip, R.id.tv_cat, R.id.tv_subcat, R.id.txt_id});
                lv_all_ques.setAdapter( adapter);
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
                headers.put("Authorization", str_r_pt_tok);
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Log.d("CAasasLLED","CALLED");
                Map<String, String> params = new HashMap<String, String>();
                params.put("searchTerm", "");
                params.put("latestQuestions","true");
                return params;
            }
        };
        // Adding request to request queue
        AppData.getInstance().addToRequestQueue(strReq);

    }
}
