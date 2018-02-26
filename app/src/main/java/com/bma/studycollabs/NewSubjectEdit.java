package com.bma.studycollabs;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewSubjectEdit extends AppCompatActivity {

    Spinner spi_main;
    Spinner spi_subcat;
    List<String> list;
    List<String> list_grades;
    List<String> list_grade_id;
    List<String> list_subjects;
    public static List<String> list_sub_cat,list_subcat_id;
    public static List<String> list_subcat_grade;
    String jsonurl,str_token,graid;
    int num_subjects;
    ArrayAdapter<String> sub_dataAdapter;
    ArrayAdapter<String> childadapter;
    LinearLayout ll,l2;
    Button bu_save;
    int height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subject_edit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle bu_sub_ed = getIntent().getExtras();
        str_token = bu_sub_ed.getString("toksese");
        Log.d("TOKENCHCK",str_token);

        ll = (LinearLayout) findViewById(R.id.lay_sub);
        l2 = (LinearLayout) findViewById(R.id.lay_grades);
        bu_save = (Button)findViewById(R.id.btn_save_changes);

        //jsonurl = "http://studycollab.com/mobile/api/index.php/Dashboard/subjectGrades";

        initimainspi();

        spi_main.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    ll.removeAllViews();
                    l2.removeAllViews();
                    bu_save.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Please select any subject",Toast.LENGTH_SHORT).show();
                }else {
                    list_subcat_id.clear();
                    list_sub_cat.clear();
                    list_subcat_grade.clear();
                    getsubcatdetails(position);
                    //test(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bu_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sizeoflistsubcat",String.valueOf(list_sub_cat.size()));
                for (int i =0;i<list_sub_cat.size();i++){
                    spi_subcat = (Spinner)findViewById(i);

                    if (spi_subcat.getSelectedItem().toString().equals("-") || spi_subcat.getSelectedItem().toString().equals("null") || spi_subcat.getSelectedItem().toString().equals("")){
                        graid = "";
                    }else if(spi_subcat.getSelectedItem().toString().equals("E")){
                        graid = "1";
                    }else if(spi_subcat.getSelectedItem().toString().equals("E+")){
                        graid = "2";
                    }else if(spi_subcat.getSelectedItem().toString().equals("D")){
                        graid = "3";
                    }else if(spi_subcat.getSelectedItem().toString().equals("D+")){
                        graid = "4";
                    }else if(spi_subcat.getSelectedItem().toString().equals("C")){
                        graid = "5";
                    }else if(spi_subcat.getSelectedItem().toString().equals("C+")){
                        graid = "6";
                    }else if(spi_subcat.getSelectedItem().toString().equals("B")){
                        graid = "7";
                    }else if(spi_subcat.getSelectedItem().toString().equals("B+")){
                        graid = "8";
                    }else if(spi_subcat.getSelectedItem().toString().equals("A")){
                        graid = "9";
                    }else if(spi_subcat.getSelectedItem().toString().equals("A+")){
                        graid = "10";
                    }

                    if (new CheckNetwork(NewSubjectEdit.this).isNetworkAvailable()) {
                        new Test_url(list_subcat_id.get(i),list_sub_cat.get(i),graid).execute();
                    }else {
                        Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                    }


                    Log.d("SUBCATIDDDD",list_subcat_id.get(i));
                    Log.d("SENDCHECKKK",list_sub_cat.get(i));
                    Log.d("SENDCHECKKK",spi_subcat.getSelectedItem().toString());
                    Log.d("GRADEIDDDDD",graid);
                    Log.d("===========","===========================");

                }

            }

        });

    }



    public void initimainspi() {

        spi_main = (Spinner)findViewById(R.id.Spi_main_api);
        list = new ArrayList<String>();
        list_grades = new ArrayList<String>();
        list_grade_id = new ArrayList<String>();
        list_subjects = new ArrayList<String>();
        list_sub_cat = new ArrayList<String>();
        list_subcat_id = new ArrayList<String>();
        list_subcat_grade = new ArrayList<String>();

        list_grades.add("-");
        list_grade_id.add("");
        list_subjects.add("Select any category");

        getsubandgrade();

        sub_dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_subjects);
        sub_dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spi_main.setAdapter(sub_dataAdapter);


    }


    public void test() {

        ll.removeAllViews();
        l2.removeAllViews();
        bu_save.setVisibility(View.VISIBLE);

        for (int i = 0;i<list_sub_cat.size();i++){
            final TextView tv = new TextView(NewSubjectEdit.this);
            spi_subcat = new Spinner(NewSubjectEdit.this);

            spi_subcat.setId(i);                                         //spinner
            tv.setBackgroundResource(R.drawable.box);
            spi_subcat.setBackgroundResource(R.drawable.box);            //spinner
            tv.setText(list_sub_cat.get(i));
            Log.d("list_sub_cat",list_sub_cat.get(i));
            tv.setTextSize(18);
            tv.setTextColor(Color.parseColor("#000000"));
            childadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_grades);
            childadapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            spi_subcat.setAdapter(childadapter);

            String str_gr = list_subcat_grade.get(i);
            if (str_gr.equals("") || str_gr.equals("null")) {
                Log.d("nulllAAAACHECK", str_gr);
                spi_subcat.setSelection(0);
            } else {
                Log.d("AAAACHECK", str_gr);
                int int_gr = Integer.parseInt(str_gr);
                spi_subcat.setSelection(int_gr);
            }



            tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    height =  tv.getMeasuredHeight();
                    Log.e("HEIGHTOFTEXTVIEW",String.valueOf(height));
                }
            });

            spi_subcat.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));
            tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER);

            tv.setPadding(14, 14, 14, 14);
            spi_subcat.setPadding(16,16,16,16);

            ll.addView(tv);
            l2.addView(spi_subcat);

        }

    }


    public void getsubandgrade() {
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constant.SUB_GRADES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("RESONSEFROMSUBJECTGRADE",response);

                    JSONObject iniobj = new JSONObject(response);
                    JSONArray jsonArray = iniobj.getJSONArray("grades");
                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        list_grade_id.add(id);
                        list_grades.add(name);

                        Log.d("gradeid",id);
                        Log.d("gradename",name);
                        Log.e("gradelistlength",String.valueOf(list_grades.size()));
                    }


                    JSONArray catjsonArray = iniobj.getJSONArray("categories");
                    for (int i = 0;i<catjsonArray.length();i++){

                        String array_Length = String.valueOf(catjsonArray.length());

                        JSONObject catjsonObject = catjsonArray.getJSONObject(i);
                        String catid = catjsonObject.getString("id");
                        String catname = catjsonObject.getString("name");
                        list_subjects.add(catname);

                        num_subjects = list_subjects.size();

                        Log.d("catid",catid);
                        Log.d("catname",catname);
                        Log.e("catlistlength",String.valueOf(num_subjects));
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
                headers.put("Authorization", str_token);
                return headers;
            }
        };
        // Adding request to request queue
        AppData.getInstance().addToRequestQueue(strReq);

    }

    private void getsubcatdetails(final int position) {
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constant.SUB_GRADES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("RESONSEFROMSUBJECTGRADE",response);

                    JSONObject iniobj = new JSONObject(response);
                    JSONArray jsonArray = iniobj.getJSONArray("grades");
                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");

                    }

                    JSONArray catjsonArray = iniobj.getJSONArray("categories");
                    for (int i = 0;i<catjsonArray.length();i++){

                        JSONObject catjsonObject = catjsonArray.getJSONObject(i);
                        String catid = catjsonObject.getString("id");
                        String catname = catjsonObject.getString("name");


                        String spi_pos = String.valueOf(position);
                        Log.d("getcatid",catid);
                        Log.d("getposition",spi_pos);

                        JSONArray subjsonArray = catjsonObject.getJSONArray("subjects");
                        for (int j =0; j<subjsonArray.length();j++ ){

                            JSONObject subsonObject = subjsonArray.optJSONObject(j);

                            String subid = subsonObject.getString("id");
                            String subname = subsonObject.getString("name");
                            String subselgra = subsonObject.getString("selectedGrade");

                            if (spi_pos.equals(catid)){
                                list_subcat_id.add(subid);
                                list_sub_cat.add(subname);
                                list_subcat_grade.add(subselgra);
                            }

                            Log.d("subid",subid);
                            Log.d("subname",subname);
                            Log.d("subgrade",subselgra);

                            Log.d("lengthsubcat",String.valueOf(list_sub_cat.size()));

                        }
                        Log.e("Loopfinished","true");
                        test();
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
                headers.put("Authorization", str_token);
                return headers;
            }
        };
        // Adding request to request queue
        AppData.getInstance().addToRequestQueue(strReq);
    }

    public class Test_url extends AsyncTask<String, Void, String> {
        String s_id,s_name,g_id;


        public Test_url(String s, String s1, String hai) {
            s_id = s;
            s_name =s1;
            g_id = hai;
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
                    jsonObject.put("id", s_id);
                    jsonObject.put("selectedGrade", g_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);

                String requestBody = jsonArray.toString();
                Log.d("RERERETE",requestBody);


                jsonStr = HttpUtils.makeRequest1(Constant.SUB_SAVE_GRADES, requestBody, str_token);
                Log.d("JSONSTRRJ",jsonStr);
                return jsonStr;

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "RESULT------------>" + s);
        }

    }
}
