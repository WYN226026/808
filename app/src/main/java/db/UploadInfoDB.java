package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import entity.UploadUserInfo;
        // UploadUserInfo 这个类的 变量有 userID 和InfoID 。为用户治疗 痕迹表中的实体类。
//该数据库中有4 个表
        //用户信息 ： 添加痕迹表 (添加和修改都用同一个数据库)
//                  删除痕迹表
        //用户治疗信息； 添加痕迹表 (添加和修改都用同一个数据库)
////                   删除痕迹表
public class UploadInfoDB extends SQLiteOpenHelper {
    private static  final  String DB_NAME ="updateInfo.DB";
    //用户添加痕迹表
    private static  final  String USER_INSERT_TABLE_NAME ="userInsertORDelete";
    //用户删除痕迹表
    private static  final  String USER_DELETE_TABLE_NAME ="userDelete";
    //用户治疗 添加痕迹 表
    private static  final  String USERINFO_INSERT_TABLE_NAME ="userInfoInsertORDelete";
    //用户治疗 删除痕迹 表
    private static  final  String USERINFO_DELETE_TABLE_NAME ="userInfoDelete";
    private static UploadInfoDB uploadInfoDB = null;
    private SQLiteDatabase mWDB = null;
    private SQLiteDatabase mRDB =null;
    private UploadInfoDB(Context context){
        super(context,DB_NAME,null,1);
    }
    public static UploadInfoDB getInstance(Context context){
        if(uploadInfoDB ==null){
            uploadInfoDB = new UploadInfoDB(context);
        }
        return uploadInfoDB;
    }
    public SQLiteDatabase openWriteDB(){
        if(mWDB == null||!mWDB.isOpen()){
            mWDB = uploadInfoDB.getWritableDatabase();
        }
        return mWDB;
    }
    public SQLiteDatabase openReadDB(){
        if(mRDB == null ||!mRDB.isOpen()){
            mRDB = uploadInfoDB.getReadableDatabase();
        }
        return mRDB;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //增加或修改
        String createUserInsertORUpdate = "CREATE TABLE IF NOT EXISTS "+USER_INSERT_TABLE_NAME +"(" +
                "userID varchar(30)" +
                ")";
        //删除
        String createUserDelete = "CREATE TABLE IF NOT EXISTS "+USER_DELETE_TABLE_NAME+"(" +
                "userID varchar(30)" +
                ")";
        //增加或修改
        String createUserInfoInsertORUpdate="CREATE TABLE IF NOT EXISTS "+USERINFO_INSERT_TABLE_NAME +"(" +
                "userID varchar(30)," +
                "userinfoID varchar(30)" +
                ")";
        //删除
        String createUserInfoDelete ="CREATE TABLE IF NOT EXISTS "+USERINFO_DELETE_TABLE_NAME +"(" +
                "userID varchar(30) ," +
                "userinfoID varchar(30)" +
                ")";
        db.execSQL(createUserInsertORUpdate);
        db.execSQL(createUserDelete);
        db.execSQL(createUserInfoInsertORUpdate);
        db.execSQL(createUserInfoDelete);
    }

  //查询某一个用户添加信息。
    public String queryUserInsert(String userID){
        String id  = "";
        Cursor query = mWDB.query(USER_INSERT_TABLE_NAME, null, "userID = ?", new String[]{userID}, null, null, null, null);
        if(query !=null && query.moveToFirst()){
            int columnIndex = query.getColumnIndex("userID");
            if(columnIndex !=-1){
                id = query.getString(columnIndex);
                System.out.println("result "+ query);
            }
            query.close();
        }
        return id;
    }
    //删除用户添加信息
    public long  deleteUserInsert(String userID){
        return mWDB.delete(USER_INSERT_TABLE_NAME,"userID = ?",new String[]{userID});
    }
    //删除用户 删除信息
    public long deleteUserDelete(String userID){
        return mWDB.delete(USER_DELETE_TABLE_NAME,"userID = ?",new String[]{userID});
    }

    //添加用户添加信息
    public long insertUserInsert(String userid){
        if("".equals(this.queryUserInsert(userid))){
            ContentValues contentValues = new ContentValues();
            contentValues.put("userID",userid);
            System.out.println("userID :" +userid);
            return mWDB.insert(USER_INSERT_TABLE_NAME,null,contentValues);
        }
        return 1;
    }
    //添加用户删除信息
    public long insertUserDelete(String userid){
        ContentValues contentValues = new ContentValues();
        contentValues.put("userID",userid);
        return mWDB.insert(USER_DELETE_TABLE_NAME,null,contentValues);
    }

