package com.bma.studycollabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindTutor extends AppCompatActivity {
    Spinner spi_sel_toc,spi_sel_cat,spi_sel_sub_cat;
    EditText ed_hint;
    Button bt_go;
    List<String> categories;
    List<String> sub_categories,type_of_user;
    ArrayAdapter<String> toc_dataAdapter,cat_dataadapter,subcat_dataadapter;
    String jsonurl,jsonurl1,jsonurl2;
    ProgressDialog pDialog;
    String str_tot,str_cat,str_scat,str_hint,str_cat_pos;
    String str_r_pt_tok;
    String str_uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tutor);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final Bundle bu_fi_tu = getIntent().getExtras();
        str_r_pt_tok = bu_fi_tu.getString("toksept");
        str_uname = bu_fi_tu.getString("str_uname");

//        jsonurl = "http://studycollab.com/mobile/api/index.php/Dashboard/subjects";
//        jsonurl1 = "http://studycollab.com/mobile/api/index.php/Dashboard/getTutortype";
//        jsonurl2 = "http://studycollab.com/mobile/api/index.php/Dashboard/getSubCategory";

        spi_sel_toc = (Spinner)findViewById(R.id.s_sel_tou);
        spi_sel_cat = (Spinner)findViewById(R.id.s_sel_cat);
        spi_sel_sub_cat = (Spinner)findViewById(R.id.s_sel_sub_cat);
        ed_hint = (EditText)findViewById(R.id.input_hint);
        bt_go = (Button)findViewById(R.id.btn_go);
        categories = new ArrayList<String>();
        sub_categories = new ArrayList<String>();
        type_of_user = new ArrayList<String>();

        gettou();
        getcat();
        str_scat = "";
//        getsubcat();

        spi_sel_toc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                str_tot = parent.getItemAtPosition(position).toString();
                if (parent.getItemAtPosition(position).toString().equals("All")){
                    str_tot = "0";
                }else if (parent.getItemAtPosition(position).toString().equals("Not Interested")){
                    str_tot = "1";
                }else if (parent.getItemAtPosition(position).toString().equals("Teacher")){
                    str_tot = "2";
                }else if (parent.getItemAtPosition(position).toString().equals("Volunteer")){
                    str_tot = "3";
                }else if (parent.getItemAtPosition(position).toString().equals("Student")){
                    str_tot = "4";
                }
                Log.d("TOTUTOR",str_tot);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spi_sel_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if ( parent.getItemAtPosition(position).toString().equals("Select category")){
                    str_cat = "";
                    str_cat_pos = "";
                }else {
                    sub_categories.clear();
                    str_cat = parent.getItemAtPosition(position).toString();
                    str_cat_pos = String.valueOf(position);
                    Log.e("STR_CATT",str_cat);
                    getsubcat(str_cat);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spi_sel_sub_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_scat = parent.getItemAtPosition(position).toString();
                if ( parent.getItemAtPosition(position).toString().equals("Select sub category")){
                    str_scat = "";
                }else {
                    str_scat = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bt_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_hint = ed_hint.getText().toString();
                Intent in_ft_sev = new Intent(getApplicationContext(),FindTuwo.class);
                Bundle bu_ft_sev = new Bundle();
                bu_ft_sev.putString("str_tok",str_r_pt_tok);
                bu_ft_sev.putString("tutype",str_tot);
                bu_ft_sev.putString("tucat",str_cat);
                bu_ft_sev.putString("tuscat",str_scat);
                bu_ft_sev.putString("tuhint",str_hint);
                bu_ft_sev.putString("tuuname",str_uname);
                in_ft_sev.putExtras(bu_ft_sev);
                startActivity(in_ft_sev);
            }
        });
    }




    private void gettou() {
        String tag_json_arry = "json_array_req";
        String url = jsonurl1;
        JsonArrayRequest req = new JsonArrayRequest(Constant.TUTORTYPE,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0;i<response.length();i++){
                            try {
                                JSONObject catobj = response.getJSONObject(i);
                                String name = catobj.getString("name");
                                type_of_user.add(name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        toc_dataAdapter = new ArrayAdapter<String>(FindTutor.this, android.R.layout.simple_spinner_item, type_of_user);
                        toc_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spi_sel_toc.setAdapter(toc_dataAdapter);
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("GDFGDNJFD","CALLED");
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", str_r_pt_tok);
                return headers;
            }
            @Override
            public String getBodyContentType()
            {
                return "application/json; charset=utf-8";
            }
        };
        AppData.getInstance().addToRequestQueue(req, tag_json_arry);
    }

    private void getsubcat(String str_cat_pos) {
        String str_cat_posio;
        str_cat_posio = this.str_cat_pos;

        if (new CheckNetwork(FindTutor.this).isNetworkAvailable()) {
            new Test_url(str_cat_posio).execute();
        }else {
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }

    private void getcat() {
        String tag_json_arry = "json_array_req";
        String url = jsonurl;
        JsonArrayRequest req = new JsonArrayRequest(Constant.SUBJECTS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        categories.add("Select category");
                    for (int i = 0;i<response.length();i++){
                        try {
                            JSONObject catobj = response.getJSONObject(i);
                            String id = catobj.getString("id");
                            String name = catobj.getString("name");
                            Log.e("FIRSTGETCAT",name);
                            categories.add(name);
                            int a = categories.size();
                            Log.e("FIRSTGETCAT",String.valueOf(a));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                        cat_dataadapter = new ArrayAdapter<String>(FindTutor.this, android.R.layout.simple_spinner_item, categories);
                        cat_dataadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spi_sel_cat.setAdapter(cat_dataadapter);
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("GDFGDNJFD","CALLED");
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", str_r_pt_tok);
                return headers;
            }
            @Override
            public String getBodyContentType()
            {
                return "application/json; charset=utf-8";
            }
        };

        AppData.getInstance().addToRequestQueue(req, tag_json_arry);

    }

    public class Test_url extends AsyncTask<String, Void, String> {
        String str_cat_position;

        public Test_url(String str_cat_posio) {
            str_cat_position = str_cat_posio;
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
                    jsonObject.put("categoryId", str_cat_position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String requestBody = jsonObject.toString();
                Log.d("REQYESTBODY",requestBody);
                return jsonStr = HttpUtils.makeRequest1(Constant.SUBCAT, requestBody, str_r_pt_tok);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "RESULT------------>" + s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                sub_categories.add("Select sub category");
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    sub_categories.add(name);
                }
                subcat_dataadapter = new ArrayAdapter<String>(FindTutor.this, android.R.layout.simple_spinner_item, sub_categories);
                subcat_dataadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spi_sel_sub_cat.setAdapter(subcat_dataadapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}