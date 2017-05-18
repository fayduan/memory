package cn.duanyufei.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Desc:
 * 在ViewPager中使用，支持类似于OnPause， OnResume的 OnShow，OnHide
 */
public abstract class ViewPagerFragment extends Fragment {
    private static final String CREATE = "create";
    private static final String DISPLAY = "display";
    /**
     * 是否可以显示
     */
    private boolean mDisplay = false;
    /**
     * 已创建出View
     */
    private boolean mCreated = false;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(CREATE, mCreated);
        outState.putBoolean(DISPLAY, mDisplay);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCreated = true;
        if (mDisplay == true) {
            onShow();
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mCreated = savedInstanceState.getBoolean(CREATE);
            mDisplay = savedInstanceState.getBoolean(DISPLAY);
        }
    }

    public boolean isCreated() {
        return mCreated;
    }

    /**
     * 显示
     */
    public void show() {
        if (mDisplay == true) {
            return;
        } else {
            mDisplay = true;
            if (mCreated == true) {
                onShow();
            }
        }
    }

    /**
     * 隐藏
     */
    public void hide() {
        if (mDisplay == false) {
            return;
        } else {
            mDisplay = false;
            onHide();
        }
    }

    /**
     * 当显示时调用
     */
    public abstract void onShow();

    /**
     * 当隐藏时调用
     */
    public abstract void onHide();
}
