package ddw.mobile.finalproject.ma02_20180970;

import android.text.Html;
import android.text.Spanned;

import java.io.Serializable;

public class ContactDto implements Serializable {

    private long _id;
    private int img;
    private String place;   //여행지
    private String date;    //여행 날짜
    private String days;    //여행 일수

    public ContactDto() {

    }
    public ContactDto(long _id, int img, String place, String date, String days) {
        this._id = _id;
        this.img = img;
        this.place = place;
        this.date = date;
        this.days = days;
    }
    public ContactDto( int img, String place, String date, String days) {
        this.img = img;
        this.place = place;
        this.date = date;
        this.days = days;
    }

    public long get_id() {
        return _id;
    }
    public void set_id(long _id) {
        this._id = _id;
    }

    public int getImg() {
        return img;
    }
    public void setImg(int img) {
        this.img = img;
    }

    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getDays() {
        return days;
    }
    public void setDays(String days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return _id + "번째 데이터: " + place + " / " + date + " / " + days;
    }
}
