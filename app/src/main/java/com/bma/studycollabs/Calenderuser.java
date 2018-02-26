package com.bma.studycollabs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bma.studycollabs.CalendarAdapter;

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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.microedition.khronos.egl.EGLSurface;

public class Calenderuser extends Activity {
    public GregorianCalendar cal_month, cal_month_copy;
    private CalendarAdapter cal_adapter;
    private TextView tv_month;
    private AndroidListAdapter list_adapter;
    GridView gridview;
    String str_tok;
    String str_uname;
    String str_loc;
    //private static final String JSON_URL = "http://studycollab.com/api/v1/index.php/Schedule/getScheduleEvent";
    TextView txt_name,txt_date,txt_stime,txt_etime;
    Spinner s_tot_hrs,s_appo_type;
    EditText ed_title,ed_loc;
    Button bt_sub_req;
    private int mYear, mMonth, mDay, mHour, mMinute;
    List<String> app_type;
    String str_app_type;
    AlertDialog.Builder alertDialog;

    AlertDialogManager alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        Bundle bu_taap_tok = getIntent().getExtras();
        str_tok = bu_taap_tok.getString("str_tok");
        str_uname = bu_taap_tok.getString("str_uname");

        alert = new AlertDialogManager();

        txt_name = (TextView)findViewById(R.id.txt_name);
        txt_date = (TextView)findViewById(R.id.txt_date_pick);
        txt_stime = (TextView)findViewById(R.id.txt_stime_pic);
        txt_etime = (TextView)findViewById(R.id.txt_etime_pic);

        s_appo_type = (Spinner)findViewById(R.id.spi_atype);
        ed_title = (EditText)findViewById(R.id.ed_title);
        ed_loc = (EditText)findViewById(R.id.ed_loc);
        bt_sub_req = (Button)findViewById(R.id.butt_sub_req);

        app_type = new ArrayList<String>();

        app_type.add("Select an appointment type");
        app_type.add("Audio/video");
        app_type.add("In Person");

