package cn.duanyufei.memory;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import cn.duanyufei.db.DBDao;
import cn.duanyufei.model.Memory;

public class AddActivity extends AppCompatActivity {

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddActivity.this.finish();
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
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
                    this.finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.empty, Toast.LENGTH_SHORT).show();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
