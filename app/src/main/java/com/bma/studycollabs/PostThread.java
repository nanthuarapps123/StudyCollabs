package com.bma.studycollabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostThread extends Activity {
    EditText ed_pt_tit,ed_pt_des;
    Spinner spinnercat, spinnersubcat;
    String str_cat, str_sub_cat,str_cat_pos,str_scat_pos;
    String str_pt_tit,str_pt_des,str_ques_no;
    List<String> catlist;
    List<String> subcatlist;
    ArrayAdapter<String> catdataAdapter, subcatdataAdapter;
    Button bt_post_ques;
    String str_r_pt_tok;
    String str_pass_tok;

    List<String> list;
    List<String> list_grades;
    List<String> list_grade_id;
    List<String> list_subjects;
    public static List<String> list_sub_cat,list_subcat_id;
    public static List<String> list_subcat_grade;

    ArrayAdapter<String> sub_dataAdapter;

    AlertDialogManager alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_thread);

        Bundle bu_po_thr = getIntent().getExtras();
        str_r_pt_tok = bu_po_thr.getString("toksept");

//        initimainspi();

        ed_pt_tit = (EditText)findViewById(R.id.ed_tit);
        ed_pt_des = (EditText)findViewById(R.id.ed_des);
        bt_post_ques = (Button)findViewById(R.id.btn_post_ques);
        alert = new AlertDialogManager();

        Log.d("Post_thread_token",str_r_pt_tok);

        bt_post_ques.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                str_pt_tit = ed_pt_tit.getText().toString();
                str_pt_des = ed_pt_des.getText().toString();
                if (str_cat.equals("Select")){
                    alert.showAlertDialog(PostThread.this, "Incorrect Category..", "Please choose any other categoey", false);
                }else if (str_sub_cat.equals("Select")){
                    alert.showAlertDialog(PostThread.this, "Incorrect sub Category..", "Please choose any other sub categoey", false);
                }else if (str_pt_tit.equals("")){
                    alert.showAlertDialog(PostThread.this, "Incorrect title..", "Title shouldn't be empty", false);
                }else if (str_pt_des.equals("")){
                    alert.showAlertDialog(PostThread.this, "Incorrect description..", "Description shouldn't be empty", false);
                }else {
                    Log.d("Str_cat",str_cat);
                    Log.d("Str_SUBcat",str_sub_cat);
                    Log.d("Str_TIT",str_pt_tit);
                    Log.d("Str_DES",str_pt_des);
                    addValues(str_cat,str_sub_cat,str_pt_tit,str_pt_des);
                }
            }
        });

        addItemsOnSpinner();
    }
    private void addValues(String str_catt, String str_sub_catt, String str_pt_titt, String str_pt_desst) {
        //String regUrl = "http://studycollab.com/mobile/api/index.php/Dashboard/Postquestion";
        StringRequest putRequest = new StringRequest(Request.Method.PUT, Constant.POST_QUESTION,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Responsse", response);
                        if (!response.equals("")){
                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                            str_ques_no = response;
                            Bundle b_searchth = new Bundle();
                            b_searchth.putString("sid", str_ques_no);
                            b_searchth.putString("token",str_r_pt_tok);
                            Intent in = new Intent(getApplicationContext(), ViewQuestion.class);
                            in.putExtras(b_searchth);
                            startActivity(in);
                        }else {
                            Toast.makeText(getApplicationContext(),"Try again",Toast.LENGTH_SHORT).show();
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
                headers.put("Authorization", str_r_pt_tok);
                return headers;
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("title", str_pt_tit);
                params.put("description", str_pt_des);
                params.put("category", str_cat_pos);
                params.put("subCategory", str_scat_pos);
                return params;
            }
        };

        AppData.getInstance().addToRequestQueue(putRequest);
    }

    public void addItemsOnSpinner() {
        spinnercat = (Spinner) findViewById(R.id.spinner_tocat);
        spinnersubcat = (Spinner) findViewById(R.id.spinner_tosubcat);
        subcatlist = new ArrayList<String>();
        catlist = new ArrayList<String>();
        catlist.add("Select");
        catlist.add("World Language");
        catlist.add("History");
        catlist.add("ELA");
        catlist.add("Mathematics");
        catlist.add("Geography");
        catlist.add("Drawing");
        catlist.add("Physics");
        catlist.add("Mechanical");

        catdataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catlist);
        catdataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnercat.setAdapter(catdataAdapter);

        subcatdataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subcatlist);
        subcatdataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        //spinnersubcat.setAdapter(subcatdataAdapter);

        spinnercat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_cat = adapterView.getItemAtPosition(i).toString();
                str_cat_pos = String.valueOf(i);
                Log.d("SSSSSSiiiiCCC",str_cat_pos);
                if (str_cat.equals("Select")) {
                    subcatlist.clear();
                    subcatlist.add("Select any Category");
                    spinnersubcat.setAdapter(subcatdataAdapter);
                } else if (str_cat.equals("World Language")) {
                    subcatlist.clear();
                    subcatlist.add("Select");
                    subcatlist.add("Chinese");
                    subcatlist.add("Spanish");
                    subcatlist.add("English");
                    subcatlist.add("Arabic");
                    subcatlist.add("Hindi");
                    subcatlist.add("Bengali");
                    subcatlist.add("Portuguese");
                    subcatlist.add("Russian");
                    subcatlist.add("Japanese");
                    subcatlist.add("German, Standard");
                    subcatlist.add("Javanese");
                    subcatlist.add("Lahnda");
                    subcatlist.add("Telugu");
                    subcatlist.add("Vietnamese");
                    subcatlist.add("Marathi");
                    subcatlist.add("French");
                    subcatlist.add("Korean");
                    subcatlist.add("Tamil");
                    subcatlist.add("Italian");
                    subcatlist.add("Urdu");
                    spinnersubcat.setAdapter(subcatdataAdapter);
                } else if (str_cat.equals("History")) {
                    subcatlist.clear();
                    subcatlist.add("Select");
                    subcatlist.add("Political");
                    subcatlist.add("Diplomatic");
                    subcatlist.add("Social");
                    subcatlist.add("Cultural");
                    subcatlist.add("Economic");
                    subcatlist.add("Intellectual");
                    spinnersubcat.setAdapter(subcatdataAdapter);
                } else if (str_cat.equals("ELA")) {
                    subcatlist.clear();
                    subcatlist.add("Select");
                    spinnersubcat.setAdapter(subcatdataAdapter);
                } else if (str_cat.equals("Mathematics")) {
                    subcatlist.clear();
                    subcatlist.add("Select");
                    subcatlist.add("Algebra");
                    subcatlist.add("Calculas");
                    subcatlist.add("Geometry");
                    subcatlist.add("Combinatorics");
                    subcatlist.add("Logic");
                    subcatlist.add("Number theory");
                    subcatlist.add("Dynamical systems and differential equations");
                    subcatlist.add("Mathematical physics");
                    subcatlist.add("Analysis");
                    subcatlist.add("Topology");
                    spinnersubcat.setAdapter(subcatdataAdapter);
                } else if (str_cat.equals("Geography")) {
                    subcatlist.clear();
                    subcatlist.add("Select");
                    subcatlist.add("Physical Geography");
                    subcatlist.add("Humun Geography");
                    subcatlist.add("Environmental Geography");
                    subcatlist.add("Regional Geography");
                    subcatlist.add("Tropical Geography");
                    subcatlist.add("Economic Geography");
                    subcatlist.add("Political Geography");
                    subcatlist.add("Historical Geography");
                    subcatlist.add("Zoo Geography");
                    subcatlist.add("Social Geography");
                    spinnersubcat.setAdapter(subcatdataAdapter);
                } else if (str_cat.equals("Drawing")) {
                    subcatlist.clear();
                    subcatlist.add("Select");
                    subcatlist.add("Life Drawing");
                    subcatlist.add("Emotive Drawing");
                    subcatlist.add("Sketching");
                    subcatlist.add("Analytic Drawing");
                    subcatlist.add("Perspective Drawing");
                    subcatlist.add("Geometric Drawing");
                    subcatlist.add("Diagrammatic Drawing");
                    subcatlist.add("Illustration Drawing");
                    spinnersubcat.setAdapter(subcatdataAdapter);
                } else if (str_cat.equals("Physics")) {
                    subcatlist.clear();
                    subcatlist.add("Select");
                    subcatlist.add("Astrophysics");
                    subcatlist.add("Classical Mechanics");
                    subcatlist.add("Electromagnetism");
                    subcatlist.add("Optics");
                    subcatlist.add("Quantum Mechanics");
                    subcatlist.add("Reletivity");
                    subcatlist.add("Thermodynamics");
                    spinnersubcat.setAdapter(subcatdataAdapter);
                } else if (str_cat.equals("Mechanical")) {
                    subcatlist.clear();
                    subcatlist.add("Select");
                    subcatlist.add("Aerospace");
                    subcatlist.add("Acoustical");
                    subcatlist.add("Manufacturing");
                    subcatlist.add("Thermal");
                    subcatlist.add("Production");
                    subcatlist.add("Vehicle");
                    spinnersubcat.setAdapter(subcatdataAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnersubcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_sub_cat = adapterView.getItemAtPosition(i).toString();
                str_scat_pos = String.valueOf(i);
                Log.d("SSSSSSCCC",str_scat_pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //new new new new//new new new new

    public void initimainspi() {

//        spi_main = (Spinner)findViewById(R.id.Spi_main_api);
//        list = new ArrayList<String>();
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
//        spi_main.setAdapter(sub_dataAdapter);


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

//                        num_subjects = list_subjects.size();

                        Log.d("catid",catid);
                        Log.d("catname",catname);
//                        Log.e("catlistlength",String.valueOf(num_subjects));
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
                headers.put("Authorization", str_r_pt_tok);
                return headers;
            }
        };
        // Adding request to request queue
        AppData.getInstance().addToRequestQueue(strReq);
    }
    //new new new new//new new new new
}
