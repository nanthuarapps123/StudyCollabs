package com.bma.studycollabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cometchat.inscripts.com.cometchatcore.coresdk.CometChat;
import cometchat.inscripts.com.readyui.CCCometchatUI;

import static java.lang.Boolean.TRUE;

public class Signinscreen extends AppCompatActivity {
    private ActionBar toolbar;
    private EditText inputEmail, inputPassword;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    TextView err_txt_user,err_txt_pass,txt_for,txt_signup;
    Button bt_login;
    ProgressDialog pDialog;

    String str_unameomail,str_password;
    String str_token;
    String str_pass_tok;
    CometChat cometChat;

    AlertDialogManager alert;



    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_TOKEN = "keytoken";
    private static final String KEY_UNAME = "keyuname";
    private static final String KEY_PASS = "keypass";
    private static final String KEY_LOGN = "keylogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinscreen);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        alert = new AlertDialogManager();
        inputLayoutEmail = (TextInputLayout)findViewById(R.id.input_layout_username);
        inputLayoutPassword = (TextInputLayout)findViewById(R.id.input_layout_password);
        txt_for = (TextView)findViewById(R.id.btn_forget_pass);
        txt_signup = (TextView)findViewById(R.id.btn_signup);

        inputEmail = (EditText)findViewById(R.id.input_username);
        inputPassword = (EditText)findViewById(R.id.input_password);

        bt_login = (Button)findViewById(R.id.btn_login);


        txt_for.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Forgetpwdscreen.class));
            }
        });

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Signupscreen.class));
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_unameomail = inputEmail.getText().toString();
                str_password = inputPassword.getText().toString();
//                final String regUrl = "http://studycollab.com/mobile/api/index.php/Account/login";
                StringRequest putRequest = new StringRequest(Request.Method.POST, Constant.SIGN_IN_URL,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Responsse", response);
                                try {
                                    JSONObject o = new JSONObject(response);
                                    String token =  o.getString("token");
                                    JSONObject phone = o.getJSONObject("user");
                                    String fname = phone.getString("firstName");
                                    String lname = phone.getString("lastName");
                                    String ruser = phone.getString("returningUser");
                                    String imgpath = phone.getString("imagePath");
                                    if (!token.equals("") && !fname.equals("") && !lname.equals("") && !ruser.equals("") && !imgpath.equals("")){
                                        if(ruser.equals("false")){

                                            String a = str_unameomail;
                                            String b = str_password;
                                            String c = token;

                                            Log.e("BEFORECHECKSESSIONA",a);
                                            Log.e("BEFORECHECKSESSIONB",b);
                                            Log.e("BEFORECHECKSESSIONC",c);
//
                                            SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putString(KEY_TOKEN, c);
                                            editor.putString(KEY_UNAME, a);
                                            editor.putString(KEY_PASS, b);
                                            editor.putBoolean(KEY_LOGN,TRUE);
                                            editor.apply();





//                                            session.createLoginSession();

                                            Intent i_main = new Intent(getApplicationContext(),Maintwo.class);
                                            Bundle bun_main = new Bundle();
                                            bun_main.putString("btoken",token);
                                            bun_main.putString("buser",str_unameomail);
                                            bun_main.putString("bpass",str_password);
                                            bun_main.putString("bfname",fname);
                                            bun_main.putString("blname",lname);
                                            bun_main.putString("bruser",ruser);
                                            bun_main.putString("bimg",imgpath);
                                            i_main.putExtras(bun_main);
                                            startActivity(i_main);
                                            finish();
                                            inputEmail.setText("");
                                            inputPassword.setText("");
                                        }else if (ruser.equals("true")){
                                            alert.showAlertDialog(Signinscreen.this, "Activate account!..", "Please check your mail to activate account...", false);
                                        }
                                    }else {
                                        alert.showAlertDialog(Signinscreen.this, "Something went wrong!..", "Try angain later...", false);
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
                                    alert.showAlertDialog(Signinscreen.this, "Connection Error", "Cannot connect to Internet...Please check your connection!", false);
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
                        params.put("userName", str_unameomail);
                        params.put("password", str_password);
                        return params;
                    }
                };

                AppData.getInstance().addToRequestQueue(putRequest);
            }

        });

    }


}
