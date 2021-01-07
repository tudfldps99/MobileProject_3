package ddw.mobile.finalproject.ma02_20180970;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class NaverTripXmlParser {

    //xml에서 읽어들일 태그를 구분한 enum -> 정수값 등으로 구분하지 않고 가독성 높은 방식을 사용
    public enum TagType { NONE, TITLE, ADDRESS, ROADADDRESS };

    private final static String FAULT_RESULT = "faultResult";

    private final static String ITEM_TAG = "item";
    private final static String TITLE_TAG = "title";
    private final static String ADDRESS_TAG = "address";
    private final static String ROADADDRESS_TAG = "roadAddress";

    public NaverTripXmlParser() {
    }

    public ArrayList<NaverTripDto> parse(String xml) {
        ArrayList<NaverTripDto> resultList = new ArrayList<NaverTripDto>();
        NaverTripDto dto = null;

        TagType tagType = TagType.NONE;     //태그를 구분하기 위한 enum 변수 초기화

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            //Boilerplate Code : 특정 기능을 구현할 때 반복적으로 비슷한 형태로 나타나는 코드

            int eventType = parser.getEventType();      // 태그 유형 구분 변수 준비

            while (eventType != XmlPullParser.END_DOCUMENT) {       // parsing 수행 - for문 또는 while문으로 구성
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if (tag.equals(ITEM_TAG)) {     // 새로운 항목을 표현하는 태그를 만났을 경우 dto 객체 생성
                            dto = new NaverTripDto();
                        // 관심 태그를 만났을 경우 태그의 타입을 저장
                        } else if (tag.equals(TITLE_TAG)) {
                            if (dto != null)
                                tagType = TagType.TITLE;
                        } else if (tag.equals(ADDRESS_TAG)) {
                            if (dto != null)
                                tagType = TagType.ADDRESS;
                        } else if (tag.equals(ROADADDRESS_TAG)) {
                            if (dto != null)
                                tagType = TagType.ROADADDRESS;
                        }
                        break;
                    case XmlPullParser.END_TAG:     // 아이템의 마지막을 나타내는 태그 확인 시 DTO를 리스트에 저장
                        if (parser.getName().equals(ITEM_TAG)) {
                            resultList.add(dto);
                            dto = null;     //dbo는 비워야 하므로 null로 초기화
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {       // 태그의 유형에 따라 dto에 값 저장
                            //앞서 읽은 태그사이의 텍스트 값을 태그의 타입에 따라 구후분 후 DTO에 저장
                            case TITLE:
                                dto.setTitle(parser.getText());
                                break;
                            case ADDRESS:
                                dto.setAddress(parser.getText());
                                break;
                            case ROADADDRESS:
                                dto.setRoadAddress(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;     //태그 타입 초기화
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
