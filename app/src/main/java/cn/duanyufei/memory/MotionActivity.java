package cn.duanyufei.memory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.duanyufei.db.DBDao;
import cn.duanyufei.model.Motion;
import cn.duanyufei.util.UpdateTask;

public class MotionActivity extends AppCompatActivity {
    
    final static String TAG = "MotionActivity";

    private static final int DEL_TAG = 0;

    private List<Motion> ml;
    private DBDao dao;
    private ListView lv;
    private long delid;
    private MyAdapter adapter;
    private FloatingActionButton fab;
    private Snackbar snackBar;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delid = 0;
        setContentView(R.layout.activity_motion);

        new UpdateTask(this).update();

        Intent intent = new Intent(this, AppWidgetService.class);
        startService(intent);
        SettingsActivity.updateWidget(this);

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
//                addIntent.setClass(MainActivity.this, AddActivity.class);
//                MainActivity.this.startActivity(addIntent);
            }
        });

        dao = DBDao.getInstance();
//        ml = dao.f();
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
//                    ml = dao.findAllMemory();
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        ml = dao.findAllMotion();
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
//            Memory memory = ml.get(arg0);
            View view = View.inflate(MotionActivity.this, R.layout.list_memory, null);
//            TextView tv_text = (TextView) view.findViewById(R.id.tv_item_text);
//            tv_text.setText(memory.getText());
//            TextView tv_number = (TextView) view.findViewById(R.id.tv_item_number);
//            tv_number.setText(memory.getNumber() + "");
//            if (memory.getType() == 0) {
//                tv_number.setTextColor(getResources().getColor(R.color.red));
//            }
//            Log.i(TAG, "getView: " + memory.getText());
            return view;
        }

    }

    AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        //点击
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Intent changeIntent = new Intent();
//            changeIntent.setClass(MainActivity.this, AddActivity.class);
//            changeIntent.putExtra("id", ml.get(position).getId());
//            MainActivity.this.startActivity(changeIntent);
        }
    };
    AdapterView.OnItemLongClickListener itemLongListener = new AdapterView.OnItemLongClickListener() {
        //长按
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            // TODO Auto-generated method stub
//            Memory m = ml.get(arg2);
//            delid = m.getId();
            new AlertDialog.Builder(MotionActivity.this)
                    .setTitle("某人要弹出来的")
                    .setMessage("是手抖不？")
                    .setPositiveButton("嗯嗯是的", del_cancel)
                    .setNegativeButton("显然不是", del_ok)
                    .show();
            return true;
        }

    };

    public DialogInterface.OnClickListener del_cancel = new DialogInterface.OnClickListener() {


        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            Toast.makeText(getApplicationContext(), "手残了吧...", Toast.LENGTH_SHORT).show();
        }
    };
    public DialogInterface.OnClickListener del_ok = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            Toast.makeText(getApplicationContext(), "已删除", Toast.LENGTH_SHORT).show();
            delete(delid);
        }
    };

    private void delete(final long delId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.deleteMemory(delId);
                Message msg = Message.obtain();
                msg.obj = null;
                msg.what = DEL_TAG;
                handler.sendMessage(msg);
                Log.i(TAG, "run: send deleteMemory");
            }
        }).start();
    }
}