    //用户信息：增 ，删 ， 查
    //uploadUserInfo: userID,infoID;
    //查询某一个 用户治疗 添加信息
    public UploadUserInfo queryUserInfoInsertS(String userID,String userInfoID) {
        Cursor cursor = mRDB.query(USERINFO_INSERT_TABLE_NAME, null, "userID =? and userInfoID =?", new String[]{userID,userInfoID}, null, null, null, "1");
        UploadUserInfo uploadUserInfo = new UploadUserInfo();

        if (cursor != null && cursor.moveToFirst()) {
            uploadUserInfo.setUserID(cursor.getString(cursor.getColumnIndexOrThrow("userID")));
            uploadUserInfo.setUserInfoID(cursor.getString(cursor.getColumnIndexOrThrow("userinfoID")));
            cursor.close();
        } else {
            return null;
        }
        return uploadUserInfo;
    }


    //删除用户治疗信息 的添加痕迹
    public long deleteUserInfoInsert(String userID,String infoID){
        return mWDB.delete(USERINFO_INSERT_TABLE_NAME,"userID = ? and userinfoID = ?",new String[]{userID,infoID});
    }
    //删除某个治疗信息 的删除痕迹
    public long deleteUserInfoDelete(String userID,String infoID){
        return mWDB.delete(USERINFO_DELETE_TABLE_NAME,"userID = ? and userinfoID = ? ",new String[]{userID,infoID});
    }
    //删除某一个用户的治疗信息的所有 删除痕迹
    public long deleteUserInfoDeleteUser(String userID){
        return mWDB.delete(USERINFO_INSERT_TABLE_NAME,"userID = ?",new String[]{userID});
    }

    //添加一个治疗信息 的添加痕迹
    public long insertUserInfoInsert(String userID,String infoID){
        if(this.queryUserInfoInsertS(userID,infoID)==null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("userID",userID);
            contentValues.put("userinfoID",infoID);
            System.out.println("userID :" +infoID);
            return mWDB.insert(USERINFO_INSERT_TABLE_NAME,null,contentValues);
        }
        return 1;
    }

    //添加一个治疗信息 的删除痕迹
    public long insertUserInfoDelete(String userID,String infoID){
        ContentValues contentValues = new ContentValues();
        contentValues.put("userID",userID);
        contentValues.put("userinfoID",infoID);
        return mWDB.insert(USERINFO_DELETE_TABLE_NAME,null,contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //查询所有
    //用户表
    //查询说有的用户信息的添加痕迹
    public List<String> queryAllUserUploadInsert(){
        List<String> userIDs = new ArrayList<>();
        Cursor query = mWDB.query(USER_INSERT_TABLE_NAME, null, null, null, null, null, null);
        while (query.moveToNext()){
            String userID = query.getString(query.getColumnIndexOrThrow("userID"));
            userIDs.add(userID);
        }
        return userIDs;
    }
    //查询缩有的用户信息的删除痕迹
    public List<String> queryAllUserUploadDelete(){
        List<String> userIDs = new ArrayList<>();
        Cursor cursor = mWDB.query(USER_DELETE_TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String userID = cursor.getString(cursor.getColumnIndexOrThrow("userID"));
            userIDs.add(userID);
        }
        return userIDs;
    }
    //查询缩有用户治疗信息的添加痕迹
    public List<UploadUserInfo> queryAllUserInfoUploadInsert(){
        List<UploadUserInfo> userInfos = new ArrayList<>();
        Cursor cursor = mWDB.query(USERINFO_INSERT_TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            UploadUserInfo uploadUserInfo = new UploadUserInfo();
            uploadUserInfo.setUserID(cursor.getString(cursor.getColumnIndexOrThrow("userID")));
            uploadUserInfo.setUserInfoID(cursor.getString(cursor.getColumnIndexOrThrow("userinfoID")));
            userInfos.add(uploadUserInfo);
        }
        return userInfos;
    }
    //查询缩有用户治疗信息的删除痕迹
    public List<UploadUserInfo> queryAllUserInfoUploadInsertDelete(){
        List<UploadUserInfo> userInfos = new ArrayList<>();
        Cursor cursor = mWDB.query(USERINFO_DELETE_TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            UploadUserInfo uploadUserInfo = new UploadUserInfo();
            uploadUserInfo.setUserID(cursor.getString(cursor.getColumnIndexOrThrow("userID")));
            uploadUserInfo.setUserInfoID(cursor.getString(cursor.getColumnIndexOrThrow("userinfoID")));
            userInfos.add(uploadUserInfo);
        }
        return userInfos;
    }

    //删除缩有。。。。
    public long deleteAllUploadUserInsert(){
        return mRDB.delete(USERINFO_INSERT_TABLE_NAME,null,null);
    }
    public long deleteAllUploadUserInfoInsert(){
        return mRDB.delete(USER_INSERT_TABLE_NAME,null,null);
    }
    public long deleteAllUploadUserInfoDelete(){
        return mRDB.delete(USERINFO_DELETE_TABLE_NAME,null,null);
    }
    public long deleteAllUploadUserDelete(){
        return mRDB.delete(USER_DELETE_TABLE_NAME,null,null);
    }
}
