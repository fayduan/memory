package cn.duanyufei.util;

import android.content.Context;
import android.widget.Toast;

import cn.duanyufei.app.MApplication;

/**
 * Created by DUAN Yufei on 2017/3/6.
 */

public class ToastUtil {
    private static Toast toast = null;

    public static int SHORT = Toast.LENGTH_SHORT;
    public static int LONG = Toast.LENGTH_LONG;

    private ToastUtil() {

    }

    public static void show(int resId) {
        if (toast == null) {
            toast = Toast.makeText(MApplication.getContext(), "", SHORT);
        }
        toast.setText(resId);
        toast.show();
    }

    public static void show(Context context, int resId) {
        if (toast == null) {
            toast = Toast.makeText(context, "", SHORT);
        }
        toast.setText(resId);
        toast.show();
    }

    public static void show(Context context, int resId, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, "", duration);
        }
        toast.setText(resId);
        toast.show();
    }

    public static void show(Context context, String text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, "", duration);
        }
        toast.setText(text);
        toast.show();
    }
}
