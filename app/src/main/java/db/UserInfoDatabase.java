package db;

import static com.example.freezecl4_zcx.StartActivity.cpuid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import entity.UploadUserInfo;
import entity.User;
import entity.UserInfo;

public class UserInfoDatabase extends SQLiteOpenHelper {
    private static  final  String DB_NAME ="userInfoDatabase";
    private static  final String USER_TABLE_NAME ="user";
    private static  final String USERiNFO_TABLE_NAME ="userinfo";
    //记录当前最大的userID 用来创建用户信息时给userid 赋值。（避免重复）
    private static  final String MAXIDSQL ="maxSql";
    //记录当前最大的userInfoID 用来创建用户治疗信息时给userInfoid 赋值。（避免重复）
    private static  final String MAXINFOIDSQL = "maxInfoIDSql";
    private static  UserInfoDatabase userInfoDatabase = null;
    private SQLiteDatabase mWDB = null;
    private SQLiteDatabase mRDB = null;

    private UserInfoDatabase(Context context){
        super(context,DB_NAME,null,2);
    }
    public static  UserInfoDatabase getInstance(Context context){
        if(userInfoDatabase ==null){
            userInfoDatabase = new UserInfoDatabase(context);
        }
        return userInfoDatabase;
    }
    public SQLiteDatabase openWriteDB(){
        if(mWDB==null||!mWDB.isOpen()) {
            mWDB = userInfoDatabase.getWritableDatabase();
        }
        return mWDB;
    }
    public SQLiteDatabase openReadDB(){
        if(mRDB==null||!mRDB.isOpen()) {
            mRDB = userInfoDatabase.getWritableDatabase();
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
        String CreateUserTableSQL ="CREATE TABLE IF NOT EXISTS " +USER_TABLE_NAME +" ("+
                "userID INTEGER PRIMARY KEY ," +
                "firstName TEXT," +
                "lastName TEXT," +
                "gender INTEGER," +
                "age TEXT," +
                "createDate TEXT," +
                "e_mail TEXT," +
                "phone_num TEXT" +
                ")";
        db.execSQL(CreateUserTableSQL);
        String CreateUserInfoTableSQL ="CREATE TABLE IF NOT EXISTS "+USERiNFO_TABLE_NAME +"(" +
                "userID INTEGER, " +
                "userInfoID INTEGER," +
                "work_mode TEXT," +
                "p12_mode TEXT," +
                "p34_mode TEXT," +
                "rf12_mode TEXT," +
                "rf34_mode TEXT," +
                "mode_f1 TEXT," +
                "mode_f2 TEXT," +
                "mode_f3 TEXT," +
                "mode_options TEXT," +
                "mode_duration TEXT," +
                "work_date TEXT," +
                "body_part TEXT," +
                "body_part_ TEXT," +
                "machine TEXT," +
                "L_forehead_current TEXT," +
                "R_forehead_current TEXT" +
                ")";
        db.execSQL(CreateUserInfoTableSQL);
        String createMAXIDSQL ="CREATE TABLE IF NOT EXISTS " + MAXIDSQL +" (" +
                "maxID INTEGER "+
                ")";

        db.execSQL(createMAXIDSQL);
        ContentValues values = new ContentValues();
        values.put("maxID",0);
        db.insert(MAXIDSQL,null,values);
        String createMAXINFOIDSQL ="CREATE TABLE IF NOT EXISTS "+MAXINFOIDSQL +" ("+
                "userID INTEGER ," +
                "userInfoID INTEGER " +
                ")";
        db.execSQL(createMAXINFOIDSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //查询缩有用户信息
    public List<User>queryAllUser(){
        List<User> users = new ArrayList<>();
        Cursor cursor = mRDB.query(false,USER_TABLE_NAME,null,null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            User user = new User();
            user.setUserID(cursor.getInt(cursor.getColumnIndexOrThrow("userID")));
            user.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("firstName")));
            user.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("lastName")));
            user.setAge(cursor.getString(cursor.getColumnIndexOrThrow("age")));
            user.setGender(cursor.getInt(cursor.getColumnIndexOrThrow("gender"))==1?true:false);
            user.setCreateDate(cursor.getString(cursor.getColumnIndexOrThrow("createDate")));
            user.setE_mail(cursor.getString(cursor.getColumnIndexOrThrow("e_mail")));
            user.setPhone_num(cursor.getString(cursor.getColumnIndexOrThrow("phone_num")));
            users.add(user);
        }
        return users;
    }
    //根据姓名查询用户信息
    public List<User> queryUser(String name){
        List<User> users = new ArrayList<>();
        Cursor cursor = mRDB.query(false,USER_TABLE_NAME,null,"firstName || lastName like ?",new String[]{"%"+name+"%"},null,null,null,null,null);
        while(cursor.moveToNext()){
            User user = new User();
            user.setUserID(cursor.getInt(cursor.getColumnIndexOrThrow("userID")));
            user.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("firstName")));
            user.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("lastName")));
            user.setAge(cursor.getString(cursor.getColumnIndexOrThrow("age")));
            user.setGender(cursor.getInt(cursor.getColumnIndexOrThrow("gender"))==1?true:false);
            user.setCreateDate(cursor.getString(cursor.getColumnIndexOrThrow("createDate")));
            user.setE_mail(cursor.getString(cursor.getColumnIndexOrThrow("e_mail")));
            user.setPhone_num(cursor.getString(cursor.getColumnIndexOrThrow("phone_num")));
            users.add(user);
        }
        return users;
    }
    //查询某一个用户信息
    public User queryUserOne(String userID){
        Cursor cursor = mRDB.query(false,USER_TABLE_NAME,null,"userID =?",new String[]{userID},null,null,null,null,null);
        User user = null;
        while (cursor.moveToNext()){
            user = new User();
            user.setUserID((cursor.getInt(cursor.getColumnIndexOrThrow("userID"))));
            user.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("firstName")));
            user.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("lastName")));
            user.setAge(cursor.getString(cursor.getColumnIndexOrThrow("age")));
            user.setGender(cursor.getInt(cursor.getColumnIndexOrThrow("gender"))==1?true:false);
            user.setCreateDate(cursor.getString(cursor.getColumnIndexOrThrow("createDate")));
            user.setE_mail(cursor.getString(cursor.getColumnIndexOrThrow("e_mail")));
            user.setPhone_num(cursor.getString(cursor.getColumnIndexOrThrow("phone_num")));
        }
        return user;
    }
    public List<UserInfo> queryAllUserInfo(){

        Cursor cursor = mRDB.query(false,USERiNFO_TABLE_NAME,null,null,null,null,null,null,null);
        List<UserInfo> userInfos = new ArrayList<>();

        while(cursor.moveToNext()){
            UserInfo userInfo = new UserInfo();
            userInfo.setUserID(cursor.getInt(cursor.getColumnIndexOrThrow("userID")));
            userInfo.setUserInfoID(cursor.getInt(cursor.getColumnIndexOrThrow("userInfoID")));
            userInfo.setWork_mode(cursor.getString(cursor.getColumnIndexOrThrow("work_mode")));
            userInfo.setP12_mode(cursor.getString(cursor.getColumnIndexOrThrow("p12_mode")));
            userInfo.setP34_mode(cursor.getString(cursor.getColumnIndexOrThrow("p34_mode")));
            userInfo.setRf12_mode(cursor.getString(cursor.getColumnIndexOrThrow("rf12_mode")));
            userInfo.setRf34_mode(cursor.getString(cursor.getColumnIndexOrThrow("rf34_mode")));
            userInfo.setMode_f1(cursor.getString(cursor.getColumnIndexOrThrow("mode_f1")));
            userInfo.setMode_f2(cursor.getString(cursor.getColumnIndexOrThrow("mode_f2")));
            userInfo.setMode_f3(cursor.getString(cursor.getColumnIndexOrThrow("mode_f3")));
            userInfo.setMode_options(cursor.getString(cursor.getColumnIndexOrThrow("mode_options")));
            userInfo.setMode_duration(cursor.getString(cursor.getColumnIndexOrThrow("mode_duration")));
            userInfo.setWork_date(cursor.getString(cursor.getColumnIndexOrThrow("work_date")));
            userInfo.setBody_part(cursor.getString(cursor.getColumnIndexOrThrow("body_part")));
            userInfo.setBody_part_(cursor.getString(cursor.getColumnIndexOrThrow("body_part_")));
            userInfo.setMachine(cursor.getString(cursor.getColumnIndexOrThrow("machine")));
            userInfo.setL_forehead_current(cursor.getString(cursor.getColumnIndexOrThrow("L_forehead_current")));
            userInfo.setR_forehead_current(cursor.getString(cursor.getColumnIndexOrThrow("R_forehead_current")));
            userInfos.add(userInfo);
        }
        return userInfos;
    }
    //查询某一个用户的缩有用户治疗信息
    public List<UserInfo>  queryOneUserTOUserInfo(String userID){
        Cursor cursor = mRDB.query(false,USERiNFO_TABLE_NAME,null,"userID = ?",new String[]{userID},null,null,null,null,null);
        List<UserInfo> userInfos = new ArrayList<>();

        while(cursor.moveToNext()){
            UserInfo userInfo = new UserInfo();
            userInfo.setUserID(cursor.getInt(cursor.getColumnIndexOrThrow("userID")));
            userInfo.setUserInfoID(cursor.getInt(cursor.getColumnIndexOrThrow("userInfoID")));
            userInfo.setWork_mode(cursor.getString(cursor.getColumnIndexOrThrow("work_mode")));
            userInfo.setP12_mode(cursor.getString(cursor.getColumnIndexOrThrow("p12_mode")));
            userInfo.setP34_mode(cursor.getString(cursor.getColumnIndexOrThrow("p34_mode")));
            userInfo.setRf12_mode(cursor.getString(cursor.getColumnIndexOrThrow("rf12_mode")));
            userInfo.setRf34_mode(cursor.getString(cursor.getColumnIndexOrThrow("rf34_mode")));
            userInfo.setMode_f1(cursor.getString(cursor.getColumnIndexOrThrow("mode_f1")));
            userInfo.setMode_f2(cursor.getString(cursor.getColumnIndexOrThrow("mode_f2")));
            userInfo.setMode_f3(cursor.getString(cursor.getColumnIndexOrThrow("mode_f3")));
            userInfo.setMode_options(cursor.getString(cursor.getColumnIndexOrThrow("mode_options")));
            userInfo.setMode_duration(cursor.getString(cursor.getColumnIndexOrThrow("mode_duration")));
            userInfo.setWork_date(cursor.getString(cursor.getColumnIndexOrThrow("work_date")));
            userInfo.setBody_part(cursor.getString(cursor.getColumnIndexOrThrow("body_part")));
            userInfo.setBody_part_(cursor.getString(cursor.getColumnIndexOrThrow("body_part_")));
            userInfo.setMachine(cursor.getString(cursor.getColumnIndexOrThrow("machine")));
            userInfo.setL_forehead_current(cursor.getString(cursor.getColumnIndexOrThrow("L_forehead_current")));
            userInfo.setR_forehead_current(cursor.getString(cursor.getColumnIndexOrThrow("R_forehead_current")));
            userInfos.add(userInfo);
        }
        return userInfos;
    }
    //查询某一个治疗信息
    public UserInfo queryOneUserInfo(String userID,String userInfoID){
        Cursor cursor = mRDB.query(false,USERiNFO_TABLE_NAME,null,"userID = ? and userInfoID =?",new String[]{userID,userInfoID},null,null,null,null);
        UserInfo userInfo = new UserInfo();
        while(cursor.moveToNext()){
            userInfo.setUserID(cursor.getInt(cursor.getColumnIndexOrThrow("userID")));
            userInfo.setUserInfoID(cursor.getInt(cursor.getColumnIndexOrThrow("userInfoID")));
            userInfo.setWork_mode(cursor.getString(cursor.getColumnIndexOrThrow("work_mode")));
            userInfo.setP12_mode(cursor.getString(cursor.getColumnIndexOrThrow("p12_mode")));
            userInfo.setP34_mode(cursor.getString(cursor.getColumnIndexOrThrow("p34_mode")));
            userInfo.setRf12_mode(cursor.getString(cursor.getColumnIndexOrThrow("rf12_mode")));
            userInfo.setRf34_mode(cursor.getString(cursor.getColumnIndexOrThrow("rf34_mode")));
            userInfo.setMode_f1(cursor.getString(cursor.getColumnIndexOrThrow("mode_f1")));
            userInfo.setMode_f2(cursor.getString(cursor.getColumnIndexOrThrow("mode_f2")));
            userInfo.setMode_f3(cursor.getString(cursor.getColumnIndexOrThrow("mode_f3")));
            userInfo.setMode_options(cursor.getString(cursor.getColumnIndexOrThrow("mode_options")));
            userInfo.setMode_duration(cursor.getString(cursor.getColumnIndexOrThrow("mode_duration")));
            userInfo.setWork_date(cursor.getString(cursor.getColumnIndexOrThrow("work_date")));
            userInfo.setBody_part(cursor.getString(cursor.getColumnIndexOrThrow("body_part")));
            userInfo.setBody_part_(cursor.getString(cursor.getColumnIndexOrThrow("body_part_")));
            userInfo.setMachine(cursor.getString(cursor.getColumnIndexOrThrow("machine")));
            userInfo.setL_forehead_current(cursor.getString(cursor.getColumnIndexOrThrow("L_forehead_current")));
            userInfo.setR_forehead_current(cursor.getString(cursor.getColumnIndexOrThrow("R_forehead_current")));
        }
        return userInfo;
    }
