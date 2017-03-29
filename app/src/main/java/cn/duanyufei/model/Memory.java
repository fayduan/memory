package cn.duanyufei.model;

import java.util.Calendar;
import java.util.Date;

public class Memory {
	int id;
	String text;
	int number;
	Date date;
	int type;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Memory(int id,String text,Date date){
		this.id = id;
		this.text = text;
		this.date = new Date(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
		Calendar cal = Calendar.getInstance();
		Date date11 = cal.getTime();
		long now = cal.getTimeInMillis();
		long sub = now - this.date.getTime();
		//long temp = datetmp.getTime();
		//long sub = temp-date.getTime();
		long b =1000;
		long l=b*60*60*24; 
		this.number = (int)(sub / l);
		if(sub>=0){
			this.type=1;
			this.number ++;
		}
		if(sub<0){
			this.type=0;
			this.number=Math.abs(this.number);
			this.number++;
		}
	}
	/*
	public Memory(int id,String text,int myear,int mmonth,int mday){
		this.id = id;
		this.text = text;
    	Date datetmp = null;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	try{
    		datetmp = sdf.parse(myear+"-"+mmonth+"-"+mday);
    	}catch(Exception e){
    	}
    	date=datetmp.getTime();
		long temp = System.currentTimeMillis();
		long sub = temp-date;
		long b =1000;
		long l=b*60*60*24; 
		this.number = (int)(sub / l);
		if(sub>=0){
			this.number ++;
		}
		if(sub<0){
			this.number--;
		}
		
	}
	*/
	@Override
	public String toString() {
		return "Memory [id=" + id + ", text=" + text + ", number=" + number
				+ ", date=" + date.toString() + "]";
	}
}
