package com.bma.studycollabs;

import android.util.Log;

/**
 * Created by NANTHA on 11/1/2017.
 */

public class Tutor {

    private String str_user_id;
    private String str_ttype;
    private String str_cat;
    private String str_imgpath;

    public String getuser_id(){
        return str_user_id;
    }

    public void setuser_id(String str_user_id) {
        Log.d("USERIDD",str_user_id);
        this.str_user_id = str_user_id;
    }

    public String getttype(){
        Log.d("TTYPEE",str_ttype);
        return str_ttype;
    }

    public void setttype(String str_ttype) {
        Log.d("STRRTYPE",str_ttype);
        this.str_ttype = str_ttype;
    }

    public String getcat(){
        Log.d("CATTT",str_cat);
        return str_cat;
    }

    public void setcat(String str_cat) {
        this.str_cat = str_cat;
    }

    public String getimgurl(){
        return str_imgpath;
    }

    public void setimgurl(String str_imgpath) {
        this.str_imgpath = str_imgpath;
    }
}
