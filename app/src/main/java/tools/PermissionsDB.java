package tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//远程控制权限数据库
public class PermissionsDB extends SQLiteOpenHelper {
    private static  final String DB_NAME = "permissionDatabase";
    private static  final String PermissionTableName = "permission";
    private SQLiteDatabase mWDB = null;
    private SQLiteDatabase mRDB = null;
    private static  PermissionsDB permissionsDB = null;
    private PermissionsDB(Context context){
        super(context,DB_NAME,null,1);
    }
    public static  PermissionsDB getInstance(Context context){
        if(permissionsDB == null){
            permissionsDB = new PermissionsDB(context);
        }
            return permissionsDB;
    }
    public SQLiteDatabase openWriteDB(){
        if(mWDB==null||!mWDB.isOpen()) {
            mWDB = permissionsDB.getWritableDatabase();
        }
        return mWDB;
    }
    public SQLiteDatabase openReadDB(){
        if(mRDB==null||!mRDB.isOpen()) {
            mRDB = permissionsDB.getWritableDatabase();
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
        String CreatePermissionTableSql= "CREATE TABLE IF NOT EXISTS "+PermissionTableName +" (" +
                "flag1 integer ," +      //flag1 为强制关闭和开启。  0 时为关。 1 为开。 默认为 1
                "flag2 integer ," +      //flag2 为开启计时功能。    0为关。 1 为开。    默认为 0
                "timing integer," +      //timing 记录时间。为整型。记录类型为(分钟) 默认为50000-1分钟
                "timingSecond integer" + //记录时间， 记录类型为 秒。 默认为60;
                ")";
        db.execSQL(CreatePermissionTableSql);
        ContentValues values = new ContentValues();
        values .put("flag1",1);
        values .put("flag2",0);
        values .put("timing",59999);
        values. put("timingSecond",60);
        db.insert(PermissionTableName,null,values);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public int queryFlag(String flag){
        Cursor cursor = mRDB.query(false,PermissionTableName,new String []{flag},null,null,null,null,null,null,null);
        int i = -1 ;
        while(cursor.moveToNext()){
            i = cursor.getInt(cursor.getColumnIndexOrThrow(flag));
        }
        return  i ;
    }
    public long updateFlag(String flag,int num){
        ContentValues values = new ContentValues();
        values.put(flag,num);
         return mWDB.update(PermissionTableName,values,null,null);
    }


}
