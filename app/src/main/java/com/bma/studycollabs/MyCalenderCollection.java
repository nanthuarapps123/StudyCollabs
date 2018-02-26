package com.bma.studycollabs;

import java.util.ArrayList;

/**
 * Created by NANTHA on 11/9/2017.
 */

public class MyCalenderCollection {

    public String str_id = "";
    public String str_title = "";
    public String str_schedule_datetime = "";
    public String str_start = "";
    public String str_end = "";
    public String str_color = "";

    public static ArrayList<MyCalenderCollection> my_date_collection_arr;
    public MyCalenderCollection(String str_id, String str_title,String str_schedule_datetime,String str_start,String str_end,String str_color){
        this.str_id = str_id;
        this.str_title=str_title;
        this.str_schedule_datetime=str_schedule_datetime;
        this.str_start = str_start;
        this.str_end = str_end;
        this.str_color = str_color;
    }

}
