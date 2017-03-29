package cn.duanyufei.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBSQLiteOpenHelper extends SQLiteOpenHelper {

	public DBSQLiteOpenHelper(Context context) {
		super(context, "memory.db", null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE [memory] (  [id] integer PRIMARY KEY AUTOINCREMENT," 
				+"  [text] varchar(200), [mdate] integer)");
}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
