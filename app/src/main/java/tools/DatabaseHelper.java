package tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "counts";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_COUNT = "count";
    private static DatabaseHelper instance;
    private static final String COLUMN_FLAG = "flag";//离线  关
    private static final String COLUMN_FLAG1 = "flag1";//本地 开
    private static final String COLUMN_FLAG2 = "flag2";//网络 开

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_COUNT + " INTEGER,"
                + COLUMN_FLAG + " INTEGER,"
                + COLUMN_FLAG2 + " INTEGER,"
                + COLUMN_FLAG1 + " INTEGER)"; // 添加新列
        db.execSQL(CREATE_TABLE);

        // 初始化次数为50万次
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_COUNT, 60000);
        initialValues.put(COLUMN_FLAG, 0); // 设置初始标志值
        initialValues.put(COLUMN_FLAG2, 1);
        initialValues.put(COLUMN_FLAG1, 0);

        db.insert(TABLE_NAME, null, initialValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 简化了升级数据库的逻辑：只是删除并重新创建表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    // 获取标志
    public int getFlag(String str) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + str + " FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            int flag = cursor.getInt(0);
            cursor.close();
            return flag;
        }
        cursor.close();
        return -1; // 如果没有数据则返回-1
    }

    // 设置标志
    public void setFlag(int flag,String str) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(str, flag);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{"1"});
    }

    // 获取当前的次数
    public long getRemainingCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_COUNT + " FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            long count = cursor.getLong(0);
            cursor.close();
            return count;
        }
        cursor.close();
        return 0; // 为了简单，如果没有数据则返回0
    }

    // 保存新的次数
    public void saveRemainingCount(long count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COUNT, count);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{"1"});
    }
}

