package tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDataBase extends SQLiteOpenHelper {

    public LocalDataBase(Context context) {
        super(context, "local.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE local(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "f1 INT, f2 INT, f3 INT, f4 INT, f5 INT, f6 INT, f7 INT, f8 INT, " +
                "f9 INT, f10 INT," + "f11 INT, f12 INT, f13 INT, f14" +
                " INT, f15 INT, f16 INT, f17 INT, f18 INT,rf12 int, rf34 int , p12 int ,p34 int )";
        db.execSQL(sql);

        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor getRowById(int rowId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("local", null, "id=?", new String[]{String.valueOf(rowId)}, null, null, null);
    }

    private void insertInitialData(SQLiteDatabase db) {
        //女胳膊                                                                                                           f1 f2  .................................................)");//胳膊
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(15,20,10,20,30,10,22,25,15,25,35,15,28,35,15,80,65,70)");
        //男胳膊
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(20,30,10,25,38,15,25,40,18,35,40,20,30,40,20,80,60,70)");
        //女腹部
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(20,28,10,25,30,15,28,35,18,28,38,20,30,38,20,65,60,75)");
        //男腹部
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(30,35,15,35,40,20,35,45,18,28,38,20,30,38,20,75,65,70)");
        //女大腿
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(20,30,10,20,35,10,20,35,10,20,35,10,20,35,10,85,50,65)");
        //男大腿
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(25,38,15,30,40,20,35,45,20,35,45,30,35,48,35,70,55,65)");
        //女小腿
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(10,32,10,20,35,15,20,35,10,20,35,15,20,30,10,85,55,70)");
        //男小腿
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(15,30,10,20,35,15,25,35,20,30,38,20,30,40,25,85,66,70)");
        //女脖颈
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(10,30,5,5,30,5,5,30,10,5,30,5,5,30,10,75,60,70)");
        //男脖颈
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(15,25,8,15,25,10,25,30,15,25,40,15,28,45,20,80,60,75)");
        //女背部
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(10,30,5,10,30,15,10,30,20,10,30,15,10,30,20,60,55,75)");
        //男背部
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(20,30,10,25,35,15,30,40,18,35,45,20,35,48,25,70,65,60)");
        //女臀部
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(20,35,15,20,40,10,20,45,15,20,40,10,20,50,15,70,55,60)");
        //男臀部
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(30,40,18,35,45,20,38,48,20,40,45,30,40,45,20,80,50,70)");
        //女后大腿
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(15,32,10,15,35,15,15,35,10,15,35,15,15,35,10,85,60,70)");
        //男大腿
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(18,30,15,20,35,18,25,35,20,30,40,20,30,45,25,90,60,75)");
        //女小腿
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(10,32,10,20,35,15,20,35,10,20,35,15,20,35,10,85,55,70)");
        //男小腿
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(15,30,10,20,35,15,25,35,20,30,38,20,30,40,25,85,60,70)");

        db.execSQL("INSERT INTO local(f1,f2,f3,f4) VALUES(1,1,1,0)");
        db.execSQL("INSERT INTO local(f1,f2,f3,f4,f5,f6) VALUES(0,0,3,3,3,3)");
        db.execSQL("INSERT INTO local(f1) VALUES(0)");
    }
    public void recover(){
        //                                                                                                           f1 f2  .................................................)");//胳膊
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("local",null,null);
        //女胳膊                                                                                                           f1 f2  .................................................)");//胳膊
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(1,15,20,10,20,30,10,22,25,15,25,35,15,28,35,15,80,65,70)");
        //男胳膊id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(2,20,30,10,25,38,15,25,40,18,35,40,20,30,40,20,80,60,70)");
        //女腹部id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(3,20,28,10,25,30,15,28,35,18,28,38,20,30,38,20,65,60,75)");
        //男腹部id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(4,30,35,15,35,40,20,35,45,18,28,38,20,30,38,20,75,65,70)");
        //女大腿id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(5,20,30,10,20,35,10,20,35,10,20,35,10,20,35,10,85,50,65)");
        //男大腿id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(6,25,38,15,30,40,20,35,45,20,35,45,30,35,48,35,70,55,65)");
        //女小腿id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(7,10,32,10,20,35,15,20,35,10,20,35,15,20,30,10,85,55,70)");
        //男小腿id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(8,15,30,10,20,35,15,25,35,20,30,38,20,30,40,25,85,66,70)");
        //女脖颈id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(9,10,30,5,5,30,5,5,30,10,5,30,5,5,30,10,75,60,70)");
        //男脖颈id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(10,15,25,8,15,25,10,25,30,15,25,40,15,28,45,20,80,60,75)");
        //女背部id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(11,10,30,5,10,30,15,10,30,20,10,30,15,10,30,20,60,55,75)");
        //男背部id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(12,20,30,10,25,35,15,30,40,18,35,45,20,35,48,25,70,65,60)");
        //女臀部id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(13,20,35,15,20,40,10,20,45,15,20,40,10,20,50,15,70,55,60)");
        //男臀部id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(14,30,40,18,35,45,20,38,48,20,40,45,30,40,45,20,80,50,70)");
        //女后大腿id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(15,15,32,10,15,35,15,15,35,10,15,35,15,15,35,10,85,60,70)");
        //男大腿id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(16,18,30,15,20,35,18,25,35,20,30,40,20,30,45,25,90,60,75)");
        //女小腿id,
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(17,10,32,10,20,35,15,20,35,10,20,35,15,20,35,10,85,55,70)");
        //男小腿
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18) VALUES(18,15,30,10,20,35,15,25,35,20,30,38,20,30,40,25,85,60,70)");
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4) VALUES(19,1,1,1,0)");
        db.execSQL("INSERT INTO local(id,f1,f2,f3,f4,f5,f6) VALUES(20,0,0,3,3,3,3)");
        db.execSQL("INSERT INTO local(id,f1) VALUES(21,0)");
    }
}