        ArrayAdapter<String> atype_dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, app_type);
        atype_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_appo_type.setAdapter(atype_dataAdapter);

        s_appo_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("Select an appointment type")){
                    Toast.makeText(getApplicationContext(),"Select an appointment type",Toast.LENGTH_SHORT).show();



                }else if (parent.getItemAtPosition(position).toString().equals("Audio/video")){
                    str_app_type = parent.getItemAtPosition(position).toString();
                }else if (parent.getItemAtPosition(position).toString().equals("In Person")){
                    ed_loc.setVisibility(View.VISIBLE);
                    str_app_type = parent.getItemAtPosition(position).toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bt_sub_req.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_name.getText().toString().equals("Name")){
                    alert.showAlertDialog(Calenderuser.this, "Try again", "Try again ...Some went wrong", false);
                }else if(ed_title.getText().toString().equals("")){
                    alert.showAlertDialog(Calenderuser.this, "Title missing", "Enter the Topic/title name", false);
                }else if(txt_date.getText().toString().equals("Schedule Date")){
                    alert.showAlertDialog(Calenderuser.this, "Schedule date", "Select a date to schedule", false);
                }else if(txt_stime.getText().toString().equals("Start Time")){
                    alert.showAlertDialog(Calenderuser.this, "Schedule Time", "Select a time to schedule", false);
                }else if(s_appo_type.getSelectedItem().toString().equals("Select an appointment type")){
                    alert.showAlertDialog(Calenderuser.this, "Appoinment type missing", "Select an appoinment type...", false);
                }else if(s_appo_type.getSelectedItem().toString().equals("In Person")){
                    if(ed_loc.getText().toString().equals("")){
                        alert.showAlertDialog(Calenderuser.this, "Spot missing", "Enter spot.", false);
                    }else {
                        String str_uname = txt_name.getText().toString();
                        String str_title = ed_title.getText().toString();
                        String str_sdate = txt_date.getText().toString();
                        String str_stime = txt_stime.getText().toString();
                        String str_etime = txt_etime.getText().toString();
                        str_loc = ed_loc.getText().toString();

                        if (new CheckNetwork(Calenderuser.this).isNetworkAvailable()) {
                            new post_req(str_uname,str_title,str_sdate,str_stime,str_etime,str_app_type,str_loc).execute();
                        }else {
                            alert.showAlertDialog(Calenderuser.this, "Connection error", "Please check your internet connection", false);
                        }


                    }
                }else{
                    String str_uname = txt_name.getText().toString();
                    String str_title = ed_title.getText().toString();
                    String str_sdate = txt_date.getText().toString();
                    String str_stime = txt_stime.getText().toString();
                    String str_etime = txt_etime.getText().toString();
                    str_loc = ed_loc.getText().toString();
                    if (new CheckNetwork(Calenderuser.this).isNetworkAvailable()) {
                        new post_req(str_uname,str_title,str_sdate,str_stime,str_etime,str_app_type,str_loc).execute();
                    }else {
                       alert.showAlertDialog(Calenderuser.this, "Connection error", "Please check your internet connection", false);
                    }


                }
            }
        });
        txt_name.setText(str_uname);
        txt_date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Calenderuser.this,
                        new DatePickerDialog.OnDateSetListener() {


                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txt_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        txt_stime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Calenderuser.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txt_stime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        txt_etime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Calenderuser.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txt_etime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        CalendarCollection.date_collection_arr=new ArrayList<CalendarCollection>();

        if (new CheckNetwork(Calenderuser.this).isNetworkAvailable()){
            new Test_url().execute();
        }else {
            alert.showAlertDialog(Calenderuser.this, "Connection error", "Please check your internet connection", false);
        }


        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        cal_adapter = new CalendarAdapter(Calenderuser.this, cal_month,CalendarCollection.date_collection_arr);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));

        ImageButton previous = (ImageButton) findViewById(R.id.ib_prev);
        previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });
        ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();
            }
        });
        gridview = (GridView) findViewById(R.id.gv_calendar);
        gridview.setAdapter(cal_adapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ((CalendarAdapter) parent.getAdapter()).setSelected(v,position);

                String selectedGridDate = CalendarAdapter.day_string.get(position);
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
                ((CalendarAdapter) parent.getAdapter()).setSelected(v,position);
                ((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate, Calenderuser.this);
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
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
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
                    jsonObject.put("tutor_userid", str_uname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestBody = jsonObject.toString();
                Log.d("REQYESTBODY",requestBody);
                return jsonStr = HttpUtils.makeRequest1(Constant.GETSCHEDULEEVENT, requestBody,str_tok );
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
                    String convertstime = getFormatedDate(start, "yyyy-MM-dd HH:mm:ss", "HH:mm:ss");
                    String convertetime = getFormatedDate(end, "yyyy-MM-dd HH:mm:ss", "HH:mm:ss");
                    Log.d("dateoutput",convertedDatee);
                    CalendarCollection.date_collection_arr.add(new CalendarCollection(convertedDatee,title,id,convertstime,convertetime,color));
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

    public class post_req extends AsyncTask<String, Void, String> {


        String str_uname,str_title,str_sdate,str_stime,str_etime,str_atype,str_loc;

        public post_req(String str_uname, String str_title, String str_sdate, String str_stime, String str_etime, String str_atype, String str_loc ) {

            this.str_uname = str_uname;
            this.str_title = str_title;
            this.str_sdate = str_sdate;
            this.str_stime = str_stime;
            this.str_etime = str_etime;
            this.str_atype = str_atype;
            this.str_loc = str_loc;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(Calenderuser.this);
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("tutor_name", str_uname);
                    jsonObject.put("title", str_title);
                    jsonObject.put("req_date", str_sdate);
                    jsonObject.put("start_time", str_stime);
                    jsonObject.put("end_time", str_etime);
                    jsonObject.put("appointment", str_atype);
                    jsonObject.put("locaiton", str_loc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);

                String requestBody = jsonArray.toString();
                Log.d("RERERETE",requestBody);


                jsonStr = HttpUtils.makeRequest1(Constant.SUBMITSCHEDULEEVENT, requestBody, str_tok);
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

            try {
                JSONObject jsonObject = new JSONObject(s);
                String res = jsonObject.getString("status");
                Log.d("SUCCETES",res);
                if (res.equals("success")){

                    String res1 = jsonObject.getString("message");
                    Log.d("SUCCETES",res1);

                    alertDialog.setTitle(res1);
                    alertDialog.setMessage("Are you want to add more schedule..click Add");
                    Log.d("SUCCETES","ONE");
                    alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ed_title.setText("");
                            txt_date.setText("Schedule Date");
                            txt_stime.setText("Start Time");
                            txt_etime.setText("End Time");
                            Log.d("SUCCETES","TWO");
                            Intent i = new Intent(getApplicationContext(),Calenderuser.class);
                            Bundle bu_taap_tok = new Bundle();
                            bu_taap_tok.putString("str_tok",str_tok);
                            bu_taap_tok.putString("str_uname",str_uname);
                            i.putExtras(bu_taap_tok);
                            startActivity(i);

                        }
                    });


                    alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent_reqsch = new Intent(getApplicationContext(),RequestSchedue.class);
                            Bundle bu_re_sc = new Bundle();
                            bu_re_sc.putString("str_tok",str_tok);
                            intent_reqsch.putExtras(bu_re_sc);
                            startActivity(intent_reqsch);
                        }
                    });
                    alertDialog.show();

                }else if (res.equals("error")){
                    String res1 = jsonObject.getString("message");
                    alert.showAlertDialog(Calenderuser.this, "Some went wrong...", ""+res1, false);
                }else {
                    alert.showAlertDialog(Calenderuser.this, "Some went wrong...", "Please try again later", false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
