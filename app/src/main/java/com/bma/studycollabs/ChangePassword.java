package com.bma.studycollabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    EditText ed_old_pw,ed_new_pass,ed_conform_pass;
    String str_old_pass,str_new_pass,str_token;
    Button btn_chan_pw;

    AlertDialogManager alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle bu_ch_pw = getIntent().getExtras();
        str_token = bu_ch_pw.getString("strtok");

        alert = new AlertDialogManager();
        ed_old_pw = (EditText)findViewById(R.id.input_old_pass);
        ed_new_pass = (EditText)findViewById(R.id.input_newpass);
        ed_conform_pass = (EditText)findViewById(R.id.input_conpass);

        btn_chan_pw = (Button)findViewById(R.id.btn_updatepass);

        btn_chan_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_old_pw.getText().toString().equals("")){
                    alert.showAlertDialog(getApplicationContext(), "Old password missing", "Please put your old password", false);
                }else if(ed_new_pass.getText().toString().equals("")){
                    alert.showAlertDialog(getApplicationContext(), "New password missing", "Please put your new password", false);
                }else if (ed_conform_pass.getText().toString().equals("")){
                    alert.showAlertDialog(getApplicationContext(), "Conform Password", "Conform new password", false);
                }else if(!ed_new_pass.getText().toString().equals(ed_conform_pass.getText().toString())){
                    alert.showAlertDialog(getApplicationContext(), "Conform new password", "Confirm your password again..", false);
                }else {
                    str_old_pass = ed_old_pw.getText().toString();
                    str_new_pass = ed_new_pass.getText().toString();

                    if (new CheckNetwork(ChangePassword.this).isNetworkAvailable()) {
                        new changepassword(str_old_pass,str_new_pass,str_token).execute();
                    }else {
                        alert.showAlertDialog(getApplicationContext(), "Connection error", "Please check your internet connection", false);
                    }
                }
            }
        });
    }

    public class changepassword extends AsyncTask<String, Void, String> {

        String str_old_pass,str_new_pass,str_rec_token;
        public changepassword(String str_old_pass, String str_new_pass, String str_token) {

            this.str_old_pass = str_old_pass;
            this.str_new_pass = str_new_pass;
            str_rec_token = str_token;
            Log.d("STR_REC_COMM",str_old_pass);
            Log.d("STR_REC_QID",str_new_pass);
            Log.d("STR_REC_TOKEN",str_rec_token);
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
                    jsonObject.put("oldpassword", str_old_pass);
                    jsonObject.put("newpassword", str_new_pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestBody = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(Constant.CHANGE_PASSWORD, requestBody, str_rec_token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("AFTERRESONSEFROMCOMM",s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                if (status.equals("success")){
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Signinscreen.class));
                }else {
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }




}