//    添加用户
    public long insertUsers(User user){
        ContentValues contentValues = new ContentValues();
        contentValues.put("userID",user.getUserID());
        contentValues.put("firstName",user.getFirstName());
        contentValues.put("lastName",user.getLastName());
        contentValues.put("gender",user.isGender());
        contentValues.put("age",user.getAge());
        contentValues.put("createDate",user.getCreateDate());
        contentValues.put("e_mail",user.getE_mail());
        contentValues.put("phone_num",user.getPhone_num());
        System.out.println(contentValues.toString());
        //增加记录infoid字段
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("userID",user.getUserID());
        contentValues1.put("userInfoID",0);
        mWDB.insert(MAXINFOIDSQL,null,contentValues1);

        //增加记录最大用户id 的字段
        updateMaxID(user.getUserID());

        return mWDB.insert(USER_TABLE_NAME,null,contentValues);
    }
    //添加用户治疗信息
    public long insertUserinfo(UserInfo userInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("userID",userInfo.getUserID());
        contentValues.put("userInfoID",userInfo.getUserInfoID());
        contentValues.put("work_mode",userInfo.getWork_mode());
        contentValues.put("p12_mode",userInfo.getP12_mode());
        contentValues.put("p34_mode",userInfo.getP34_mode());
        contentValues.put("rf12_mode",userInfo.getRf12_mode());
        contentValues.put("rf34_mode",userInfo.getRf34_mode());
        contentValues.put("mode_f1",userInfo.getMode_f1());
        contentValues.put("mode_f2",userInfo.getMode_f2());
        contentValues.put("mode_f3",userInfo.getMode_f3());
        contentValues.put("mode_options",userInfo.getMode_options());
        contentValues.put("mode_duration",userInfo.getMode_duration());
        contentValues.put("work_date",userInfo.getWork_date());
        contentValues.put("body_part",userInfo.getBody_part());
        contentValues.put("body_part_",userInfo.getBody_part_());
        contentValues.put("machine",userInfo.getPan());
        contentValues.put("L_forehead_current",userInfo.getL_forehead_current());
        contentValues.put("R_forehead_current",userInfo.getR_forehead_current());
        //修改记录infoid 字段。
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("userInfoID",userInfo.getUserInfoID());
        mWDB.update(MAXINFOIDSQL,contentValues1,"userID =? ",new String[]{String.valueOf(userInfo.getUserID())});
        System.out.println("save " + userInfo);
        return mWDB.insert(USERiNFO_TABLE_NAME,null,contentValues);
    }
    //统计现有的用户信息的总量
    public int contUser(){
        String sql ="SELECT COUNT(*) FROM "+USER_TABLE_NAME ;
        Cursor cursor = mWDB.rawQuery(sql,null);
        int i = 0;
        if(cursor !=null){
            cursor.moveToNext();
            i = Integer.parseInt(cursor.getString(0));
        }
        return i;
    }

    public int countUserInfo(String userID){
        String sql = "SELECT COUNT(*) FROM "+USERiNFO_TABLE_NAME +
                "where " +
                "userID =" + userID;
        Cursor cursor = mWDB.rawQuery(sql,null);
        int i = 0;
        if(cursor !=null){
            cursor.moveToNext();
            i = Integer.parseInt(cursor.getString(0));
        }
        return i;
    }
    //删除用户信息
    public long deleteUser(String userID){
        if(mWDB.delete(USER_TABLE_NAME,"userID = ?",new String[]{userID})>0){
            if(mWDB.delete(USERiNFO_TABLE_NAME,"userID =?",new String[]{userID})>0){
                return 1;
            }
            return 1;
        }
        return 0;
    }
