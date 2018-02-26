package com.bma.studycollabs;

/**
 * Created by NANTHA on 11/20/2017.
 */

public class NotiTutor {
    private String id;
    private String title;
    private String cat;
    private String scat;
    private String date;

    public void setid(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getTitle() {
        return title;
    }

    public void setcat(String cat) {
        this.cat = cat;
    }

    public String getcat() {
        return cat;
    }

    public void setscat(String scat) {
        this.scat = scat;
    }

    public String getscat() {
        return scat;
    }

    public void setDate(String des) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

}
