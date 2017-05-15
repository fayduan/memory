package cn.duanyufei.memory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import cn.duanyufei.db.DBDao;
import cn.duanyufei.model.Motion;
import cn.duanyufei.model.Record;

public class AddRecordActivity extends AppCompatActivity {

    private EditText txtWeight;
    private DatePicker dp;
    private DBDao dao;
    private Calendar calendar;
    private Context context;
    private Record record;
    private long motionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        dao = DBDao.getInstance();
        Intent get = getIntent();
        record = (Record) get.getSerializableExtra("record");


        setContentView(R.layout.activity_add_record);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRecordActivity.this.finish();
            }
        });

        txtWeight = (EditText) findViewById(R.id.txt_weight);
        dp = (DatePicker) findViewById(R.id.add_dp);
        calendar = Calendar.getInstance();
        if (record == null) {
            motionId = getIntent().getLongExtra("motionId", -1L);
            if (motionId < 0) finish();
        } else {
            txtWeight.setText(record.getWeight() + "");
            calendar.setTime(record.getDate());
            Motion motion = dao.findMotion(record.getMotionId());
            if (motion != null) {
                getSupportActionBar().setTitle(motion.getText() + " " + motion.getGroups() + "×" + motion.getNumber());
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dp.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {

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
                String text = txtWeight.getText().toString();
                if (!text.isEmpty()) {
                    int weight = Integer.parseInt(text);
                    if (record == null) {

                        if (motionId > 0) {
                            dao.addRecord(new Record(motionId, calendar.getTime(), weight));
                        }
                    } else {
                        record.setWeight(weight);
                        record.setDate(calendar.getTime());
                        dao.updateRecord(record);
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
