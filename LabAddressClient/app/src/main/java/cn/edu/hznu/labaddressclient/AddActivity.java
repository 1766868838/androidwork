package cn.edu.hznu.labaddressclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Button add_Btn =  findViewById(R.id.add_Btn);
        EditText nameText =  findViewById(R.id.name);
        EditText numberText =  findViewById(R.id.number);
        add_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("content://cn.edu.hznu.addressbook.provider/addressList");
                ContentValues values = new ContentValues();
                String name = nameText.getText().toString();
                String mobile = numberText.getText().toString();
                if(name.equals("")||mobile.equals("")){
                    Toast.makeText(AddActivity.this,"姓名或手机号码不能为空",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    values.put("name", String.valueOf(nameText.getText()));
                    values.put("mobile", String.valueOf(numberText.getText()));
                    uri = getContentResolver().insert(uri,values);
                    Cursor cursor = getContentResolver().query(uri,
                            null,
                            null,
                            null,
                            null);
                    Log.d("MainActivity", String.valueOf(cursor.getCount()));
                    cursor.close();
                    AddActivity.this.finish();
                }

            }
        });


    }
}