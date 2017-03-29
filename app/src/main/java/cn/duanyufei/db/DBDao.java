package com.minor.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.minor.model.Memory;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBDao {
	private DBSQLiteOpenHelper helper;
	private Calendar cal;
	
	public DBDao(Context context){
		helper = new DBSQLiteOpenHelper(context);
		cal = Calendar.getInstance();
	}
	
	public void add(String text,Date date){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("insert into memory (text,mdate) values(?,?)",
				new Object[]{text,date.getTime()});
		//System.out.println("After:"+df.format(date));
		db.close();
	}
	
	public Memory find(int id){
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from memory where id=?",new String[]{id+""});
		boolean result = cursor.moveToNext();
		//int id = cursor.getInt(cursor.getColumnIndex("id"));
		String text = cursor.getString(cursor.getColumnIndex("text"));
		long mdate  = cursor.getLong(cursor.getColumnIndex("mdate"));
		cal.setTimeInMillis(mdate);
		Memory m = new Memory(id,text,cal.getTime());
		cursor.close();
		db.close();
		return m;
	}
	public void update(int id,String text,Date date){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("update memory set text=?,mdate=? where id=? ", new Object[]{text,date.getTime(),id});
		db.close();
	}
	
	public void delete(int id){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from memory where id=? ", new Object[]{id});
		db.close();
	}
	
	public List<Memory> findAll(){
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from memory order by mdate desc",null);
		List<Memory> list = new ArrayList<Memory>();
		while(cursor.moveToNext()){
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String text = cursor.getString(cursor.getColumnIndex("text"));
			long mdate  = cursor.getLong(cursor.getColumnIndex("mdate"));
			cal.setTimeInMillis(mdate);
			Memory m = new Memory(id,text,cal.getTime());
			list.add(m);
		}
		cursor.close();
		db.close();
		return list;
	}
	public List<Memory> findAllBefore(){
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from memory where mdate<=strftime('%Y-%m-%d') order by mdate desc",null);
		List<Memory> list = new ArrayList<Memory>();
		while(cursor.moveToNext()){
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String text = cursor.getString(cursor.getColumnIndex("text"));
			long mdate  = cursor.getLong(cursor.getColumnIndex("mdate"));
			cal.setTimeInMillis(mdate);
			Memory m = new Memory(id,text,cal.getTime());
			list.add(m);
		}
		cursor.close();
		db.close();
		return list;
	}
	public List<Memory> findAllAfter(){
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from memory where mdate>strftime('%Y-%m-%d') order by mdate",null);
		List<Memory> list = new ArrayList<Memory>();
		while(cursor.moveToNext()){
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String text = cursor.getString(cursor.getColumnIndex("text"));
			long mdate  = cursor.getLong(cursor.getColumnIndex("mdate"));
			cal.setTimeInMillis(mdate);
			Memory m = new Memory(id,text,cal.getTime());
			list.add(m);
		}
		cursor.close();
		db.close();
		return list;
	}
}
