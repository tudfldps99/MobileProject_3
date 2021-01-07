package ddw.mobile.finalproject.ma02_20180970;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateContactActivity extends AppCompatActivity {

    ContactDto trip;

    EditText etPlace;
    //EditText etDate;
    EditText etDays;
    ImageView image;

    Button btn;
    CalendarView calView;
    TextView tvYear, tvMonth, tvDay;
    int selectYear, selectMonth, selectDay;
    String text;

    Button btnSharing;

    ContactDBManager contactDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        trip = (ContactDto) getIntent().getSerializableExtra("trip");

        etPlace = findViewById(R.id.etPlace);
        //etDate = findViewById(R.id.etDate);
        etDays = findViewById(R.id.etDays);
        image = findViewById(R.id.imageView);

        btn = findViewById(R.id.button);
        calView = findViewById(R.id.calendarView);
        tvYear = findViewById(R.id.tvYear);
        tvMonth = findViewById(R.id.tvMonth);
        tvDay = findViewById(R.id.tvDay);

        btnSharing = findViewById(R.id.btnSharing);

        image.setImageResource(trip.getImg());
        etPlace.setHint(trip.getPlace());
        etDays.setHint(trip.getDays());

        //calendar 위젯 사용.
        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectYear = year;
                selectMonth = month + 1;
                selectDay = dayOfMonth;

                String y = String.valueOf(selectYear);
                String m = String.valueOf(selectMonth);
                String d = String.valueOf(selectDay);

                if (selectMonth <= 9) {
                    m = "0" + m;
                }
                if (selectDay <= 9) {
                    d = "0" + d;
                }

                text = y + m + d;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvYear.setText(Integer.toString(selectYear));
                tvMonth.setText(Integer.toString(selectMonth));
                tvDay.setText(Integer.toString(selectDay));
            }
        });

        btnSharing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent Sharing_intent = new Intent(Intent.ACTION_SEND);
                Sharing_intent.setType("text/plain");
                String Sharing_Message = "<다녀온 여행지 공유> \n" +
                        "여행 장소: " + trip.getPlace() + "\n" +
                        " / 여행 날짜(출발일): " + trip.getDate() + "\n" +
                        " / 여행일 수: " + trip.getDays();
                Sharing_intent.putExtra(Intent.EXTRA_TEXT, Sharing_Message);

                Intent Sharing = Intent.createChooser(Sharing_intent, "공유하기");
                startActivity(Sharing);
            }
        });

        contactDBManager = new ContactDBManager(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateContact:
                String place = etPlace.getText().toString();
                String date = text;
                String days = etDays.getText().toString();

                image.setImageResource(trip.getImg());
                trip.setPlace(place);
                trip.setDate(date);
                trip.setDays(days);

                boolean result = contactDBManager.modifyTrip(trip);

                if (result) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("trip", trip);
                    setResult(RESULT_OK, resultIntent);
                } else {
                    Toast.makeText(this, "항목을 입력해주세요", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                }
                finish();
                break;
            case R.id.btnUpdateContactClose:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }
}