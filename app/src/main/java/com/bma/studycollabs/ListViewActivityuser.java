package com.bma.studycollabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bma.studycollabs.AndroidListAdapter;
import com.bma.studycollabs.CalendarCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class ListViewActivityuser extends AppCompatActivity implements OnClickListener {

    private ListView lv_android;
    private AndroidListAdapter list_adapter;
    private Button btn_calender;
    String user_phone,str_passcode;
    /***************************for calender**************************************************/
    private static final String JSON_URL = "http://studycollab.com/api/v1/index.php/Schedule/getScheduleEvent";
    /***************************for calender**************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        ActionBar b = getSupportActionBar();
        b.hide();

//        CalendarCollection.date_collection_arr=new ArrayList<CalendarCollection>();
//        new Test_url().execute();
        getWidget();

    }

    public void getWidget(){
        btn_calender = (Button) findViewById(R.id.btn_calender);
        btn_calender.setOnClickListener(this);
        lv_android = (ListView) findViewById(R.id.lv_android);
//        list_adapter=new AndroidListAdapter(ListViewActivityuser.this,R.layout.list_item, CalendarCollection.date_collection_arr);
//        lv_android.setAdapter(list_adapter);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_calender:
                Intent in = new Intent(getApplicationContext(), Calenderuser.class);//back button done
                startActivity(in);
                break;
            default:
                break;
        }
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
                    jsonObject.put("tutor_userid", "bala");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestBody = jsonObject.toString();
                Log.d("REQYESTBODY",requestBody);
                return jsonStr = HttpUtils.makeRequest1(Constant.GETSCHEDULEEVENT, requestBody,"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJVME0wTVRFd05ESXdNVGRmTURRME9USTIiLCJpYXQiOjE1MDk3NzA5NjYsIm5iZiI6MTUwOTc3MDk2NiwiaXNzIjoic2MubG9jYWxob3N0IiwidXNlciI6eyJpZCI6IjQiLCJ1c2VyX2lkIjoiYmFsYSIsImZpcnN0TmFtZSI6ImJhbGEiLCJsYXN0TmFtZSI6InNlbHZhcmFzdSIsInJldHVybmluZ1VzZXIiOiIwIiwiaW1hZ2VwYXRoIjoidXBsb2FkSW1hZ2VcL2JhbGEyMDE3XzEwXzIwXzA1XzU5XzQ4LmpwZyIsInN0YXR1cyI6IkFjdGl2ZSJ9fQ.80NVdiUPSCZwgnubHd93GLH_v-7HoB8XXUWNob_g4Ruv1QhXt-mqghRVBgvkT1T6qHFt3Rgczbo7BvrTcKTURA" );
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
                    String id = jsonObject.getString("id");
                    String title = jsonObject.getString("title");
                    String datetime = jsonObject.getString("schedule_datetime");
                    String start = jsonObject.getString("start");
                    String end = jsonObject.getString("end");
                    String color = jsonObject.getString("color");

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date convertedDate = new Date();
                    convertedDate = dateFormat.parse(datetime);
                    String convertedDatee = getFormatedDate(datetime, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
                    Log.d("dateoutput",convertedDatee);
                    CalendarCollection.date_collection_arr.add(new CalendarCollection(convertedDatee,title,id,start,end,color));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getFormatedDate(String strDate, String sourceFormate, String destinyFormate) {
        SimpleDateFormat df;
        df = new SimpleDateFormat(sourceFormate);
        Date date = null;
        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df = new SimpleDateFormat(destinyFormate);
        return df.format(date);
    }
}
