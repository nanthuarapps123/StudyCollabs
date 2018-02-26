package com.bma.studycollabs;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Forgetpwdscreen extends AppCompatActivity {
    EditText ed_fpwd_in_una,ed_fpwd_in_mail;
    TextView txt_fpwd_err;
    Button bt_rst_pwd;
    String str_uname,str_email,str_tok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwdscreen);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ed_fpwd_in_una = (EditText)findViewById(R.id.fpwd_input_username);
        ed_fpwd_in_mail = (EditText)findViewById(R.id.fpwd_input_email);

        bt_rst_pwd = (Button)findViewById(R.id.btn_rset_password);

//        txt_fpwd_err = (TextView)findViewById(R.id.error_fpwd_password);

        bt_rst_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ed_fpwd_in_una.getText().toString().equals("")){
                    Toast.makeText(Forgetpwdscreen.this, "Enter user name", Toast.LENGTH_SHORT).show();
                }else if(ed_fpwd_in_mail.getText().toString().equals("")){
                    Toast.makeText(Forgetpwdscreen.this, "Enter mail id", Toast.LENGTH_SHORT).show();
                }else {
                    str_uname = ed_fpwd_in_una.getText().toString();
                    str_email = ed_fpwd_in_mail.getText().toString();
                    addAddressValues();
                }
            }
        });

    }

    private void addAddressValues() {
        //String regUrl = "http://studycollab.com/api/v1/index.php/Account/forgotPassword";
        StringRequest putRequest = new StringRequest(Request.Method.POST, Constant.FORGET_PASS_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESLK",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equals("success")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                ed_fpwd_in_una.setText("");
                                ed_fpwd_in_mail.setText("");
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
                Map<String, String>  params = new HashMap<String, String>();
                params.put("userName", str_uname);
                params.put("email", str_email);
                return params;
            }
        };
        AppData.getInstance().addToRequestQueue(putRequest);
    }
}
