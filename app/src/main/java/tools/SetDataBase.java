package tools;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SetDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "intDatabase.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "intTable";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FLAG1 = "flag1"; //磁立瘦温度报警开关，默认1
    private static final String COLUMN_FLAG2 = "flag2";// 磁立瘦控制板声音开关，默认1
    private static final String COLUMN_FLAG3 = "flag3";//冷冻水流报警值，默认10,范围0-20
    private static final String COLUMN_FLAG4 = "flag4";//冷冻负压报警值，默认103，超过103报警
    private static final String COLUMN_FLAG5 = "flag5";
    private static final String COLUMN_FLAG6 = "flag6";
    private static final String COLUMN_FLAG7 = "flag7";
    private static final String COLUMN_FLAG8 = "flag8";
    private static final String COLUMN_FLAG9 = "flag9";
    private static final String COLUMN_FLAG10 = "flag10";
    private static final String COLUMN_FLAG11 = "flag11";
    private static final String COLUMN_FLAG12 = "flag12";
    private static final String COLUMN_FLAG13 = "flag13";
    private static final String COLUMN_FLAG14 = "flag14";//b
    private static final String COLUMN_FLAG15 = "flag15";
    private static final String COLUMN_FLAG16 = "flag16";
    private static final String COLUMN_FLAG17 = "flag17";
    private static final String COLUMN_FLAG18 = "flag18";
    private static final String COLUMN_FLAG19 = "flag19";
    private static final String COLUMN_FLAG20 = "flag20";
    private static final String COLUMN_FLAG21 = "flag21";
    private static final String COLUMN_FLAG22 = "flag22";
    private static final String COLUMN_FLAG23 = "flag23";
    private static final String COLUMN_FLAG24 = "flag24";//冷冻intellgeet模式设置选项1-5，默认1  上面的手柄
    private static final String COLUMN_FLAG25 = "flag25";//冷冻intellgeet模式设置选项1-5，默认1  下面的手柄
    private static final String COLUMN_FLAG26 = "flag26";//T1射频时间，默认3
    private static final String COLUMN_FLAG27 = "flag27";//T2射频时间，默认3
    private static final String COLUMN_FLAG28 = "flag28";//射频占空比默认40
    private static final String COLUMN_FLAG29 = "flag29";//磁力电压默认450
    private static final String COLUMN_FLAG30 = "flag30";//射频电压默认90
    private static final String COLUMN_FLAG31 = "flag31"; // wifi 报警
    public SetDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 创建数据库表，并为所有列赋予默认初值 0
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FLAG1 + " INTEGER," +
                COLUMN_FLAG2 + " INTEGER," +
                COLUMN_FLAG3 + " INTEGER," +
                COLUMN_FLAG4 + " INTEGER," +
                COLUMN_FLAG5 + " INTEGER," +
                COLUMN_FLAG6 + " INTEGER," +
                COLUMN_FLAG7 + " INTEGER," +
                COLUMN_FLAG8 + " INTEGER," +
                COLUMN_FLAG9 + " INTEGER," +
                COLUMN_FLAG10 + " INTEGER," +
                COLUMN_FLAG11 + " INTEGER," +
                COLUMN_FLAG12 + " INTEGER," +
                COLUMN_FLAG13 + " INTEGER," +
                COLUMN_FLAG14 + " INTEGER," +
                COLUMN_FLAG15 + " INTEGER," +
                COLUMN_FLAG16 + " INTEGER," +
                COLUMN_FLAG17 + " INTEGER," +
                COLUMN_FLAG18 + " INTEGER," +
                COLUMN_FLAG19 + " INTEGER," +
                COLUMN_FLAG20 + " INTEGER," +
                COLUMN_FLAG21 + " INTEGER," +
                COLUMN_FLAG22 + " INTEGER," +
                COLUMN_FLAG23 + " INTEGER," +
                COLUMN_FLAG24 + " INTEGER," +
                COLUMN_FLAG25 + " INTEGER," +
                COLUMN_FLAG26 + " INTEGER," +
                COLUMN_FLAG27 + " INTEGER," +
                COLUMN_FLAG28 + " INTEGER," +
                COLUMN_FLAG29 + " INTEGER," +
                COLUMN_FLAG30 + " INTEGER,"+
                COLUMN_FLAG31 +" INTEGER)";
        db.execSQL(createTable);
        // 初始化一行数据
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_FLAG1, 1);
        initialValues.put(COLUMN_FLAG2, 1);
        initialValues.put(COLUMN_FLAG3, 10);
        initialValues.put(COLUMN_FLAG4, 103);
        initialValues.put(COLUMN_FLAG5, 103);
        initialValues.put(COLUMN_FLAG6, 103);
        initialValues.put(COLUMN_FLAG7, 103);
        initialValues.put(COLUMN_FLAG8, 103);
        initialValues.put(COLUMN_FLAG9, 103);
        initialValues.put(COLUMN_FLAG10, 103);
        initialValues.put(COLUMN_FLAG11, 103);
        initialValues.put(COLUMN_FLAG12, 103);
        initialValues.put(COLUMN_FLAG13, 103);
        initialValues.put(COLUMN_FLAG14, 103);
        initialValues.put(COLUMN_FLAG15, 103);
        initialValues.put(COLUMN_FLAG16, 103);
        initialValues.put(COLUMN_FLAG17, 103);
        initialValues.put(COLUMN_FLAG18, 103);
        initialValues.put(COLUMN_FLAG19, 103);
        initialValues.put(COLUMN_FLAG20, 103);
        initialValues.put(COLUMN_FLAG21, 103);
        initialValues.put(COLUMN_FLAG22, 103);
        initialValues.put(COLUMN_FLAG23, 103);
        initialValues.put(COLUMN_FLAG24, 1);
        initialValues.put(COLUMN_FLAG25, 1);
        initialValues.put(COLUMN_FLAG26, 3);
        initialValues.put(COLUMN_FLAG27, 3);
        initialValues.put(COLUMN_FLAG28, 40);
        initialValues.put(COLUMN_FLAG29, 370);
        initialValues.put(COLUMN_FLAG30, 280);
        initialValues.put(COLUMN_FLAG31,0); // WIFI 报警

        db.insert(TABLE_NAME, null, initialValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // 更新指定列的单个值
    public void updateSingleColumn(int id, String columnName, int newValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnName, newValue); // 动态指定列名和新值
        db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    @SuppressLint("Range")
    public int getSingleColumnDataById(int id, String columnName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + columnName + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        int value = -1; // 默认值，表示没有找到数据或发生错误
        if (cursor.moveToFirst()) {
            value = cursor.getInt(cursor.getColumnIndex(columnName));
        }

        cursor.close();
        db.close();

        return value;
    }

}

