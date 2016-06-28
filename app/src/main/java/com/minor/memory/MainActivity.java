package com.minor.memory;

import java.util.ArrayList;
import java.util.List;

import com.minor.db.DBDao;
import com.minor.model.Memory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.DialogInterface.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private List<Memory> ml;
	//private List<Integer> selected;
	private DBDao dao;
	private ListView lv;
	private TextView tv;
	private int delid;
	//private static HashMap<Integer,Boolean> isSelected;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		delid=0;
		setContentView(R.layout.activity_main);
		lv = (ListView)findViewById(R.id.list);
		tv = (TextView)findViewById(R.id.msg_main);
		lv.setOnItemClickListener(itemListener);
		lv.setOnItemLongClickListener(itemLongListener);
		//lv.setOnItemSelectedListener(itemSelectedListener);
		
		//selected = new ArrayList<Integer>();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_add) {
			Intent addIntent = new Intent();
			addIntent.setClass(MainActivity.this, AddActivity.class);
			MainActivity.this.startActivity(addIntent);
		}
		if(id == R.id.menu_settings){
			Toast.makeText(getApplicationContext(), "���û�û��,�ٺٺ�~", Toast.LENGTH_SHORT).show();
		}
		if(id == R.id.menu_teach){
			new AlertDialog.Builder(MainActivity.this)
				.setTitle("�̱���")
				.setMessage("1.�������Ǹ�\"+\"����Ӽ������õġ�"+'\n'
						+"2.�����ӵ��ǻ�û��������,�����Ǻ�ɫ�ġ�"+'\n'
						+"3.������Ŀɾ����"+'\n'
						+"4.�����Ŀ�༭��"+'\n'
						)
						.setPositiveButton("�õİ�", null)
						.show();
		}
		if(id == R.id.menu_aboutus){
			new AlertDialog.Builder(MainActivity.this)
				.setTitle("������")
				.setMessage("�Ҿ���С����~"+'\n'+"�汾:"+getResources().getString(R.string.version)+"(���԰汾)"+'\n'+"����:2015/1/9")
				.setPositiveButton("�õİ�", null)
				.show();
		}
		return super.onOptionsItemSelected(item);
	}

	private void Init(){
		dao = new DBDao(MainActivity.this);
		ml = dao.findAll();
		if(ml.size()==0){
			tv.setVisibility(View.VISIBLE);
			lv.setVisibility(View.INVISIBLE);
			tv.setText(R.string.msg_nolist);
		}else {
			//isSelected = new HashMap<Integer, Boolean>();
			//for(int i=0; i<ml.size();i++) {  
	        //    isSelected.put(i,false);
	        //}  
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
			View view = View.inflate(MainActivity.this , R.layout.list_item , null);
			TextView tv_text = (TextView) view.findViewById(R.id.tv_item_text);
			tv_text.setText(memory.getText());
			TextView tv_number = (TextView) view.findViewById(R.id.tv_item_number);
			tv_number.setText(memory.getNumber()+"");
			if(memory.getType()==0){
				tv_number.setTextColor(getResources().getColor(R.color.red));
			}
			return view;
			/*
			ViewHolder holder = null;  
            if (arg1 == null) {  
            // ���ViewHolder����  
            holder = new ViewHolder();  
                // ���벼�ֲ���ֵ��convertview  
            arg1 = View.inflate(MainActivity.this , R.layout.list_item , null);
            holder.tv = (TextView) arg1.findViewById(R.id.tv_item_number);  
            holder.cb = (CheckBox) arg1.findViewById(R.id.lv_cb);  
            // Ϊview���ñ�ǩ  
            arg1.setTag(holder);  
            } else {  
            // ȡ��holder  
            	holder = (ViewHolder) arg1.getTag();  
            }  
  
  
            // ����list��TextView����ʾ  
	        holder.tv.setText(ml.get(arg0));  
	        // ����isSelected������checkbox��ѡ��״��  
	        holder.cb.setChecked(isSelected.get(arg0));  
	        return arg1; 
	        */
		}
		
	}
	OnItemClickListener itemListener = new OnItemClickListener() {  
		//���
        @Override  
        public void onItemClick(AdapterView<?> parent, View view, int position,  
                long id) {  
            // �����view��������list.xml�ж����LinearLayout����.  
            // ���Կ���ͨ��findViewById���������ҵ�list.xml�ж���������Ӷ���,����:  
        	//Memory m = ml.get(position);
        	//Toast.makeText(getApplicationContext(), m.toString(), Toast.LENGTH_SHORT).show();
        	Intent changeIntent = new Intent();
        	changeIntent.setClass(MainActivity.this, AddActivity.class);
        	changeIntent.putExtra("id", ml.get(position).getId());
        	MainActivity.this.startActivity(changeIntent);
        }  
    };
    OnItemLongClickListener itemLongListener = new OnItemLongClickListener() {
    	//����
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			Memory m = ml.get(arg2);
			delid = m.getId();
			new AlertDialog.Builder(MainActivity.this)   
				.setTitle("ĳ��Ҫ��������")  
				.setMessage("���ֶ�����")  
				.setPositiveButton("�����ǵ�", del_cancel)  
				.setNegativeButton("��Ȼ����", del_ok)  
				.show();  
			return false;
		}
    	
	};
	/*
	OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			selected.add(((Memory)ml.get(arg2)).getId());
			String temp="";
			for(int i=0;i<selected.size();i++){
				temp+=selected.get(i).toString();
				temp+=",";
			}
			Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			//selected.remove(((Memory)ml.get(arg2)).getId());
			
		}
		
	};
	*/
	public OnClickListener del_cancel = new OnClickListener() {
		

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "�ֲ��˰�...", Toast.LENGTH_SHORT).show();
		}
	};
	public OnClickListener del_ok = new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "��ɾ��������", Toast.LENGTH_SHORT).show();
			dao.delete(delid);
			Init();
		}
	};
}
