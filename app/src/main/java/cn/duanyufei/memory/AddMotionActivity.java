package cn.duanyufei.memory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.duanyufei.db.DBDao;
import cn.duanyufei.model.Motion;
import cn.duanyufei.util.ToastUtil;

public class AddMotionActivity extends AppCompatActivity {

    private EditText txtMotion;
    private EditText txtGroup;
    private EditText txtNumber;
    private EditText txtCurWeight;
    private RadioGroup rgPos;
    private RadioButton rbChest;
    private RadioButton rbBack;
    private RadioButton rbLeg;

    private DBDao dao;
    private long id;
    private Context context;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        dao = DBDao.getInstance();
        Intent get = getIntent();
        id = get.getLongExtra("id", -1L);

        setContentView(R.layout.activity_add_motion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMotionActivity.this.finish();
            }
        });

        txtMotion = (EditText) findViewById(R.id.txt_motion);
        txtGroup = (EditText) findViewById(R.id.txt_group);
        txtNumber = (EditText) findViewById(R.id.txt_number);
        txtCurWeight = (EditText) findViewById(R.id.txt_cur_weight);
        rgPos = (RadioGroup) findViewById(R.id.rg_pos);
        rbChest = (RadioButton) findViewById(R.id.rb_chest);
        rbBack = (RadioButton) findViewById(R.id.rb_back);
        rbLeg = (RadioButton) findViewById(R.id.rb_leg);

        rgPos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_chest:
                        pos = 0;
                        break;
                    case R.id.rb_back:
                        pos = 1;
                        break;
                    case R.id.rb_leg:
                        pos = 2;
                        break;
                }
            }
        });

        if (id != -1) {
            Motion m = dao.findMotion(id);
            txtMotion.setText(m.getText());
            txtGroup.setText(m.getGroups() + "");
            txtNumber.setText(m.getNumber() + "");
            txtCurWeight.setText(m.getCurWeight() + "");
            pos = m.getPos();
        } else {
            pos = getIntent().getIntExtra("pos", 0);
        }
        switch (pos) {
            case 0:
                rbChest.setChecked(true);
                break;
            case 1:
                rbBack.setChecked(true);
                break;
            case 2:
                rbLeg.setChecked(true);
                break;
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
                String motion = txtMotion.getText().toString();
                String group = txtGroup.getText().toString();
                String number = txtNumber.getText().toString();
                String curWeight = txtCurWeight.getText().toString();
                if (motion.isEmpty() || group.isEmpty() || number.isEmpty() || curWeight.isEmpty()) {
                    ToastUtil.show(getApplicationContext(), R.string.empty, ToastUtil.SHORT);
                } else {
                    Motion newMotion = new Motion(motion, Integer.parseInt(group), Integer.parseInt(number), Integer.parseInt(curWeight), pos);
                    if (id != -1) {
                        dao.updateMotion(id, newMotion);
                    } else {
                        dao.addMotion(newMotion);
                    }
                    this.finish();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
