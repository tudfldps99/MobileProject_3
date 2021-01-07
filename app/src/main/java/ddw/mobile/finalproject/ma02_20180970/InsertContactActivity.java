package ddw.mobile.finalproject.ma02_20180970;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class InsertContactActivity extends AppCompatActivity {

    private static final String TAG = "InsertContactActivity";
    private static final int FILECHOOSE_REQ_CODE = 100;
    private ImageView imageView;

    ImageView addImg;
    EditText etPlace;
    //EditText etDate;
    EditText etDays;

    ContactDBHelper helper;

    Button btnAdd;

    Button btn;
    CalendarView calView;
    TextView tvYear, tvMonth, tvDay;
    int selectYear, selectMonth, selectDay;
    String text;

    ContactDBManager contactDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_contact);

        addImg = findViewById(R.id.imageView);
        etPlace = findViewById(R.id.etPlace);
        //etDate = findViewById(R.id.etDate);
        etDays = findViewById(R.id.etDays);

        btn = findViewById(R.id.button);
        calView = findViewById(R.id.calendarView);
        tvYear = findViewById(R.id.tvYear);
        tvMonth = findViewById(R.id.tvMonth);
        tvDay = findViewById(R.id.tvDay);

        helper = new ContactDBHelper(this);

        contactDBManager = new ContactDBManager(this);

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


        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent i = new Intent (Intent.ACTION_GET_CONTENT);
                  i.addCategory(Intent.CATEGORY_OPENABLE);
                  i.setType("image/*");
                  startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSE_REQ_CODE);
              }
          }
        );
        imageView = findViewById(R.id.imageView);
    }

    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.btnAddNewContact:
                boolean result = contactDBManager.addNewTrip(
                        new ContactDto(R.drawable.my_cat, etPlace.getText().toString(), text, etDays.getText().toString() )
                );
                if (result) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("trip", etPlace.getText().toString());
                    setResult(RESULT_OK, resultIntent);
                } else {
                    Toast.makeText(this, "새로운 여행지 추가 실패", Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
            case R.id.btnAddNewContactClose:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILECHOOSE_REQ_CODE && resultCode == RESULT_OK) {
            try {
                Log.i(TAG, "onActivityResult: " + data.getData());
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                in.close();
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}