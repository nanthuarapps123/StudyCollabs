package com.bma.studycollabs;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Signupscreen extends AppCompatActivity {

    EditText ed_fname,ed_mname,ed_lname,ed_mail,ed_phone,ed_username,ed_password;
    private Spinner spi_tot;
    Button bt_signup;
    String str_fname,str_mname,str_lname,str_mail,str_phone,str_username,str_password,str_tot;
    AlertDialogManager alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupscreen);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        alert = new AlertDialogManager();
        bt_signup = (Button) findViewById(R.id.btn_signup);

        ed_fname = (EditText)findViewById(R.id.input_fname);
        ed_mname = (EditText)findViewById(R.id.input_mname);
        ed_lname = (EditText)findViewById(R.id.input_lname);
        ed_mail = (EditText)findViewById(R.id.input_email);
        ed_phone = (EditText)findViewById(R.id.input_phone);
        ed_username = (EditText)findViewById(R.id.input_uname);
        ed_password = (EditText)findViewById(R.id.input_pword);

        addItemsOnSpinnertot();

        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_fname = ed_fname.getText().toString();
                str_mname = ed_mname.getText().toString();
                str_lname = ed_lname.getText().toString();
                str_mail = ed_mail.getText().toString();
                str_phone = ed_phone.getText().toString();
                str_username = ed_username.getText().toString();
                str_password = ed_password.getText().toString();

                if (str_fname.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill your first name",Toast.LENGTH_SHORT).show();
                }else if (str_lname.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill your last name",Toast.LENGTH_SHORT).show();
                }else if (str_mail.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill your mail",Toast.LENGTH_SHORT).show();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(str_mail).matches()) {
                    Toast.makeText(getApplicationContext(),"You are entered an incorrect mail format",Toast.LENGTH_SHORT).show();
                }else if (!Patterns.PHONE.matcher(str_phone).matches()) {
                    Toast.makeText(getApplicationContext(),"You are entered an incorrect phone number",Toast.LENGTH_SHORT).show();
                }else if (str_phone.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill your phone number",Toast.LENGTH_SHORT).show();
                }else if (str_username.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill your user name",Toast.LENGTH_SHORT).show();
                }else if (str_password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill your password",Toast.LENGTH_SHORT).show();
                }else {
                    addAddressValues(str_fname, str_mname, str_lname, str_username, str_mail, str_password, str_phone, str_tot);
                }
            }
        });
    }

    public void addItemsOnSpinnertot() {

        spi_tot = (Spinner) findViewById(R.id.spi_tot);
        List<String> list = new ArrayList<String>();
        list.add("Select");
        list.add("None");
        list.add("Student");
        list.add("Teacher");
        list.add("Volunteer");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spi_tot.setAdapter(dataAdapter);
        spi_tot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).toString().equals("Select")){
                    Toast.makeText(getApplicationContext(),"Please select type of tutor",Toast.LENGTH_SHORT).show();
                }else if (adapterView.getItemAtPosition(i).toString().equals("None")){
                    Toast.makeText(getApplicationContext(),"You are selected none..",Toast.LENGTH_SHORT).show();
                    str_tot = adapterView.getItemAtPosition(i).toString();
                }else if (adapterView.getItemAtPosition(i).toString().equals("Student")){
                    Toast.makeText(getApplicationContext(),"Student selected..",Toast.LENGTH_SHORT).show();
                    str_tot = adapterView.getItemAtPosition(i).toString();
                }else if (adapterView.getItemAtPosition(i).toString().equals("Teacher")){
                    Toast.makeText(getApplicationContext(),"Teacher selected..",Toast.LENGTH_SHORT).show();
                    str_tot = adapterView.getItemAtPosition(i).toString();
                }else if (adapterView.getItemAtPosition(i).toString().equals("Volunteer")){
                    Toast.makeText(getApplicationContext(),"Volunteer selected..",Toast.LENGTH_SHORT).show();
                    str_tot = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

        private void addAddressValues(String str_fnamee, String str_mnamee, String str_lnamee, String str_usernamee, String str_maile, String str_passworde, String str_phonee, String str_tote) {

//        String regUrl = "http://studycollab.com/mobile/api/index.php/Account/register";
        StringRequest putRequest = new StringRequest(Request.Method.PUT, Constant.SIGN_UP_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Responsse", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("success")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                ed_fname.setText("");
                                ed_mname.setText("");
                                ed_lname.setText("");
                                ed_mail.setText("");
                                ed_phone.setText("");
                                ed_username.setText("");
                                ed_password.setText("");
                                startActivity(new Intent(getApplicationContext(),Signinscreen.class));
                            }else {
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String> ();
                params.put("firstName", str_fname);
                params.put("middleName", str_mname);
                params.put("lastName", str_lname);
                params.put("userName", str_username);
                params.put("emailAddress", str_mail);
                params.put("password", str_password);
                params.put("telephone", "");
                params.put("mobile", str_phone);
                params.put("screenName","");
                params.put("tutorType", str_tot);
                return params;
            }
        };

        AppData.getInstance().addToRequestQueue(putRequest);


    }
}