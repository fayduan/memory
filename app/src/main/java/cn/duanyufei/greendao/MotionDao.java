package cn.duanyufei.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.duanyufei.model.Motion;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MOTION".
*/
public class MotionDao extends AbstractDao<Motion, Long> {

    public static final String TABLENAME = "MOTION";

    /**
     * Properties of entity Motion.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Text = new Property(1, String.class, "text", false, "TEXT");
        public final static Property Groups = new Property(2, int.class, "groups", false, "GROUPS");
        public final static Property Number = new Property(3, int.class, "number", false, "NUMBER");
        public final static Property Pos = new Property(4, int.class, "pos", false, "POS");
        public final static Property Part = new Property(5, String.class, "part", false, "PART");
    }


    public MotionDao(DaoConfig config) {
        super(config);
    }
    
    public MotionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MOTION\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TEXT\" TEXT," + // 1: text
                "\"GROUPS\" INTEGER NOT NULL ," + // 2: groups
                "\"NUMBER\" INTEGER NOT NULL ," + // 3: number
                "\"POS\" INTEGER NOT NULL ," + // 4: pos
                "\"PART\" TEXT);"); // 5: part
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MOTION\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Motion entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String text = entity.getText();
        if (text != null) {
            stmt.bindString(2, text);
        }
        stmt.bindLong(3, entity.getGroups());
        stmt.bindLong(4, entity.getNumber());
        stmt.bindLong(5, entity.getPos());
 
        String part = entity.getPart();
        if (part != null) {
            stmt.bindString(6, part);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Motion entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String text = entity.getText();
        if (text != null) {
            stmt.bindString(2, text);
        }
        stmt.bindLong(3, entity.getGroups());
        stmt.bindLong(4, entity.getNumber());
        stmt.bindLong(5, entity.getPos());
 
        String part = entity.getPart();
        if (part != null) {
            stmt.bindString(6, part);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Motion readEntity(Cursor cursor, int offset) {
        Motion entity = new Motion( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // text
            cursor.getInt(offset + 2), // groups
            cursor.getInt(offset + 3), // number
            cursor.getInt(offset + 4), // pos
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // part
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Motion entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setText(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setGroups(cursor.getInt(offset + 2));
        entity.setNumber(cursor.getInt(offset + 3));
        entity.setPos(cursor.getInt(offset + 4));
        entity.setPart(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Motion entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Motion entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Motion entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
