package cn.duanyufei.util;

import android.content.Context;
import android.content.SharedPreferences;

import cn.duanyufei.app.MApplication;

/**
 * Created by DUAN Yufei on 2017/3/9.
 */

public class StorageUtil {

    /**
     * 公用配置
     */
    private static final String LOCAL_STORAGE = "memory_storage";

    private static SharedPreferences sharedPreferences
            = MApplication.getInstance().getSharedPreferences(LOCAL_STORAGE, Context.MODE_PRIVATE);

    /**
     * 数据标签
     */
    private static final String COLOR = "color";
    private static final String NAME = "name";
    private static final String CHECK = "check";

    /**
     * 用户数据
     */
    private static int sColor = 0;
    private static String sName = null;
    private static String sCheck = null;

    /**
     * Getters & Setters
     */
    public static int getColor() {
        if (sColor == 0) {
            sColor = sharedPreferences.getInt(COLOR, 0);
        }
        return sColor;
    }

    public static void setColor(int color) {
        sColor = color;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(COLOR, color);
        editor.apply();
    }

    public static String getName() {
        return sharedPreferences.getString(NAME, null);
    }

    public static String getCheck() {
        return sharedPreferences.getString(CHECK, null);
    }

    public static void setName(String name) {
        sName = name;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME, name);
        editor.apply();
    }

    public static void setCheck(String check) {
        sCheck = check;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CHECK, check);
        editor.apply();
    }
}
