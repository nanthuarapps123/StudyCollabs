package com.bma.studycollabs;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class MyCalender extends Activity {
    String str_tok;
    String myeventurl;
    public GregorianCalendar cal_month, cal_month_copy;
    private MyCalendarAdapter mycal_adapter;
    private TextView tv_month;
    GridView gridview;
    ArrayList<String> al_id,al_tutor_userid,al_title,al_schdule_date,al_start_time,al_end_time,al_appiont_type,al_status,al_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calender);

        final Bundle bu_my_cal = getIntent().getExtras();
        str_tok = bu_my_cal.getString("toksept");

        //myeventurl = "http://studycollab.com/api/v1/index.php/Schedule/getScheduleEventByUser";

        al_id = new ArrayList<String>();
        al_tutor_userid = new ArrayList<String>();
        al_title = new ArrayList<String>();
        al_schdule_date = new ArrayList<String>();
        al_start_time = new ArrayList<String>();
        al_end_time = new ArrayList<String>();
        al_appiont_type = new ArrayList<String>();
        al_status = new ArrayList<String>();
        al_location = new ArrayList<String>();

        MyCalenderCollection.my_date_collection_arr=new ArrayList<MyCalenderCollection>();
        makejsonarray1();

        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        mycal_adapter = new MyCalendarAdapter(MyCalender.this, cal_month,MyCalenderCollection.my_date_collection_arr);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));

        ImageButton previous = (ImageButton) findViewById(R.id.ib_prev);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });
        ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();
            }
        });
        gridview = (GridView) findViewById(R.id.gv_calendar);
        gridview.setAdapter(mycal_adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ((MyCalendarAdapter) parent.getAdapter()).setSelected(v,position);

                String selectedGridDate = MyCalendarAdapter.day_string.get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*","");

                int gridvalue = Integer.parseInt(gridvalueString);
                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((MyCalendarAdapter) parent.getAdapter()).setSelected(v,position);
                ((MyCalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate, MyCalender.this);
            }
        });
    }

    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
                    cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
                    cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    public void refreshCalendar() {
        mycal_adapter.refreshDays();
        mycal_adapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }


    private void initViews(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_my_cal);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Requsets> sche_lis = prepareData1();
        Requestadater1 requestadapter = new Requestadater1(getApplicationContext(),sche_lis,str_tok);
        recyclerView.setAdapter(requestadapter);
    }

    private ArrayList<Requsets> prepareData1(){
        ArrayList<Requsets> req_list = new ArrayList<>();
        for(int i=0;i<al_tutor_userid.size();i++){
            Requsets requsets = new Requsets();
            requsets.setid(al_id.get(i));
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
                    Constant.GETSCHEDULEEVENTBYUSER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("RERERRschemyevent","Response: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArrayacceted = jsonObject.getJSONArray("Accepted");
                        for (int i = 0 ;i<jsonArrayacceted.length();i++){
                            JSONObject jsonObjectacc = jsonArrayacceted.getJSONObject(i);
                            String id = jsonObjectacc.getString("id");
                            String title = jsonObjectacc.getString("title");
                            String schedule_datetime = jsonObjectacc.getString("schedule_datetime");
                            String start = jsonObjectacc.getString("start");
                            String end = jsonObjectacc.getString("end");
                            String color = jsonObjectacc.getString("color");

                            String convertedDatee = getFormatedDate(schedule_datetime, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
                            String convertstime = getFormatedDate(start, "yyyy-MM-dd HH:mm:ss", "HH:mm:ss");
                            String convertetime = getFormatedDate(end, "yyyy-MM-dd HH:mm:ss", "HH:mm:ss");

                            MyCalenderCollection.my_date_collection_arr.add(new MyCalenderCollection(id,title,convertedDatee,convertstime,convertetime,color));

                        }
                        JSONArray jsonArrayending = jsonObject.getJSONArray("Pending");
                        for (int j = 0 ; j<jsonArrayending.length();j++){
                            JSONObject jsonObjectending = jsonArrayending.getJSONObject(j);
                            String id = jsonObjectending.getString("id");
                            String title = jsonObjectending.getString("title");
                            String schdule_date = jsonObjectending.getString("schdule_date");
                            String start_time = jsonObjectending.getString("start_time");
                            String end_time = jsonObjectending.getString("end_time");
                            String user_id = jsonObjectending.getString("user_id");
                            String appiont_type = jsonObjectending.getString("appiont_type");
                            String location = jsonObjectending.getString("location");
                            String status = jsonObjectending.getString("status");

                            al_id.add(id);
                            al_tutor_userid.add(user_id);
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
