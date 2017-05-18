package cn.duanyufei.memory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;

import cn.duanyufei.custom.SectionsPagerAdapter;
import cn.duanyufei.custom.ViewPagerActivity;

public class MotionActivity extends ViewPagerActivity {

    final static String TAG = "MotionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_motion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MotionActivity.this.finish();
            }
        });

        initBottomNavigationBar();
        initViewPager();
    }

    private void initBottomNavigationBar() {
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.clearAll();
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.ic_fitness_center_white_36dp, getString(R.string.chest)))
                .addItem(new BottomNavigationItem(R.mipmap.ic_accessibility_white_36dp, getString(R.string.back)))
                .addItem(new BottomNavigationItem(R.mipmap.ic_directions_run_white_36dp, getString(R.string.leg)))
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(this);
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mFragments = new ArrayList<>();
        mFragments.add(new PartFragment());
        mFragments.add(new PartFragment());
        mFragments.add(new PartFragment());


        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), mFragments));
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);  //最多缓存
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            MotionActivity.this.startActivity(new Intent(MotionActivity.this, AddMotionActivity.class));
        }
        return true;
    }
}
