package cn.duanyufei.memory;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;

import cn.duanyufei.db.DBDao;
import cn.duanyufei.model.Memory;

public class AddActivity extends Activity {

    private LinearLayout btn_cancel;
    private LinearLayout btn_ok;
    private EditText mname;
    private DatePicker dp;
    private DBDao dao;
    private Calendar calendar;
    private int id;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        dao = new DBDao(AddActivity.this);
        Intent get = getIntent();
        id = get.getIntExtra("id", -1);

        setContentView(R.layout.activity_add);
        //btn_ok = (LinearLayout) findViewById(R.id.btn_ok);
//        btn_ok.setClickable(true);
//        btn_ok.setOnTouchListener(touch_ok);
//        btn_ok.setOnClickListener(click_ok);
        mname = (EditText) findViewById(R.id.mname);
        dp = (DatePicker) findViewById(R.id.add_dp);
        calendar = Calendar.getInstance();
        if (id != -1) {
            Memory m = dao.find(id);
            calendar.setTime(m.getDate());
            mname.setText(m.getText());
            //Toast.makeText(getApplicationContext(),calendar.toString(), Toast.LENGTH_SHORT).show();
        }
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dp.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener() {

            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                //Toast.makeText(getApplicationContext(), year+"."+(monthOfYear+1)+"."+dayOfMonth, Toast.LENGTH_SHORT).show();
                calendar.set(year, monthOfYear, dayOfMonth);
            }

        });

    }

    public OnClickListener click_cancel = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "????,??????...", Toast.LENGTH_SHORT).show();
            backMain();
        }
    };
    public OnClickListener click_ok = new OnClickListener() {

        @Override
        public void onClick(View v) {
            //
            String text = mname.getText().toString();
            if (!text.isEmpty()) {
                if (id != -1) {
                    //Toast.makeText(getApplicationContext(), id+text+calendar.toString(), Toast.LENGTH_SHORT).show();
                    dao.update(id, text, calendar.getTime());
                    int appWidgetId = ConfigActivity.getAwID(context, id);
                    AppWidgetManager awm = AppWidgetManager.getInstance(context);
                    MyAppWidgetProvider.sendMsg(context, awm, appWidgetId, id);
                    Log.i("minor", "1sendmsg,appWidgetID=" + appWidgetId + ",MID=" + id);
                } else {
                    dao.add(text, calendar.getTime());
                }
                backMain();
            } else {
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public OnTouchListener touch_cancel = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                btn_cancel.setBackgroundColor(getResources().getColor(R.color.background_lightdark));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                btn_cancel.setBackgroundColor(getResources().getColor(R.color.background_dark));
            }
            return false;
        }
    };

    public OnTouchListener touch_ok = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                btn_ok.setBackgroundColor(getResources().getColor(R.color.background_lightdark));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                btn_ok.setBackgroundColor(getResources().getColor(R.color.background_dark));
            }
            return false;
        }
    };

    private void backMain() {
        Intent mainIntent = new Intent();
        mainIntent.setClass(AddActivity.this, MainActivity.class);
        AddActivity.this.startActivity(mainIntent);
        AddActivity.this.finish();
    }
}
