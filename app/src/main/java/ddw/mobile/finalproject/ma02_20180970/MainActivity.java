package ddw.mobile.finalproject.ma02_20180970;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import noman.googleplaces.NRPlaces;
//import noman.googleplaces.Place; -> 어떤 방식을 써도 괜찮으나 지금 예제에서는 noman 패키지 Place는 import 없이 풀패키지명으로, Google의 기본 Place는 import 해서 사용
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG = "MainActivity";
    final static int PERMISSION_REQ_CODE = 100;

    EditText editText;
    ListView lvList;
    String apiAddress;

    String query;

    MyTripAdapter adapter;
    ArrayList<NaverTripDto> resultList;
    NaverTripXmlParser parser;
    NaverNetworkManager networkManager;
    ImageFileManager imgFileManager;        //파일과 관련한 작업


    private GoogleMap mGoogleMap;
    private Marker centerMarker;
    private LocationManager locationManager;
    Geocoder geocoder;

    private MarkerOptions markerOptions;
    private LatLng currentLoc;
    private String bestProvider;

    private PlacesClient placesClient;

    //우리나라 17개 시/도
    //서울, 인천, 부산, 대구, 광주, 대전, 울산, 경기도, 강원도, 충청북도, 충청남도, 전라북도, 전라남도, 경상북도, 경상남도, 제주도, 세종
    //광역시
    Double Seoul_lat = 37.65703986065283; Double Seoul_lng = 126.98133760956229; //서울
    Double Incheon_lat = 37.38718376113373; Double Incheon_lng = 126.70409879572959; //인천
    Double Busan_lat = 35.16932199485548; Double Busan_lng = 129.04150089915348; //부산
    Double Daegu_lat = 35.82986032410841; Double Daegu_lng = 128.556492157318; //대구
    Double Gwangiu_lat = 35.16284256304242; Double Gwangiu_lng = 126.82749689517533; //광주
    Double Daejeon_lat = 36.348067263980994; Double Daejeon_lng = 127.39243763633388; //대전
    Double Ulsan_lat = 35.54962652891334; Double Ulsan_lng = 129.25403359186862; //울산

    //도
    Double Gangwon_lat = 37.84067169000314; Double Gangwon_lng = 128.14365099316257;  //강원도
    Double Chungbuk_lat = 36.63368526973997; Double Chungbuk_lng = 127.48996444797758; //충청북도
    Double Chungnam_lat = 36.80540856994882; Double Chungnam_lng = 127.1988267502487; //충청남도
    Double Jeonbuk_lat = 35.831703532375265; Double Jeonbuk_lng = 127.11642928782418; //전라북도
    Double Jeonnam_lat = 34.809828556684906; Double Jeonnam_lng = 126.3913316096409; //전라남도
    Double Gyeongbuk_lat = 36.563123696664626; Double Gyeongbuk_lng = 128.7808580285018; //경상북도
    Double Gyeongnam_lat = 35.291000736522044; Double Gyeongnam_lng = 128.1436509794078; //경상남도

    //특별자치도
    Double Jeju_lat = 33.50735395725828; Double Jeju_lng = 126.52797527565787; //제주도

    Double Chuncheon_lat = 37.8861465586649; Double Chuncheon_lng = 127.73114258333706; //춘천
    Double Sokcho_lat = 38.188865747459005; Double Sokcho_lng = 128.5313546141723; //속초
    Double Gangneung_lat = 37.7318265579565; Double Gangneung_lng = 128.85269960293292; //강릉

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edText_want);
        lvList = findViewById(R.id.lvList);

        resultList = new ArrayList<NaverTripDto>();
        adapter = new MyTripAdapter(this, R.layout.listview_naver_trip, resultList);
        lvList.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.naver_api_url); //네이버 오픈 API에 접근하는 url
        parser = new NaverTripXmlParser();
        networkManager = new NaverNetworkManager(this); //context정보 = 앱이 실행되는 환경정보를 가지고 있음.

        networkManager.setClientId(getResources().getString(R.string.naver_client_id));
        networkManager.setClientSecret(getResources().getString(R.string.naver_client_secret));
        imgFileManager = new ImageFileManager(this);


        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        bestProvider = LocationManager.GPS_PROVIDER;

        mapLoad();

        Places.initialize(getApplicationContext(), getString(R.string.google_api_key));
        placesClient = Places.createClient(this);
    }

    public void onClick (View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btnSearch:
                query = editText.getText().toString();  // UTF-8 인코딩 필요
                // OpenAPI 주소와 query 조합 후 서버에서 데이터를 가져옴
                // 가져온 데이터는 파싱 수행 후 어댑터에 설정
                //버튼을 눌렀을때 네트워크 작업과 파싱 작업은 별도의 스레드에서 수행할 수 있도록 스레드를 만들어놓고, 스레드를 호출. (스레드보다는 AsyncTask)

                try { //query가 한글일 경우에 UTF-8로 인코딩 되어있지 않으면 한글이 꺼짐. but 예외사항 발생할 수 있기에 그냥 encode 써주면 빨간줄 생김. -> try-catch문 이용.
                    new NetworkAsyncTask().execute(apiAddress
                            + URLEncoder.encode(query, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MainActivity.this, "검색 후 '검색한 위치 주변'을 눌러주시면 지도를 볼 수 있습니다.\n<주의사항> 지도보기 가능 지역 : 서울/인천/부산/강원도/충북/충남/경북/경남/전북/전남/대구/광주/대전/울산/제주/춘천/속초/강릉 ", Toast.LENGTH_LONG).show();

                break;
            case R.id.btnOpenAllContact:
                Toast.makeText(MainActivity.this, "여행기록들 중 하나를 누르면 그 여행기록에 대한 수정 및 공유가 가능합니다. ", Toast.LENGTH_LONG).show();
                intent = new Intent(this, AllContactsActivity.class);
                break;

            case R.id.around_trip:      //현재 위치 주변
                searchStart(PlaceType.PARK);
                break;
            case R.id.Cancel:       //마커들 삭제
                mGoogleMap.clear();
                break;
            case R.id.ReadAMap:         //검색 위치 주변
                if (editText.getText().toString().equals("서울")) {
                    searchStart_search(PlaceType.PARK, Seoul_lat, Seoul_lng);
                } else if (editText.getText().toString().equals("인천")) {
                    searchStart_search(PlaceType.PARK, Incheon_lat, Incheon_lng);
                } else if (editText.getText().toString().equals("부산")) {
                    searchStart_search(PlaceType.PARK, Busan_lat, Busan_lng);
                } else if (editText.getText().toString().equals("대구")) {
                    searchStart_search(PlaceType.PARK, Daegu_lat, Daegu_lng);
                } else if (editText.getText().toString().equals("광주")) {
                    searchStart_search(PlaceType.PARK, Gwangiu_lat, Gwangiu_lng);
                } else if (editText.getText().toString().equals("대전")) {
                    searchStart_search(PlaceType.PARK, Daejeon_lat, Daejeon_lng);
                } else if (editText.getText().toString().equals("울산")) {
                    searchStart_search(PlaceType.PARK, Ulsan_lat, Ulsan_lng);
                } else if (editText.getText().toString().equals("제주도")) {
                    searchStart_search(PlaceType.PARK, Jeju_lat, Jeju_lng);
                } else if (editText.getText().toString().equals("춘천")) {
                    searchStart_search(PlaceType.PARK, Chuncheon_lat, Chuncheon_lng);
                } else if (editText.getText().toString().equals("속초")) {
                    searchStart_search(PlaceType.PARK, Sokcho_lat, Sokcho_lng);
                } else if (editText.getText().toString().equals("강릉")) {
                    searchStart_search(PlaceType.PARK, Gangneung_lat, Gangneung_lng);
                } else if (editText.getText().toString().equals("충청북도")) {
                    searchStart_search(PlaceType.PARK, Chungbuk_lat, Chungbuk_lng);
                } else if (editText.getText().toString().equals("충청남도")) {
                    searchStart_search(PlaceType.PARK, Chungnam_lat, Chungnam_lng);
                } else if (editText.getText().toString().equals("전라북도")) {
                    searchStart_search(PlaceType.PARK, Jeonbuk_lat, Jeonbuk_lng);
                } else if (editText.getText().toString().equals("전라남도")) {
                    searchStart_search(PlaceType.PARK, Jeonnam_lat, Jeonnam_lng);
                } else if (editText.getText().toString().equals("경상북도")) {
                    searchStart_search(PlaceType.PARK, Gyeongbuk_lat, Gyeongbuk_lng);
                } else if (editText.getText().toString().equals("경상남도")) {
                    searchStart_search(PlaceType.PARK, Gyeongnam_lat, Gyeongnam_lng);
                } else if (editText.getText().toString().equals("강원도")) {
                    searchStart_search(PlaceType.PARK, Gangwon_lat, Gangwon_lng);
                }

                break;
        }
        if (intent != null) startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_developer:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("개발자 소개")
                        .setIcon(R.drawable.my_img)
                        .setMessage("모바일 응용 수강생\n" + "20180970 컴퓨터학과 박승연입니다.\n")
                        .setPositiveButton("확인", null);
                Dialog dlg = builder1.create();
                dlg.setCanceledOnTouchOutside(false);
                dlg.show();
                return true;
            case R.id.menu_finish:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("앱 종료")
                        .setMessage("앱을 종료하시겠습니까?")
                        .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
                break;
        }
        return true;
    }

    class NetworkAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {     // 실행 전 동작. UI작업.
            super.onPreExecute();

            //진행 상태를 알려주는 다이얼로그 = ProgressDialog
            progressDlg = ProgressDialog.show(MainActivity.this, "Wait", "Downloading...");
        }
        @Override
        protected String doInBackground(String... strings) {        //별개의 스레드 작업. 네트워크 작업.
            String address = strings[0];
            String result = null;
            result = networkManager.downloadContents(address);

            //NaverNetworkManager 확인해보면, isOnline이 아니면 null이 반환됨. 따라서 null일때 처리 해줘야함.
            if (result == null) return "Error!";

            Log.d(TAG, result);

            // parsing
            resultList = parser.parse(result);
            //--> 초기에는 응답결과가 빠르지만, 스크롤을 내리면 이미지가 채워지는 시간이 걸리게됨.

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            adapter.setList(resultList);    // Adapter 에 결과 List 를 설정 후 notify
            progressDlg.dismiss();
        }
    }

    //현재 위치 주변
    private void searchStart(String type) {
        if (checkPermission()) {
            Location location = locationManager.getLastKnownLocation(bestProvider);
            //currentLoc = new LatLng(location.getLatitude(), location.getLongitude());

            new NRPlaces.Builder().listener(placesListener)
                    .key(getResources().getString(R.string.google_api_key))
                    .latlng(location.getLatitude(), location.getLongitude())
                    .radius(5000)
                    .type(type)
                    .build()
                    .execute();
        }
    }

    //검색 위치 주변
    private void searchStart_search(String type, Double lat, Double lng) {
        if (checkPermission()) {
            new NRPlaces.Builder().listener(placesListener)
                    .key(getResources().getString(R.string.google_api_key))
                    .latlng(lat, lng)
                    .radius(10000)
                    .type(type)
                    .build()
                    .execute();
        }
    }

    /*Place ID 의 장소에 대한 세부정보 획득*/    //p.13
    private void getPlaceDetail(String placeId) {
        //Place 잘 체크하기 (일반 개발자 만든  API vs Google API)
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.PHONE_NUMBER, Place.Field.ADDRESS);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener(
                new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse reponse) {
                        Place place = reponse.getPlace();
                        Log.d(TAG, "Place found: " + place.getName());
                        Log.d(TAG, "Address: " + place.getAddress());

                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("name", place.getName());
                        intent.putExtra("address", place.getAddress());

                        startActivity(intent);
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            Log.e(TAG, "Place not found: " + statusCode + " " + e.getMessage());
                        }
                    }
                }
        );

    }

    PlacesListener placesListener = new PlacesListener() {
        @Override
        public void onPlacesSuccess(final List<noman.googleplaces.Place> places) {       //noman.googlePlaces.Place가 import 되어 있다면 Google API의 Place와 충돌하므로 import 문을 삭제하여야 함. -> 풀패키지명을 사용하므로 import 불필요
            Log.d(TAG, "Adding Markers");

            //마커 추가 --> 시간이 많이 걸리는 작업이므로 별도의 스레드 작업으로 분리해서 수행
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (noman.googleplaces.Place place : places) {
                        markerOptions.title(place.getName());
                        markerOptions.position(new LatLng(place.getLatitude(), place.getLongitude()));
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_RED
                        ));
                        Marker newMarker = mGoogleMap.addMarker(markerOptions);
                        newMarker.setTag(place.getPlaceId());       //나중에 getTag로 꺼내면 됨
                        Log.d(TAG, place.getName() + " : " + place.getPlaceId());
                    }
                }
            });

        }

        @Override
        public void onPlacesFailure(PlacesException e) {
        }

        @Override
        public void onPlacesStart() {
        }

        @Override
        public void onPlacesFinished() {
        }
    };
        @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        markerOptions= new MarkerOptions();
        Log.d(TAG, "Map ready");

        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        if (checkPermission()) {
            mGoogleMap.setMyLocationEnabled(true);
        }

        //내 위치 확인 버튼 클릭 처리 리스너
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Toast.makeText(MainActivity.this, "현재 위치로 이동", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //지도 상의 현재 위치 아이콘 클릭 처리 리스너
        mGoogleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                Toast.makeText(MainActivity.this,
                        String.format("현재 위치: (%f, %f)", location.getLatitude(), location.getLongitude()),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //마커의 윈도우 클릭했을 때 확인하도록 구현하는 리스너
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String placeId = marker.getTag().toString();
                getPlaceDetail(placeId);

                Toast.makeText(MainActivity.this, "실제 주소: " + geoCoding(marker.getPosition()), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private String geoCoding(LatLng latLng) {
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        // 결과로부터 주소 추출
        Address addressList = addresses.get(0);
        return addressList.getAddressLine(0);
    }

    /*구글맵을 멤버변수로 로딩*/
    private void mapLoad() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);      // 매개변수 this: MainActivity 가 OnMapReadyCallback 을 구현하므로
    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]  {Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQ_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 퍼미션을 획득하였을 경우 맵 로딩 실행
                mapLoad();
            } else {
                // 퍼미션 미획득 시 액티비티 종료
                Toast.makeText(this, "앱 실행을 위해 권한 허용이 필요함", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}