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
import cn.duanyufei.greendao.PlanDao;
import cn.duanyufei.greendao.RecordDao;
import cn.duanyufei.model.Memory;
import cn.duanyufei.model.Motion;
import cn.duanyufei.model.Plan;
import cn.duanyufei.model.Record;

public class DBDao {

    final static String TAG = "DBDao";

    public final static String dbName = "memory.db";
    private static DBDao mInstance;
    private DbOpenHelper openHelper;
    private DaoSession daoSession;

    private DBDao() {
        openHelper = new DbOpenHelper(MApplication.getContext(), dbName, null);
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        daoSession = daoMaster.newSession();
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
                    mInstance = new DBDao();
                }
            }
        }
        return mInstance;
    }

    public void addMemory(String text, Date date) {
        MemoryDao dao = daoSession.getMemoryDao();
        Memory memory = new Memory(text, date);
        dao.insert(memory);
    }

    public Memory findMemory(long id) {
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
        Memory memory = findMemory(id);
        if (memory != null) {
            memory.setText(text);
            memory.setDate(date);
        } else {
            memory = new Memory(id, text, date);
        }
        MemoryDao dao = daoSession.getMemoryDao();
        dao.update(memory);
    }

    public void updateMemory(Memory memory, int pos) {
        MemoryDao dao = daoSession.getMemoryDao();
        memory.setPosition(pos);
        dao.update(memory);
    }

    public void deleteMemory(long id) {
        MemoryDao dao = daoSession.getMemoryDao();
        dao.deleteByKey(id);
    }

    synchronized public List<Memory> findAllMemory() {
        MemoryDao dao = daoSession.getMemoryDao();
        QueryBuilder<Memory> qb = dao.queryBuilder();
        qb.orderAsc(MemoryDao.Properties.Position);
        qb.orderDesc(MemoryDao.Properties.Id);
        List<Memory> memories = qb.list();
        for (int i = 0; i < memories.size(); i++) {
            memories.get(i).update();
        }
        return memories;
    }

    public void addPlan(String text, Date date) {
        PlanDao dao = daoSession.getPlanDao();
        Plan plan = new Plan();
        plan.setText(text);
        plan.setDate(date);
        plan.setIsDone(false);
        plan.setPosition(-1);
        dao.insert(plan);
    }

    public void updatePlan(Plan plan, int pos) {
        PlanDao dao = daoSession.getPlanDao();
        plan.setPosition(pos);
        dao.update(plan);
    }

    public void deletePlan(long id) {
        PlanDao dao = daoSession.getPlanDao();
        dao.deleteByKey(id);
    }

    synchronized public List<Plan> findAllPlan() {
        PlanDao dao = daoSession.getPlanDao();
        QueryBuilder<Plan> qb = dao.queryBuilder();
        qb.orderAsc(PlanDao.Properties.Position);
        qb.orderDesc(PlanDao.Properties.Id);
        return qb.list();
    }

    public long addMotion(Motion motion) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MotionDao dao = daoSession.getMotionDao();
        long id = dao.insert(motion);
        addRecord(new Record(id, new Date(), motion.getCurWeight()));
        return id;
    }

    public long addRecord(Record record) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        RecordDao dao = daoSession.getRecordDao();
        return dao.insert(record);
    }

    public void updateMotion(long id, Motion motion) {
        Motion oldMotion = findMotion(id);
        oldMotion.setGroups(motion.getGroups());
        oldMotion.setNumber(motion.getNumber());
        oldMotion.setText(motion.getText());
        oldMotion.setPos(motion.getPos());
        if (oldMotion.getCurWeight() != motion.getCurWeight()) {
            addRecord(new Record(oldMotion.getId(), new Date(), motion.getCurWeight()));
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MotionDao dao = daoSession.getMotionDao();
        dao.update(oldMotion);
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
            Motion motion = motions.get(0);
            Record record = findNewestRecord(motion.getId());
            if (record != null) {
                motion.setCurWeight(record.getWeight());
            }
            return motion;
        }
    }

    public List<Motion> findMotionByPos(int pos) {
        if (pos == -1) return null;
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MotionDao dao = daoSession.getMotionDao();
        QueryBuilder<Motion> qb = dao.queryBuilder();
        qb.where(MotionDao.Properties.Pos.eq(pos));
        List<Motion> motions = qb.list();
        RecordDao recordDao = daoSession.getRecordDao();
        for (int i = 0; i < motions.size(); i++) {
            QueryBuilder<Record> recordQueryBuilder = recordDao.queryBuilder();
            recordQueryBuilder.where(RecordDao.Properties.MotionId.eq(motions.get(i).getId()));
            recordQueryBuilder.orderDesc(RecordDao.Properties.Date);
            List<Record> records = recordQueryBuilder.list();
            if (records != null && records.size() != 0) {
                motions.get(i).setCurWeight(records.get(0).getWeight());
            }
        }
        return motions;
    }

    public Record findNewestRecord(long id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        RecordDao recordDao = daoSession.getRecordDao();
        QueryBuilder<Record> recordQueryBuilder = recordDao.queryBuilder();
        recordQueryBuilder.where(RecordDao.Properties.MotionId.eq(id));
        recordQueryBuilder.orderDesc(RecordDao.Properties.Date);
        List<Record> records = recordQueryBuilder.list();
        if (records == null || records.size() == 0) {
            return null;
        } else {
            return records.get(0);
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
        qb.orderDesc(RecordDao.Properties.Date);
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
            recordQueryBuilder.where(RecordDao.Properties.MotionId.eq(motions.get(i).getId()));
            recordQueryBuilder.orderDesc(RecordDao.Properties.Date);
            List<Record> records = recordQueryBuilder.list();
            if (records != null && records.size() != 0) {
                motions.get(i).setCurWeight(records.get(0).getWeight());
            }
        }
        return motions;
    }

    public void deleteMotion(long id) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MotionDao dao = daoSession.getMotionDao();
        RecordDao recordDao = daoSession.getRecordDao();
        List<Record> records = findRecordByMotion(id);
        for (int i = 0; i < records.size(); i++) {
            recordDao.delete(records.get(i));
        }
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
            openHelper = new DbOpenHelper(MApplication.getContext(), dbName, null);
        }
        return openHelper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DbOpenHelper(MApplication.getContext(), dbName, null);
        }
        return openHelper.getWritableDatabase();
    }
}
