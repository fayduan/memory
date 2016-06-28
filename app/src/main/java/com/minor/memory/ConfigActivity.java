package com.minor.memory;

import java.util.List;

import com.minor.db.DBDao;
import com.minor.memory.MainActivity.MyAdapter;
import com.minor.model.Memory;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ConfigActivity extends Activity {

	private int mAppWidgetId;
	private List<Memory> ml;
	private DBDao dao;
	private ListView lv;
	private TextView tv;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_main);
		lv = (ListView)findViewById(R.id.list);
		tv = (TextView)findViewById(R.id.msg_main);
		lv.setOnItemClickListener(itemListener);
		
		Intent intent = getIntent();
		Bundle extra = intent.getExtras();
		if(extra != null){
			mAppWidgetId = extra.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Init();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Init();
	}

	private void Init(){
		dao = new DBDao(ConfigActivity.this);
		ml = dao.findAll();
		if(ml.size()==0){
			tv.setVisibility(View.VISIBLE);
			lv.setVisibility(View.INVISIBLE);
			tv.setText(R.string.msg_nolist);
		}else {
			lv.setVisibility(View.VISIBLE);
			tv.setVisibility(View.INVISIBLE);
			lv.setAdapter(new MyAdapter());
		}
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return ml.size();
		}

		@Override
		public Object getItem(int arg0) {
			return ml.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			Memory memory = ml.get(arg0);
			View view = View.inflate(ConfigActivity.this , R.layout.list_item , null);
			TextView tv_text = (TextView) view.findViewById(R.id.tv_item_text);
			tv_text.setText(memory.getText());
			TextView tv_number = (TextView) view.findViewById(R.id.tv_item_number);
			tv_number.setText(memory.getNumber()+"");
			if(memory.getType()==0){
				tv_number.setTextColor(getResources().getColor(R.color.red));
			}
			return view;

		}
		
	}
	OnItemClickListener itemListener = new OnItemClickListener() {  
		//µã»÷
        @Override  
        public void onItemClick(AdapterView<?> parent, View view, int position,  
                long id) {  
        	int mID = ml.get(position).getId();
        	saveID(context, mAppWidgetId, mID);
        	AppWidgetManager awm = AppWidgetManager.getInstance(context);
        	Intent resultIntent = new Intent();
    		resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
    		resultIntent.putExtra("id", ml.get(position).getId());
    		//send msg
    		MyAppWidgetProvider.sendMsg(context, awm, mAppWidgetId, mID);
    		
    		setResult(RESULT_OK, resultIntent);
    		finish();
        }  
    };

	public static void saveID(Context context,int appWidgetId,int mID){
		SharedPreferences sp = context.getSharedPreferences("mid", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("awmid_"+appWidgetId, mID);
		editor.commit();
		
		SharedPreferences sp1 = context.getSharedPreferences("awmid", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor1 = sp1.edit();
		editor1.putInt("mid_"+mID, appWidgetId);
		editor1.commit();
	}
	
	public static int getID(Context context,int appWidgetId){
		SharedPreferences sp = context.getSharedPreferences("mid", Activity.MODE_PRIVATE);
		return sp.getInt("awmid_"+appWidgetId, -1);
	}
	
	public static int getAwID(Context context,int mId){
		SharedPreferences sp = context.getSharedPreferences("awmid", Activity.MODE_PRIVATE);
		return sp.getInt("mid_"+mId, -1);
	}
}
