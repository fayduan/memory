package cn.duanyufei.view;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;

import java.util.List;

public class ViewPagerActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    final static String TAG = "ViewPagerActivity";

    protected ViewPager mViewPager;
    protected BottomNavigationBar mBottomNavigationBar;
    protected List<Fragment> mFragments;
    protected int mSelectedIndex = 0;

    public void hide() {
        if (mFragments != null && mSelectedIndex < mFragments.size()) {
            ((ViewPagerFragment) mFragments.get(mSelectedIndex)).hide();
        }

    }

    public void show() {
        if (mFragments != null && mSelectedIndex < mFragments.size()) {
            ((ViewPagerFragment) mFragments.get(mSelectedIndex)).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        show();
    }

    //  Start of BottomNavigationBar.OnTabSelectedListener
    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onTabSelected(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }
    // End of BottomNavigationBar.OnTabSelectedListener

    // Start of ViewPager.OnPageChangeListener
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ((ViewPagerFragment) mFragments.get(mSelectedIndex)).hide();
        mSelectedIndex = position;
        mBottomNavigationBar.selectTab(position);
        ((ViewPagerFragment) mFragments.get(mSelectedIndex)).show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    // End of ViewPager.OnPageChangeListener

    public void setViewPagerDisallowTouchEvent() {
        if (mViewPager != null) {
            mViewPager.requestDisallowInterceptTouchEvent(true);
        }
    }
}
