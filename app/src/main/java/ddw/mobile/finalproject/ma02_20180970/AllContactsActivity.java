package ddw.mobile.finalproject.ma02_20180970;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AllContactsActivity extends AppCompatActivity {
    final int UPDATE_CODE = 200;
    final int ADD_CODE = 100;

    ListView lvContacts;
    MyCursorAdapter adapter;
    ArrayList<ContactDto> contactList;
    ContactDBManager contactDBManager;

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);

        editText = findViewById(R.id.editSearch_my);
        lvContacts = (ListView)findViewById(R.id.lvContacts);

        //helper = new ContactDBHelper(this);
        contactDBManager = new ContactDBManager(this);
        contactList = contactDBManager.getAllTrip();

        //MyCursorAdapter 객체 생성
        adapter = new MyCursorAdapter(this, R.layout.listview_trip, contactList);
        lvContacts.setAdapter(adapter);

        //수정
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ContactDto trip = contactList.get(position);
                Intent intent = new Intent(AllContactsActivity.this, UpdateContactActivity.class);
                intent.putExtra("trip", trip);
                startActivityForResult(intent, UPDATE_CODE);
                adapter.notifyDataSetChanged();
            }
        });

        //삭제
        lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, final long id) {
                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(AllContactsActivity.this);
                builder.setTitle("여행기록 삭제")
                        .setMessage(contactList.get(pos).getPlace() + "을(를) 삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (contactDBManager.removeTrip(contactList.get(pos).get_id())) {
                                    Toast.makeText(AllContactsActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                                    contactList.clear();
                                    contactList.addAll(contactDBManager.getAllTrip());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(AllContactsActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();

                return true;
            }
        });
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSearch_my:
                String string = editText.getText().toString();

                contactDBManager.getTripByPlace(string);
                if (string.getBytes().length <= 0) {
                    Toast.makeText(AllContactsActivity.this, "검색할 내용을 입력하지 않아 취소되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    contactList.clear();
                    contactList.addAll(contactDBManager.getTripByPlace(string));
                    if (contactDBManager.getTripByPlace(string).size() == 0)
                        Toast.makeText(AllContactsActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(AllContactsActivity.this, "검색 결과입니다.", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }

                break;
            case R.id.btnAddNewContact:
                Intent intent = new Intent (this, InsertContactActivity.class);
                startActivityForResult(intent, ADD_CODE);
                break;
            case R.id.btnBack:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CODE) {  // InsertContactActivity 호출 후 결과 확인
            switch (resultCode) {
                case RESULT_OK:
                    String trip = data.getStringExtra("trip");
                    Toast.makeText(this, trip + "추가 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "추가 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        else if (requestCode == UPDATE_CODE) {    // UpdateContactActivity 호출 후 결과 확인
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "수정 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        contactList.clear();
        contactList.addAll(contactDBManager.getAllTrip());
        adapter.notifyDataSetChanged();
    }
}