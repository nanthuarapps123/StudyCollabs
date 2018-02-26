package com.bma.studycollabs;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bma.boommenu.BoomButtons.OnBMClickListener;
import com.bma.boommenu.BoomButtons.SimpleCircleButton;
import com.bma.boommenu.BoomMenuButton;
import com.inscripts.interfaces.Callbacks;
import com.inscripts.interfaces.LaunchCallbacks;

import org.json.JSONObject;

import java.util.HashMap;

import cometchat.inscripts.com.cometchatcore.coresdk.CometChat;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Maintwo extends AppCompatActivity {
    AlertDialogManager alert = new AlertDialogManager();
    String username,password;
    String str_token,str_user,str_fname,str_lname,str_ruser,str_imgpath;
    String str_pass_tok;
    String str_pass,str_login;
    private ProgressDialog pDialog;
    CometChat cometChat;
    private Boolean exit = false;
    Boolean isloginn = false;
//    private static final java.lang.String TAG = MainActivity.class.getSimpleName();
    private final int PERMISSION_LAUNCH_COMETCHAT = 11;
    String siteUrl,licenseKey,apiKey;
    Callbacks callbacksss;
    String cusername,cpassword;
    ImageView img_vol_hrs;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_TOKEN = "keytoken";
    private static final String KEY_UNAME = "keyuname";
    private static final String KEY_PASS = "keypass";
    private static final String KEY_LOGN = "keylogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintwo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = sp.edit();
        str_pass_tok = sp.getString(KEY_TOKEN, null);
        str_user = sp.getString(KEY_UNAME, null);
        str_pass = sp.getString(KEY_PASS, null);
        isloginn = sp.getBoolean(KEY_LOGN,false);


        if (!isLoggedIn()){
         startActivity(new Intent(getApplicationContext(),Signinscreen.class));
        }

        str_pass_tok = new StringBuilder(650).append("Bearer").append(" ").append(str_pass_tok).toString();

        BoomMenuButton bmb3 = (BoomMenuButton) findViewById(R.id.bmb3);
        for (int i = 0; i < bmb3.getPiecePlaceEnum().pieceNumber(); i++) {
            bmb3.addBuilder(BuilderManager.getTextOutsideCircleButtonBuilderWithDifferentPieceColor().listener(new OnBMClickListener() {
                @Override
                public void onBoomButtonClick(int index) {
                    if (index==0){
                        Intent in_se_thr = new Intent(getApplicationContext(),Searchthread.class);
                        Bundle bu_se_thr = new Bundle();
                        bu_se_thr.putString("toksept",str_pass_tok);
                        in_se_thr.putExtras(bu_se_thr);
                        startActivity(in_se_thr);
                    }else if (index==1){
                        Intent in_po_thr = new Intent(getApplicationContext(),PostThread.class);
                        Bundle bu_po_thr = new Bundle();
                        bu_po_thr.putString("toksept",str_pass_tok);
                        in_po_thr.putExtras(bu_po_thr);
                        startActivity(in_po_thr);
                    }else if (index==2){
                        Intent in_fi_tu = new Intent(getApplicationContext(),FindTutor.class);
                        Bundle bu_fi_tu = new Bundle();
                        bu_fi_tu.putString("toksept",str_pass_tok);
                        bu_fi_tu.putString("str_uname",str_user);
                        in_fi_tu.putExtras(bu_fi_tu);
                        startActivity(in_fi_tu);
                    }else if (index==3){
                        Intent in_sub_ed = new Intent(getApplicationContext(),NewSubjectEdit.class);
                        Bundle bu_sub_ed = new Bundle();
                        bu_sub_ed.putString("toksese",str_pass_tok);
                        in_sub_ed.putExtras(bu_sub_ed);
                        startActivity(in_sub_ed);
                    }else if (index==4){
                        Intent in_vol_hes = new Intent(getApplicationContext(),Volunteerhr.class);
                        Bundle bu_vol_hrs = new Bundle();
                        bu_vol_hrs.putString("toksept",str_pass_tok);
                        in_vol_hes.putExtras(bu_vol_hrs);
                        startActivity(in_vol_hes);
                    }else if (index==5){
                            Intent in_my_cal = new Intent(getApplicationContext(),MyCalender.class);
                            Bundle bu_my_cal = new Bundle();
                            bu_my_cal.putString("toksept",str_pass_tok);
                            in_my_cal.putExtras(bu_my_cal);
                            startActivity(in_my_cal);
                    }else if (index==6){
                        Intent intent_reqsch = new Intent(getApplicationContext(),RequestSchedue.class);
                        Bundle bu_re_sc = new Bundle();
                        bu_re_sc.putString("str_tok",str_pass_tok);
                        intent_reqsch.putExtras(bu_re_sc);
                        startActivity(intent_reqsch);
                    }else if (index==7){
                        new GetContacts().execute();
                    }else if (index==8){
                        Intent in_res_sc = new Intent(getApplicationContext(),Resourcescreen.class);
                        Bundle bu_res_sc = new Bundle();
                        bu_res_sc.putString("toksept",str_pass_tok);
                        in_res_sc.putExtras(bu_res_sc);
                        startActivity(in_res_sc);
                    }
                }

            }));
        }

        BoomMenuButton bmb4 = (BoomMenuButton) findViewById(R.id.bmb4);
        for (int i = 0; i < bmb4.getPiecePlaceEnum().pieceNumber(); i++)
            bmb4.addBuilder(BuilderManager.getPieceCornerRadiusHamButtonBuilder().listener(new OnBMClickListener() {
                @Override
                public void onBoomButtonClick(int index) {
                    if (index==0){

                    }else if (index==1){
                        Intent in_pro_screen = new Intent(getApplicationContext(),Profilescreen.class);
                        Bundle bu_pro_screen = new Bundle();
                        bu_pro_screen.putString("toksept",str_pass_tok);
                        bu_pro_screen.putString("usertopro",str_user);
                        bu_pro_screen.putString("imgtopro",str_imgpath);
                        in_pro_screen.putExtras(bu_pro_screen);
                        startActivity(in_pro_screen);
                    }else if (index==2){
//                        Intent in_not_sc = new Intent(getApplicationContext(),Notification.class);
                        Intent in_not_sc = new Intent(getApplicationContext(),TabNotifi.class);
                        Bundle bu_not_sc = new Bundle();
                        bu_not_sc.putString("toksept",str_pass_tok);
                        in_not_sc.putExtras(bu_not_sc);
                        startActivity(in_not_sc);
                    }else if (index==3){
                        logoutUser();
                    }
                }
            }));
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkAndLaunchCometchat() {
        cometChat.initializeCometChat(siteUrl, licenseKey, apiKey, false, new Callbacks() {
            @Override
            public void successCallback(JSONObject jsonObject) {
                Log.d("Step3","successs");
                cometChat.login(username, password, new Callbacks() {
                    @Override
                    public void successCallback(JSONObject jsonObject) {
                        Log.d("Step4","successs");

                        cometChat.launchCometChat(Maintwo.this, false, new LaunchCallbacks() {
                            @Override
                            public void successCallback(JSONObject jsonObject)
                            {
                                Log.d("Step5","successs");
                            }
                            @Override
                            public void failCallback(JSONObject jsonObject)
                            {
                                Log.e("Step5","fail");
                            }
                            @Override
                            public void userInfoCallback(JSONObject jsonObject)
                            {
                                Log.d("Step5",jsonObject.toString());
                            }
                            @Override
                            public void chatroomInfoCallback(JSONObject jsonObject)
                            {
                                Log.d("Step5","chatroominfo");
                            }
                            @Override
                            public void onMessageReceive(JSONObject jsonObject)
                            {
                                Log.d("Step5","onmessagereceive");
                            }
                            @Override
                            public void error(JSONObject jsonObject)
                            {
                                Log.e("Step5","error");
                            }
                            @Override
                            public void onLogout()
                            {
                                Log.d("Step5","successs");
                            }
                        });
                    }
                    @Override
                    public void failCallback(JSONObject jsonObject)
                    {
                        Log.e("Step4","fail");
                    }
                });
            }
            @Override
            public void failCallback(JSONObject jsonObject)
            {
                Log.e("Step3","fail");
            }
        });
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Maintwo.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            siteUrl = "http://studycollab.com/chat";
            licenseKey = "3ZKUF-8ATZR-4KLXK-0GUAM-9XKDZ";
            apiKey ="c495a59c0fd768769401d075b02e5e08";
            username = str_user;
            password = str_pass;
            String[] PERMISSIONS = {android.Manifest.permission.READ_PHONE_STATE};
            if (hasPermissions(Maintwo.this, PERMISSIONS)) {
                Log.d("Step1","successs");
                cometChat = CometChat.getInstance(Maintwo.this);
                checkAndLaunchCometchat();
            } else {
                Log.e("Step2","fail");
                ActivityCompat.requestPermissions(Maintwo.this, PERMISSIONS, PERMISSION_LAUNCH_COMETCHAT);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                    System.exit(0);
                }
            }, 3 * 1000);
        }

    }

    public boolean 	isLoggedIn(){
        return sp.getBoolean(KEY_LOGN, FALSE);
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(getApplicationContext(), Signinscreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
