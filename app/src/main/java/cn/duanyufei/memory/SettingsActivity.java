package cn.duanyufei.memory;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import java.util.List;

import cn.duanyufei.db.DBDao;
import cn.duanyufei.model.Memory;
import cn.duanyufei.util.StorageUtil;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup colorGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });

        colorGroup = (RadioGroup) findViewById(R.id.rdo_color);
        if (StorageUtil.getColor() == 0) {
            colorGroup.check(R.id.rdo_color_white);
        } else {
            colorGroup.check(R.id.rdo_color_black);
        }

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
                if (colorGroup.getCheckedRadioButtonId() == R.id.rdo_color_white) {
                    StorageUtil.setColor(0);
                } else {
                    StorageUtil.setColor(1);
                }
                updateWidget(this);
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void updateWidget(Context context) {
        DBDao dao = DBDao.getInstance();
        List<Memory> list = dao.findAllMemory();
        for (int i = 0; i < list.size(); i++) {
            int appWidgetId = ConfigActivity.getAwID(context, list.get(i).getId());
            if (appWidgetId > 0) {
                AppWidgetManager awm = AppWidgetManager.getInstance(context);
                MyAppWidgetProvider.sendMsg(context, awm, appWidgetId, list.get(i).getId());
            }

        }

    }
}
