package ddw.mobile.finalproject.ma02_20180970;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

    public class DetailActivity extends AppCompatActivity {

        EditText etName;
        EditText etAddress;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail);

            etName = findViewById(R.id.etPlaceDetail);
            etAddress = findViewById(R.id.etAddressDetail);

            Intent intent = getIntent();
            String name = intent.getStringExtra("name");
            String address = intent.getStringExtra("address");

            etName.setText(name);
            etAddress.setText(address);
        }

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnClose:
                    finish();
                    break;
            }
        }
}