//    删除用户治疗信息
    public long deleteUserInfo(String userID,String userInfoID){
        if(mWDB.delete(USERiNFO_TABLE_NAME,"userID = ? and userInfoID =? ",new String[]{userID,userInfoID})>0){
            return 1;
        }
        return 0;
    }
//  查询用户治疗信息 的userInfoid 的最大值
    public int queryMaxUserInfo(String userID){
        String sql = "Select userInfoID  from "+MAXINFOIDSQL+" where userID = "+userID+" order by CAST(userInfoID as UNSIGNED) DESC LIMIT 1";
        Cursor cursor = mWDB.rawQuery(sql,null);
        if(cursor!=null&&cursor.moveToNext()){
            return Integer.parseInt(cursor.getString(0));
        }
        return 0;
    }
    //修改用户信息
    public long updateUser(User user){
        ContentValues values = new ContentValues();
        values.put("firstName", user.getFirstName());
        values.put("lastName", user.getLastName());
        values.put("gender", user.isGender() ? 1 : 0); // Convert boolean to integer
        values.put("age", user.getAge());
        values.put("createDate", user.getCreateDate());
        values.put("e_mail",user.getE_mail());
        values.put("phone_num",user.getPhone_num());
        return mWDB.update(USER_TABLE_NAME, values, "userID = ?", new String[] { String.valueOf(user.getUserID()) });
    }
    public long updateInfoWeight(String weight, String userID , String infoID){
        ContentValues values = new ContentValues();
        values.put("L_forehead_current",weight);
        return mWDB.update(USERiNFO_TABLE_NAME,values,"userID = ? and userInfoID = ? ",new String[]{userID,infoID});
    }
    public long updateInfoHeight(String height , String userID , String infoID){
        ContentValues values = new ContentValues();
        values.put("R_forehead_current",height);
        return mWDB.update(USERiNFO_TABLE_NAME,values,"userID = ? and userInfoID = ? ",new String[]{userID,infoID});
    }
