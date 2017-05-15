package cn.duanyufei.memory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.duanyufei.db.DBDao;
import cn.duanyufei.model.Motion;
import cn.duanyufei.model.Record;
import cn.duanyufei.util.DateUtil;

public class RecordActivity extends AppCompatActivity {

    final static String TAG = "RecordActivity";

    private static final int DEL_TAG = 0;

    private List<Record> ml;
    private DBDao dao;
    private ListView lv;
    private long delid;
    private MyAdapter adapter;
    private FloatingActionButton fab;
    private Handler handler;

    Motion motion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delid = 0;
        setContentView(R.layout.activity_record);
        Intent get = getIntent();
        motion = (Motion) get.getSerializableExtra("motion");
        if (motion == null) {
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(motion.getText() + " " + motion.getGroups() + "×" + motion.getNumber());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordActivity.this.finish();
            }
        });

        lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(itemListener);
        lv.setOnItemLongClickListener(itemLongListener);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordActivity.this, AddRecordActivity.class);
                intent.putExtra("motionId", motion.getId());
                RecordActivity.this.startActivity(intent);
            }
        });

        dao = DBDao.getInstance();
        ml = dao.findRecordByMotion(motion.getId());
        adapter = new MyAdapter();
        lv.setAdapter(adapter);

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == DEL_TAG) {
                    ml = dao.findRecordByMotion(motion.getId());
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        ml = dao.findRecordByMotion(motion.getId());
        adapter.notifyDataSetChanged();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (ml == null) {
                return 0;
            }
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
            final Record record = ml.get(arg0);
            View view = View.inflate(RecordActivity.this, R.layout.list_record, null);
            TextView txtDate = (TextView) view.findViewById(R.id.txt_date);
            TextView txtWeight = (TextView) view.findViewById(R.id.txt_weight);
            txtDate.setText(DateUtil.toShortDateString(record.getDate()));
            txtWeight.setText(record.getWeight() + "");
            return view;
        }

    }

    AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        //点击
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Intent changeIntent = new Intent();
            changeIntent.setClass(RecordActivity.this, AddRecordActivity.class);
            changeIntent.putExtra("record", ml.get(position));
            RecordActivity.this.startActivity(changeIntent);
        }
    };
    AdapterView.OnItemLongClickListener itemLongListener = new AdapterView.OnItemLongClickListener() {
        //长按
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            Record m = ml.get(arg2);
            delid = m.getId();
            new AlertDialog.Builder(RecordActivity.this)
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
            if (ml.size() == 1) {
                Toast.makeText(getApplicationContext(), "最少留一个吧", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "已删除", Toast.LENGTH_SHORT).show();
                delete(delid);
            }
        }
    };

    private void delete(final long delId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.deleteRecord(delId);
                Message msg = Message.obtain();
                msg.obj = null;
                msg.what = DEL_TAG;
                handler.sendMessage(msg);
            }
        }).start();
    }
}
