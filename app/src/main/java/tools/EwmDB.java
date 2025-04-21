package tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class EwmDB extends SQLiteOpenHelper {
    private static final String TABLE_NAME ="EwmDB";
    private static  final String flag = "flag";
    private static EwmDB dbHelder=null;
    private SQLiteDatabase mWDB=null;
    private SQLiteDatabase mRDB=null;
    private static final int DATABASE_VERSION = 1;
    public EwmDB(@Nullable Context context) {
        super(context,TABLE_NAME, null, DATABASE_VERSION);
    }

    public static EwmDB getInstance(Context context){
        if(dbHelder==null){
            dbHelder= new EwmDB(context);
        }
        return dbHelder;
    }
    public SQLiteDatabase openWriteDB(){
        if(mWDB==null ||mWDB.isOpen()){
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
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql="create table "+TABLE_NAME +" ("+flag +" INTEGER )";
        sqLiteDatabase.execSQL(sql);
        ContentValues values = new ContentValues();
        values.put(flag,1);
        sqLiteDatabase.insert(TABLE_NAME,null,values);
    }
    public int updateTable(int flag){
        ContentValues values = new ContentValues();
        values.put("flag",flag);
        return mWDB.update(TABLE_NAME,values,null,null);
    }
    public int getFlag(){
        Cursor cursor = mRDB.query(TABLE_NAME,new String[]{"flag"},null,null,null,null,null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndexOrThrow("flag"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