//    修改用户治疗信息的日期
    public long updateUserInfoCreateDate(String userID,String userInfoID,String date){
        ContentValues values = new ContentValues();
        values.put("work_date",date);
        String[]whereArgs  = {userID,userInfoID};
        return mWDB.update(USERiNFO_TABLE_NAME,values,"userID = ? and userInfoID = ?",whereArgs);
    }
    //查询用户信息表中最大的userid
    public int selectUserMaxID(){
        String select  ="select maxID from "+MAXIDSQL;
        Cursor cursor = mWDB.rawQuery(select,null);
        if(cursor.moveToNext()){
            return cursor.getInt(cursor.getColumnIndexOrThrow("maxID"));
        }
        return 0;
    }
    //修改 最大 userid
    public long updateMaxID(int id){
        ContentValues contentValues = new ContentValues();
        contentValues.put("maxID",id);
        return mWDB.update(MAXIDSQL,contentValues,null,null);
    }
    // 批量查找（当之前没有连接到网络时， 忽然连接到网络并且有添加或者有修改痕迹时。则通过这些 userid 来查询用户信息来上传到服务器中）
    public List<User> batchQueryUser(List<String> userIDs){
        List<User> users = new ArrayList<>();
        for(String userID:userIDs){
            User user = new User();
            user =queryUserOne(userID);
            user.setMachine(cpuid);
            users.add(user);
        }
        return users;
    }
    //批量查找(同上)
        //遍历用户治疗信息
    public List<UserInfo> batchQueryUserInfo(List<UploadUserInfo> uploadUserInfos){
        List<UserInfo> userInfos1 = new ArrayList<>();
        for(UploadUserInfo uploadUserInfo :uploadUserInfos){
            UserInfo userInfo = new UserInfo();
            userInfo = queryOneUserInfo(uploadUserInfo.getUserID(),uploadUserInfo.getUserInfoID());
            userInfo.setMachine(cpuid);
            userInfos1.add(userInfo);
        }
        return userInfos1;
    }
    //删除所有
    public int deleteAllUser(){
       return mWDB.delete(USER_TABLE_NAME,null,null);
    }
    public int deleteAllUserInfo(){
        return mWDB.delete(USERiNFO_TABLE_NAME,null,null);
    }
    //远程同步（不可打断。）
    public void insertUsersInfoAndUsersAll(List<User>users,List<UserInfo> userInfos){
        System.out.println("insert users info "+users.toString());
        System.out.println("insert users info "+userInfos.toString());
        //事务开始
        mWDB.beginTransaction();
        deleteAllUser();
        deleteAllUserInfo();
        int i =0;
        try{
//            遍历添加
            for(User user :users){
                insertUsers(user);
                //记录id
                i=i<user.getUserID() ? user.getUserID():i;
                System.out.println("maxID"+i);
                updateMaxID(i);
            }
            for(UserInfo userInfo:userInfos){
                insertUserinfo(userInfo);
            }
            //事务成功
            mWDB.setTransactionSuccessful();
        }finally {
            //事务结束
            mWDB.endTransaction();
        }
    }

}
