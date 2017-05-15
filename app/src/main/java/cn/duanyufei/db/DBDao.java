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
import cn.duanyufei.greendao.MotionDao;
import cn.duanyufei.greendao.RecordDao;
import cn.duanyufei.model.Memory;
import cn.duanyufei.model.Motion;
import cn.duanyufei.model.Record;

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

    public void addMemory(String text, Date date) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MemoryDao dao = daoSession.getMemoryDao();
        Memory memory = new Memory(text, date);
        dao.insert(memory);
    }

    public Memory findMemory(long id) {
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

    public void updateMemory(long id, String text, Date date) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Memory memory = new Memory(id, text, date);
        MemoryDao dao = daoSession.getMemoryDao();
        dao.update(memory);
    }

    public void deleteMemory(long id) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MemoryDao dao = daoSession.getMemoryDao();
        dao.deleteByKey(id);
    }

    synchronized public List<Memory> findAllMemory() {
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

    public void addMotion(Motion motion) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MotionDao dao = daoSession.getMotionDao();
        dao.insert(motion);
    }

    public void addRecord(Record record) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        RecordDao dao = daoSession.getRecordDao();
        dao.insert(record);
    }

    public void updateMotion(Motion motion) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MotionDao dao = daoSession.getMotionDao();
        dao.update(motion);
    }

    public void updateRecord(Record record) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        RecordDao dao = daoSession.getRecordDao();
        dao.update(record);
    }

    public Motion findMotion(long id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MotionDao dao = daoSession.getMotionDao();
        QueryBuilder<Motion> qb = dao.queryBuilder();
        qb.where(MotionDao.Properties.Id.eq(id));
        List<Motion> motions = qb.list();
        if (motions == null || motions.size() == 0) {
            return null;
        } else {
            return motions.get(0);
        }
    }

    public Record findRecord(long id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        RecordDao dao = daoSession.getRecordDao();
        QueryBuilder<Record> qb = dao.queryBuilder();
        qb.where(RecordDao.Properties.Id.eq(id));
        List<Record> records = qb.list();
        if (records == null || records.size() == 0) {
            return null;
        } else {
            return records.get(0);
        }
    }

    public List<Record> findRecordByMotion(long id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        RecordDao dao = daoSession.getRecordDao();
        QueryBuilder<Record> qb = dao.queryBuilder();
        qb.where(RecordDao.Properties.MotionId.eq(id));
        List<Record> records = qb.list();
        return records;
    }

    synchronized public List<Motion> findAllMotion() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MotionDao dao = daoSession.getMotionDao();
        QueryBuilder<Motion> qb = dao.queryBuilder();
        List<Motion> motions = qb.list();
        RecordDao recordDao = daoSession.getRecordDao();
        for (int i = 0; i < motions.size(); i++) {
            QueryBuilder<Record> recordQueryBuilder = recordDao.queryBuilder();
            recordQueryBuilder.where(RecordDao.Properties.MotionId.eq(motions.get(i)));
            recordQueryBuilder.orderDesc(RecordDao.Properties.Date);
            List<Record> records = recordQueryBuilder.list();
            if (records == null || records.size() == 0) {
                motions.get(i).setCurWeight(records.get(0).getWeight());
            }
        }
        return motions;
    }

    public void deleteMotion(long id) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MotionDao dao = daoSession.getMotionDao();
        dao.deleteByKey(id);
    }

    public void deleteRecord(long id) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        RecordDao dao = daoSession.getRecordDao();
        dao.deleteByKey(id);
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
