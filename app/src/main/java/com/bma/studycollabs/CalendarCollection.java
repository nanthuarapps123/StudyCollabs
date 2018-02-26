package com.bma.studycollabs;
import java.util.ArrayList;
public class CalendarCollection {
	public String id = "";
	public String date="";
	public String event_message="";
	public String start="";
	public String end="";
	public  String color = "";
	
	public static ArrayList<CalendarCollection> date_collection_arr;
	public CalendarCollection(String date, String event_message,String id,String start,String end,String color){
		this.id = id;
		this.date=date;	
		this.event_message=event_message;
		this.start = start;
		this.end = end;
		this.color = color;
	}
}
