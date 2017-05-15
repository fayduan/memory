package cn.duanyufei.memory;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.duanyufei.db.DBDao;
import cn.duanyufei.model.Memory;

public class ConfigActivity extends AppCompatActivity {

    private int mAppWidgetId;
    private List<Memory> ml;
    private DBDao dao;
    private ListView lv;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(itemListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.select);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        if (extra != null) {
            mAppWidgetId = extra.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        dao = DBDao.getInstance();
        ml = dao.findAllMemory();
        lv.setAdapter(new MyAdapter());
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
            View view = View.inflate(ConfigActivity.this, R.layout.list_memory, null);
            TextView tv_text = (TextView) view.findViewById(R.id.tv_item_text);
            tv_text.setText(memory.getText());
            TextView tv_number = (TextView) view.findViewById(R.id.tv_item_number);
            tv_number.setText(memory.getNumber() + "");
            if (memory.getType() == 0) {
                tv_number.setTextColor(getResources().getColor(R.color.red));
            }
            return view;

        }

    }

    OnItemClickListener itemListener = new OnItemClickListener() {
        //���
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            long mID = ml.get(position).getId();
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

    public static void saveID(Context context, int appWidgetId, long mID) {
        SharedPreferences sp = context.getSharedPreferences("mid", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("awmid_" + appWidgetId, mID);
        editor.commit();

        SharedPreferences sp1 = context.getSharedPreferences("awmid", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sp1.edit();
        editor1.putInt("mid_" + mID, appWidgetId);
        editor1.commit();
    }

    public static int getID(Context context, int appWidgetId) {
        SharedPreferences sp = context.getSharedPreferences("mid", Activity.MODE_PRIVATE);
        return sp.getInt("awmid_" + appWidgetId, -1);
    }

    public static int getAwID(Context context, long mId) {
        SharedPreferences sp = context.getSharedPreferences("awmid", Activity.MODE_PRIVATE);
        return sp.getInt("mid_" + mId, -1);
    }
}
