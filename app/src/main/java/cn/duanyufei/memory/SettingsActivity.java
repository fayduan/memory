package cn.duanyufei.memory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

import cn.duanyufei.util.StorageUtil;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup colorGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
