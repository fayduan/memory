package cn.duanyufei.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;

/**
 * Created by DUAN Yufei on 2017/3/30.
 */

public class ColorUtil {

    public static int getColor(Context context) {
        if (StorageUtil.getColor() == 0) {
            return ContextCompat.getColor(context, android.R.color.white);
        } else {
            return ContextCompat.getColor(context, android.R.color.black);
        }
    }
}
