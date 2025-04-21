package tools.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// TODO 数据库名：RFInitDB (作用：初始化数据表。最初的。每个表中有个 flag 字段，默认值为 0 ，当用户点击保存时， flag 值变为 1，则不会在这个表中查找数据而是在 RFInitDB-SAVE 中查找1）)

public class RfInitDatabase extends SQLiteOpenHelper {
    private static  final String DATABASE_NAME = "RFInitDB";
    private static  final String TABLE_NAME_HIIT = "HIIT";
    private static  final String TABLE_NAME_STRENGTH = "STRENGTH";
    private static  final String TABLE_NAME_MUSCLE="MUSCLE";
    private static  final String TABLE_NAME_SHAPE = "SHAPE";
    private static  final String TABLE_NAME_FAT = "FAT";
    private static  final String TABLE_NAME_RECOVERY ="RECOVERY";
    private SQLiteDatabase mRDB = null;
    private  SQLiteDatabase mWDB = null;
    private  static  RfInitDatabase dbHelder = null;
    private  RfInitDatabase (Context context){
        super(context,DATABASE_NAME,null,1);
    }
    public static  RfInitDatabase getRfInitDatabase(Context context){
        if(dbHelder == null){
            dbHelder = new RfInitDatabase(context);
        }
        return dbHelder;
    }
    public SQLiteDatabase openWriteDB(){
        if(mWDB==null || mWDB.isOpen()){
            mWDB = dbHelder.getWritableDatabase();
        }
        return mWDB;
    }
    public SQLiteDatabase openReadDB(){
        if(mRDB == null || mRDB.isOpen()){
            mRDB = dbHelder.getReadableDatabase();
        }
        return mRDB;
    }
    public void closeDB(){
        if (mWDB!=null && mWDB.isOpen()){
            mWDB.close();
            mWDB=null;
        }
        if (mRDB!=null && mRDB.isOpen()){
            mRDB.close();
            mRDB=null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String HIIT_sql = "create table "+TABLE_NAME_HIIT +" ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "RF12  INT ," +
                "RF34  INT ," +
                "P12   INT ," +
                "P34   INT ," +
                "F1    INT," +
                "F2    INT," +
                "F3    INT," +
                "flag  INT DEFAULT 0"+
                ")";
        db.execSQL(HIIT_sql);
        String STRENGTH_sql = "create table "+TABLE_NAME_STRENGTH +" ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "RF12  INT ," +
                "RF34  INT ," +
                "P12   INT ," +
                "P34   INT ," +
                "F1    INT," +
                "F2    INT," +
                "F3    INT," +
                "flag  INT DEFAULT 0"+
                ")";
        db.execSQL(STRENGTH_sql);
        String MUSCLE_sql = "create table "+TABLE_NAME_MUSCLE +" ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "RF12  INT ," +
                "RF34  INT ," +
                "P12   INT ," +
                "P34   INT ," +
                "F1    INT," +
                "F2    INT," +
                "F3    INT," +
                "flag  INT DEFAULT 0"+
                ")";
        db.execSQL(MUSCLE_sql);
        String SHAPE_sql = "create table "+TABLE_NAME_SHAPE +" ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "RF12  INT ," +
                "RF34  INT ," +
                "P12   INT ," +
                "P34   INT ," +
                "F1    INT," +
                "F2    INT," +
                "F3    INT," +
                "flag  INT DEFAULT 0"+
                ")";
        db.execSQL(SHAPE_sql);
        String FAT_sql = "create table "+TABLE_NAME_FAT +" ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "RF12  INT ," +
                "RF34  INT ," +
                "P12   INT ," +
                "P34   INT ," +
                "F1    INT," +
                "F2    INT," +
                "F3    INT," +
                "flag  INT DEFAULT 0"+
                ")";
        db.execSQL(FAT_sql);
        String RECOVERY_sql = "create table "+TABLE_NAME_RECOVERY +" ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "RF12  INT ," +
                "RF34  INT ," +
                "P12   INT ," +
                "P34   INT ," +
                "F1    INT," +
                "F2    INT," +
                "F3    INT," +
                "flag  INT DEFAULT 0"+
                ")";
        db.execSQL(RECOVERY_sql);
        insertInit(db);
    }

    private void insertInit(SQLiteDatabase db){
        //男
        //胳膊                                                                     RF    P
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,50,50,20,30,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(13,13,55,55,25,38,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(13,13,55,55,25,40,18)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(15,15,55,55,35,40,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES(20,20,60,60,30,40,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(15,15,70,70,80,60,70)");
        //腹部
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,60,60,30,35,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(13,13,60,60,35,40,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(15,15,60,60,35,45,18)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(15,15,60,60,28,30,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES(20,20,60,60,30,38,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(15,15,70,70,75,65,70)");
        //臀部
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,60,60,30,40,18)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(15,15,65,65,35,45,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(15,15,65,65,38,48,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(15,15,65,65,40,45,30)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES(20,20,70,70,40,45,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(15,15,75,75,80,50,70)");
        //大腿
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,50,50,25,38,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(13,13,55,55,30,40,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(15,15,60,60,35,45,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(15,15,60,60,35,45,30)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES(20,20,60,60,35,48,35)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(15,15,70,70,70,55,65)");
        //小腿
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,50,50,15,30,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(13,13,45,45,20,35,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(13,13,50,50,25,35,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(15,15,55,55,30,38,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES(18,18,55,55,30,40,25)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(15,15,60,60,85,60,70)");
        //后脖颈
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,45,45,15,25,8)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(13,13,45,45,15,25,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(13,13,50,50,25,30,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(15,15,55,55,25,40,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES(15,15,55,55,28,45,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(15,15,60,60,80,60,75)");
        //背部
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,50,50,20,30,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(13,13,55,55,25,35,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(13,13,60,60,30,40,18)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(15,15,65,65,35,45,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES(20,20,70,70,35,48,25)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(15,15,70,70,70,65,60)");

        //后大腿
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,50,50,18,30,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(13,13,55,55,20,35,18)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(13,13,55,55,25,35,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(15,15,55,55,30,40,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES(18,18,55,55,30,45,25)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(15,15,60,60,90,60,75)");
        //后小腿
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,50,50,15,30,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(13,13,55,55,20,35,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(13,13,55,55,25,35,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(15,15,55,55,30,38,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES (18,18,55,55,30,40,25)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(15,15,60,60,85,60,70)");

        //女
        //胳膊
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,50,50,15,20,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(13,13,55,55,20,30,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(13,13,55,55,22,25,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(15,15,55,55,25,35,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES(20,20,60,60,28,35,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(15,15,70,70,80,65,70)");
        //腹部
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,60,60,20,28,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(15,15,60,60,25,30,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(15,15,60,60,28,35,18)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(15,15,60,60,28,38,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES (20,20,60,60,30,38,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(15,15,70,70,65,60,75)");
        //臀部
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(15,15,65,65,30,40,18)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(15,15,65,65,35,45,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(15,15,65,65,38,48,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(20,20,65,65,35,45,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES (15,15,60,60,35,48,25)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(15,15,65,65,70,65,60)");
        //大腿
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,50,50,20,30,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(10,10,50,50,20,35,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(10,10,45,45,20,35,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(10,10,50,50,20,35,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES (10,10,45,45,20,35,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(10,10,45,45,85,50,65)");
        //小腿
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,50,50,15,30,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(12,12,55,55,20,35,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(10,10,50,50,25,35,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(15,15,55,55,30,38,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES (10,10,45,45,30,40,25)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(10,10,40,40,85,60,70)");
        //后脖颈
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(5,5,30,30,15,25,8)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(5,5,30,30,15,25,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(10,10,35,35,25,30,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(5,5,35,35,25,40,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES(10,10,40,40,28,45,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(10,10,40,40,80,60,75)");
        //背部
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,40,40,20,30,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(10,10,45,45,25,35,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(10,10,45,45,30,40,18)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(10,10,45,45,35,45,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES (10,10,45,45,35,48,25)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(10,10,45,45,70,65,60)");
        //后大腿
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,50,50,18,30,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(12,12,55,55,20,35,18)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(10,10,55,55,25,35,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(15,15,55,55,30,40,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES (10,10,45,45,30,45,25)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(10,10,40,40,90,60,75)");
        //后小腿
        db.execSQL("INSERT INTO "+TABLE_NAME_HIIT+"(RF12,RF34,P12,P34,F1,F2,F3)     VALUES(10,10,50,50,15,30,10)");
        db.execSQL("INSERT INTO "+TABLE_NAME_STRENGTH+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(12,12,55,55,20,35,15)");
        db.execSQL("INSERT INTO "+TABLE_NAME_MUSCLE+"(RF12,RF34,P12,P34,F1,F2,F3)   VALUES(10,10,50,50,25,35,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_SHAPE+"(RF12,RF34,P12,P34,F1,F2,F3)    VALUES(15,15,55,55,30,38,20)");
        db.execSQL("INSERT INTO "+TABLE_NAME_FAT+"(RF12,RF34,P12,P34,F1,F2,F3)      VALUES (10,10,45,45,30,40,25)");
        db.execSQL("INSERT INTO "+TABLE_NAME_RECOVERY+"(RF12,RF34,P12,P34,F1,F2,F3) VALUES(10,10,40,40,85,60,70)");
    }
    public WorkIData getData(int mode ,int part) {
        String tableName;
        switch (mode){
            case 1:
                tableName = TABLE_NAME_HIIT;
                break;
            case 2:
                tableName = TABLE_NAME_STRENGTH;
                break;
            case 3:
                tableName = TABLE_NAME_MUSCLE;
                break;
            case 4:
                tableName = TABLE_NAME_SHAPE;
                break;
            case 5:
                tableName = TABLE_NAME_FAT;
                break;
            case 6:
                tableName = TABLE_NAME_RECOVERY;
                break;
            default:
                tableName= TABLE_NAME_HIIT;
                break;
        }
        Cursor cursor = mRDB.query(false,tableName,null,"id =?",new String[]{String.valueOf(part)},null,null,null,null);
        WorkIData workIData = new WorkIData();
        while(cursor.moveToNext()){
            workIData.setP1(cursor.getInt(cursor.getColumnIndexOrThrow("P12")));
            workIData.setP2(cursor.getInt(cursor.getColumnIndexOrThrow("P34")));
            workIData.setRf1(cursor.getInt(cursor.getColumnIndexOrThrow("RF12")));
            workIData.setRf2(cursor.getInt(cursor.getColumnIndexOrThrow("RF34")));
            workIData.setF1(cursor.getInt(cursor.getColumnIndexOrThrow("F1")));
            workIData.setF2(cursor.getInt(cursor.getColumnIndexOrThrow("F2")));
            workIData.setF3(cursor.getInt(cursor.getColumnIndexOrThrow("F3")));
        }
        Log.e("", "getDsadasdata: " + workIData);
        return workIData;
    }
    //修改flag 如何为0 则 恢复初始化数据， 如何为1 则使用用户保存的数据。
    public long updateFlag(int mode ,int part , int rf){
        String tableName;
        switch (mode){
            case 1:
                tableName = TABLE_NAME_HIIT;
                break;
            case 2:
                tableName = TABLE_NAME_STRENGTH;
                break;
            case 3:
                tableName = TABLE_NAME_MUSCLE;
                break;
            case 4:
                tableName = TABLE_NAME_SHAPE;
                break;
            case 5:
                tableName = TABLE_NAME_FAT;
                break;
            case 6:
                tableName = TABLE_NAME_RECOVERY;
                break;
            default:
                tableName= TABLE_NAME_HIIT;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("flag",rf);
        System.out.println("save flag   table  "+tableName+ "mode "+part);
        return mWDB.update(tableName,contentValues,"id = ?",new String[]{String.valueOf(part)});
    }
    public long recovery(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("flag",0);
        mWDB.update(TABLE_NAME_HIIT,contentValues,null,null);
        mWDB.update(TABLE_NAME_STRENGTH,contentValues,null,null);
        mWDB.update(TABLE_NAME_MUSCLE,contentValues,null,null);
        mWDB.update(TABLE_NAME_SHAPE,contentValues,null,null);
        mWDB.update(TABLE_NAME_FAT,contentValues,null,null);
        return  mWDB.update(TABLE_NAME_RECOVERY,contentValues,null,null);
    }
    public int  getFlag (int mode , int part ){
        String tableName;
        int getFlag = -1;
        switch (mode){
            case 1:
                tableName = TABLE_NAME_HIIT;
                break;
            case 2:
                tableName = TABLE_NAME_STRENGTH;
                break;
            case 3:
                tableName = TABLE_NAME_MUSCLE;
                break;
            case 4:
                tableName = TABLE_NAME_SHAPE;
                break;
            case 5:
                tableName = TABLE_NAME_FAT;
                break;
            case 6:
                tableName = TABLE_NAME_RECOVERY;
                break;
            default:
                tableName= TABLE_NAME_HIIT;
        }
        System.out.println("get flag === "+tableName+"   mode  "+mode +"  part "+part);
        Cursor cursor = mRDB.query(tableName,new String[]{"flag"},"id = ?",new String[]{String.valueOf(part)},null,null,null);
        while(cursor.moveToNext()){
            getFlag = cursor.getInt(cursor.getColumnIndexOrThrow("flag"));
        }
        return getFlag;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
