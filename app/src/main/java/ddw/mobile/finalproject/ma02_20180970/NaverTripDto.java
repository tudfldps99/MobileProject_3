package ddw.mobile.finalproject.ma02_20180970;

import android.text.Html;
import android.text.Spanned;

public class NaverTripDto {

    private int _id;

    //xml을 parsing할 때 사용
    private String title;
    private String address;
    private String roadAddress;

    public int get_id() {
        return _id;
    }
    
    public void set_id(int _id) {
        this._id = _id;
    }
    
    public String getTitle() {
        //제목 부분 확인해보면 중간중간 태그들이 들어감 (ex-<b>). 이러한 태그들을 string으로 바꾸고자 할때 사용.
        Spanned spanned = Html.fromHtml(title);
        return spanned.toString();
//        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    @Override
    public String toString() {
        return "NaverTripDto{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", description='" + roadAddress + '\'' +
                '}';
    }
}
