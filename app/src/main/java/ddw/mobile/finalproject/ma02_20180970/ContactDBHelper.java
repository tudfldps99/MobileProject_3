package ddw.mobile.finalproject.ma02_20180970;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDBHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "contact_db";
    public final static String TABLE_NAME = "contact_table";
    public final static String COL_ID = "_id";
    public final static String COL_PLACE = "place";     //여행장소
    public final static String COL_DATE = "date";       //여행날짜
    public final static String COL_DAYS = "days";       //여행일

    public final static String COL_IMG="img";

    public ContactDBHelper (Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement,"
                + COL_IMG + " INTEGER, " + COL_PLACE + " TEXT, " + COL_DATE + " TEXT, " + COL_DAYS + " TEXT);");

        //샘플 데이터
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '"+R.drawable.busan+"', '부산', '20180709', '5');");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '"+R.drawable.chuncheon+"', '춘천', '20180827', '2');");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '"+R.drawable.seoul+"', '서울', '20190625', '3');");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '"+R.drawable.incheon+"', '인천', '201901012', '5');");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '"+R.drawable.busan+"', '부산', '20191223', '2');");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '"+R.drawable.jeju+"', '제주도', '20200305', '4');");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '"+R.drawable.gangneung+"', '강릉', '20200308', '6');");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '"+R.drawable.chuncheon+"', '춘천', '20200509', '3');");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '"+R.drawable.sokcho+"', '속초', '20201220', '7');");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '"+R.drawable.jeju+"', '제주도', '20201229', '4');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
    }
}
