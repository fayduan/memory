package cn.duanyufei.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import cn.duanyufei.greendao.DaoMaster;

/**
 * Created by DUAN Yufei on 2017/5/18.
 */

public class DbOpenHelper extends DaoMaster.OpenHelper {
    public DbOpenHelper(Context context, String name) {
        super(context, name);
    }

    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion);
        if (oldVersion < 2) {
            db.execSQL("create table \"MOTION\" (" + //
                    "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                    "\"TEXT\" TEXT," + // 1: text
                    "\"GROUPS\" INTEGER NOT NULL ," + // 2: groups
                    "\"NUMBER\" INTEGER NOT NULL ," + // 3: number
                    "\"POS\" INTEGER NOT NULL ," + // 4: pos
                    "\"PART\" TEXT);");
            db.execSQL("CREATE TABLE \"RECORD\" (" + //
                    "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                    "\"MOTION_ID\" INTEGER," + // 1: motionId
                    "\"DATE\" INTEGER," + // 2: date
                    "\"WEIGHT\" INTEGER NOT NULL );");
        }
        if (oldVersion < 3) {
            db.execSQL(String.format("ALTER TABLE %s ADD %s integer default 0", "MOTION", "pos"));
            db.execSQL(String.format("ALTER TABLE %s ADD %s varchar", "MOTION", "part"));
        }

    }
}