package cn.duanyufei.memory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.duanyufei.app.MApplication;
import cn.duanyufei.db.DBDao;
import cn.duanyufei.model.Memory;
import cn.duanyufei.util.UpdateTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int DEL_TAG = 0;

    private List<Memory> ml;
    //private List<Integer> selected;
    private DBDao dao;
    private ListView lv;
    private int delid;
    private MyAdapter adapter;
    private FloatingActionButton fab;
    private Snackbar snackBar;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delid = 0;
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(itemListener);
        lv.setOnItemLongClickListener(itemLongListener);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent();
                addIntent.setClass(MainActivity.this, AddActivity.class);
                MainActivity.this.startActivity(addIntent);
            }
        });

        dao = new DBDao(MainActivity.this);
        ml = dao.findAll();
        adapter = new MyAdapter();
        lv.setAdapter(adapter);

        snackBar = Snackbar.make(fab, R.string.msg_nolist, Snackbar.LENGTH_INDEFINITE).setAction(R.string.button_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        });

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == DEL_TAG) {
                    init();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_settings:
                Toast.makeText(getApplicationContext(), "设置还没做,嘿嘿嘿~", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_teach:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("教笨蛋")
                        .setMessage("1.点击右下角加号添加纪念日。" + '\n'
                                + "2.如果添加的是还没到的日子,天数是红色的。" + '\n'
                                + "3.长按项目删除。" + '\n'
                                + "4.点击项目编辑。" + '\n'
                        )
                        .setPositiveButton("好的吧", null)
                        .show();
                break;
            case R.id.menu_update:
                new UpdateTask(this).update();
                break;
            case R.id.menu_aboutus:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("关于我")
                        .setMessage("我就是小段呗~" + '\n' + "版本:" + MApplication.getInstance().getVersion())
                        .setPositiveButton("好的吧", null)
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        dao = new DBDao(MainActivity.this);
        ml = dao.findAll();
        if (ml.size() == 0) {
            snackBar.show();
        }
        adapter.notifyDataSetChanged();
    }

    class MyAdapter extends BaseAdapter {

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
            View view = View.inflate(MainActivity.this, R.layout.list_item, null);
            TextView tv_text = (TextView) view.findViewById(R.id.tv_item_text);
            tv_text.setText(memory.getText());
            TextView tv_number = (TextView) view.findViewById(R.id.tv_item_number);
            tv_number.setText(memory.getNumber() + "");
            if (memory.getType() == 0) {
                tv_number.setTextColor(getResources().getColor(R.color.red));
            }
            Log.i(TAG, "getView: " + memory.getText());
            return view;
            /*
            ViewHolder holder = null;
            if (arg1 == null) {  
            // 获得ViewHolder对象  
            holder = new ViewHolder();  
                // 导入布局并赋值给convertview  
            arg1 = View.inflate(MainActivity.this , R.layout.list_item , null);
            holder.tv = (TextView) arg1.findViewById(R.id.tv_item_number);  
            holder.cb = (CheckBox) arg1.findViewById(R.id.lv_cb);  
            // 为view设置标签  
            arg1.setTag(holder);  
            } else {  
            // 取出holder  
            	holder = (ViewHolder) arg1.getTag();  
            }  
  
  
            // 设置list中TextView的显示  
	        holder.tv.setText(ml.get(arg0));  
	        // 根据isSelected来设置checkbox的选中状况  
	        holder.cb.setChecked(isSelected.get(arg0));  
	        return arg1; 
	        */
        }

    }

    OnItemClickListener itemListener = new OnItemClickListener() {
        //点击
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // 这里的view是我们在list.xml中定义的LinearLayout对象.  
            // 所以可以通过findViewById方法可以找到list.xml中定义的它的子对象,如下:  
            //Memory m = ml.get(position);
            //Toast.makeText(getApplicationContext(), m.toString(), Toast.LENGTH_SHORT).show();
            Intent changeIntent = new Intent();
            changeIntent.setClass(MainActivity.this, AddActivity.class);
            changeIntent.putExtra("id", ml.get(position).getId());
            MainActivity.this.startActivity(changeIntent);
        }
    };
    OnItemLongClickListener itemLongListener = new OnItemLongClickListener() {
        //长按
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            // TODO Auto-generated method stub
            Memory m = ml.get(arg2);
            delid = m.getId();
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("某人要弹出来的")
                    .setMessage("是手抖不？")
                    .setPositiveButton("嗯嗯是的", del_cancel)
                    .setNegativeButton("显然不是", del_ok)
                    .show();
            return true;
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
            Toast.makeText(getApplicationContext(), "手残了吧...", Toast.LENGTH_SHORT).show();
        }
    };
    public OnClickListener del_ok = new OnClickListener() {

        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            Toast.makeText(getApplicationContext(), "已删除纪念日", Toast.LENGTH_SHORT).show();
            delete(delid);
            init();
        }
    };

    private void delete(final int delId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.delete(delId);
                Message msg = Message.obtain();
                msg.obj = null;
                msg.what = DEL_TAG;
                handler.sendMessage(msg);
                Log.i(TAG, "run: send delete");
            }
        }).start();
    }
}
