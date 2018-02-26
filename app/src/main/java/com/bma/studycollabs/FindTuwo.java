package com.bma.studycollabs;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FindTuwo extends AppCompatActivity {

    String str_tot,str_cat,str_scat,str_hint;
    String str_tok;
    String str_uname;
    String jsonurl;
    ArrayList<String> list_uid,list_ttype,list_cat,list_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tuwo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle bu_ft_sev = getIntent().getExtras();
        str_tok = bu_ft_sev.getString("str_tok");
        str_tot = bu_ft_sev.getString("tutype");
        str_cat = bu_ft_sev.getString("tucat");
        str_scat = bu_ft_sev.getString("tuscat");
        str_hint = bu_ft_sev.getString("tuhint");
        str_uname = bu_ft_sev.getString("tuuname");

        //jsonurl = "http://studycollab.com/mobile/api/index.php/Dashboard/getTutoruser";

        list_uid = new ArrayList<String>();
        list_ttype = new ArrayList<String>();
        list_cat = new ArrayList<String>();
        list_img = new ArrayList<String>();

        if (new CheckNetwork(FindTuwo.this).isNetworkAvailable()) {
            new Test_url().execute();
        }else {
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }





    }

    private void initViews(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Tutor> tutor_lis = prepareData();
        TutorAdapter tutorAdapter = new TutorAdapter(getApplicationContext(),tutor_lis,str_tok,str_uname);
        recyclerView.setAdapter(tutorAdapter);
    }

    private ArrayList<Tutor> prepareData(){
        ArrayList<Tutor> tutor_list = new ArrayList<>();
        for(int i=0;i<list_uid.size();i++){
            Tutor tutor = new Tutor();
            tutor.setuser_id(list_uid.get(i));
            tutor.setttype(list_ttype.get(i));
            tutor.setcat(list_cat.get(i));
            tutor.setimgurl(list_img.get(i));
            Log.d("Preparedatacheck",list_cat.get(i));
            tutor_list.add(tutor);
        }
        return tutor_list;
    }


    public class Test_url extends AsyncTask<String, Void, String> {

        public Test_url() {
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
                    jsonObject.put("tutorType", str_tot);
                    jsonObject.put("subjects", str_cat);
                    jsonObject.put("subsubjects", str_scat);
                    jsonObject.put("username", str_hint);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestBody = jsonObject.toString();
                Log.d("REQYESTBODY",requestBody);
                return jsonStr = HttpUtils.makeRequest1(Constant.TUTORUSER, requestBody,str_tok );
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
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String user_id = jsonObject.getString("user_id");
                    String tutorType = jsonObject.getString("tutorType");
                    String category = jsonObject.getString("category");
                    String Image = jsonObject.getString("Image");

                    list_uid.add(user_id);
                    list_ttype.add(tutorType);
                    list_cat.add(category);
                    list_img.add(Constant.root+Image);
                    initViews();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
