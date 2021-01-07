package ddw.mobile.finalproject.ma02_20180970;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ContactDBManager {

    ContactDBHelper contactDBHelper = null;
    Cursor cursor = null;

    public ContactDBManager(Context context) {
        contactDBHelper = new ContactDBHelper(context);
    }

    //DB의 모든 데이터 반환
    public ArrayList<ContactDto> getAllTrip() {
        ArrayList<ContactDto> tripList = new ArrayList<ContactDto>();
        SQLiteDatabase db = contactDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ContactDBHelper.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex(ContactDBHelper.COL_ID));
            int image = cursor.getInt((cursor.getColumnIndex(ContactDBHelper.COL_IMG)));
            String place = cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_PLACE));
            String date = cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_DATE));
            String days = cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_DAYS));

            tripList.add(new ContactDto(id, image, place, date, days));
        }
        cursor.close();
        contactDBHelper.close();
        return tripList;
    }

    //DB에 새로운 trip 추가
    public boolean addNewTrip(ContactDto newTrip) {
        SQLiteDatabase db = contactDBHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(ContactDBHelper.COL_IMG, newTrip.getImg());
        value.put(ContactDBHelper.COL_PLACE, newTrip.getPlace());
        value.put(ContactDBHelper.COL_DATE, newTrip.getDate());
        value.put(ContactDBHelper.COL_DAYS, newTrip.getDays());

//      insert 메소드를 사용할 경우 데이터 삽입이 정상적으로 이루어질 경우 1 이상, 이상이 있을 경우 0 반환 확인 가능
        long count = db.insert(ContactDBHelper.TABLE_NAME, null, value);
        contactDBHelper.close();
        if (count > 0)
            return true;
        return false;
    }

    public boolean modifyTrip(ContactDto trip) {
        SQLiteDatabase sqLiteDatabase = contactDBHelper.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put(ContactDBHelper.COL_IMG, trip.getImg());
        row.put(ContactDBHelper.COL_PLACE, trip.getPlace());
        row.put(ContactDBHelper.COL_DATE, trip.getDate());
        row.put(ContactDBHelper.COL_DAYS, trip.getDays());

        String whereClause = ContactDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(trip.get_id()) };
        int result = sqLiteDatabase.update(ContactDBHelper.TABLE_NAME, row, whereClause, whereArgs);
        contactDBHelper.close();
        if (result > 0) return true;
        return false;
    }

    public boolean removeTrip(long id) {
        SQLiteDatabase sqLiteDatabase = contactDBHelper.getWritableDatabase();
        String whereClause = ContactDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        int result = sqLiteDatabase.delete(ContactDBHelper.TABLE_NAME, whereClause,whereArgs);
        contactDBHelper.close();
        if (result > 0) return true;
        return false;
    }

    public ArrayList<ContactDto> getTripByPlace(String searchPlace) {
        ArrayList<ContactDto> tripList = new ArrayList<ContactDto>();
        SQLiteDatabase db = contactDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ContactDBHelper.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            String trip = cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_PLACE));
            if (trip.equals(searchPlace)) {
                long id = cursor.getInt(cursor.getColumnIndex(ContactDBHelper.COL_ID));
                int image = cursor.getInt((cursor.getColumnIndex(ContactDBHelper.COL_IMG)));
                String date = cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_DATE));
                String days = cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_DAYS));

                tripList.add(new ContactDto(id, image, trip, date, days));
            }
        }
        cursor.close();
        contactDBHelper.close();
        return tripList;
    }

    public void close() {
        if (contactDBHelper != null) contactDBHelper.close();
        if (cursor != null) cursor.close();
    }
}
