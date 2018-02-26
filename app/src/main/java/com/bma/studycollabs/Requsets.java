package com.bma.studycollabs;

/**
 * Created by NANTHA on 11/8/2017.
 */

public class Requsets {
    private String id;
    private String uid;
    private String title;
    private String sdate;
    private String stime;
    private String etime;
    private String atype;
    private String status;
    private String location;

    public void setuid(String uid) {
        this.uid = uid;
    }

    public String getuid(){
        return uid;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String gettitle(){
        return title;
    }

    public void setsdate(String sdate) {
        this.sdate = sdate;
    }

    public String getdate(){
        return sdate;
    }

    public void setstime(String stime) {
        this.stime = stime;
    }

    public String getstime(){
        return stime;
    }

    public void setetime(String etime) {
        this.etime = etime;
    }

    public String getetime(){
        return etime;
    }

    public void setatype(String atype) {
        this.atype = atype;
    }

    public String getAtype(){
        return atype;
    }

    public void setstatus(String status) {
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    public void setlocation(String location) {
        this.location = location;
    }

    public String getLocation(){
        return location;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getid() {
        return id;
    }
}
