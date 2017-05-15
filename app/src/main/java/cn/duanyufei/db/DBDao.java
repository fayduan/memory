package cn.duanyufei.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.duanyufei.app.MApplication;
import cn.duanyufei.greendao.DaoMaster;
import cn.duanyufei.greendao.DaoSession;
import cn.duanyufei.greendao.MemoryDao;
import cn.duanyufei.model.Memory;

public class DBDao {
    private final static String dbName = "iseeyou.db";
    private static DBDao mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;
    private Calendar cal;

    private DBDao(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        cal = Calendar.getInstance();
    }

    /**
     * 获取单例引用
     *
     * @return 实例
     */

    public static DBDao getInstance() {
        if (mInstance == null) {
            synchronized (DBDao.class) {
                if (mInstance == null) {
                    mInstance = new DBDao(MApplication.getContext());
                }
            }
        }
        return mInstance;
    }

    public void add(String text, Date date) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MemoryDao dao = daoSession.getMemoryDao();
        Memory memory = new Memory(text, date);
        dao.insert(memory);
    }

    public Memory find(long id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MemoryDao dao = daoSession.getMemoryDao();
        QueryBuilder<Memory> qb = dao.queryBuilder();
        qb.where(MemoryDao.Properties.Id.eq(id));
        List<Memory> memories = qb.list();
        if (memories == null || memories.size() == 0) {
            return null;
        } else {
            Memory memory = memories.get(0);
            memory.update();
            return memory;
        }
    }

    public void update(long id, String text, Date date) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Memory memory = new Memory(id, text, date);
        MemoryDao dao = daoSession.getMemoryDao();
        dao.update(memory);
    }

    public void delete(long id) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MemoryDao dao = daoSession.getMemoryDao();
        dao.deleteByKey(id);
    }

    synchronized public List<Memory> findAll() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MemoryDao dao = daoSession.getMemoryDao();
        QueryBuilder<Memory> qb = dao.queryBuilder();
        List<Memory> memories = qb.list();
        for (int i = 0; i < memories.size(); i++) {
            memories.get(i).update();
        }
        return memories;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        return openHelper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        return openHelper.getWritableDatabase();
    }
}
