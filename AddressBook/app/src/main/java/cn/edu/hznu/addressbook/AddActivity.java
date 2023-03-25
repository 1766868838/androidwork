package cn.edu.hznu.addressbook;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    GloablId gloablId = new GloablId();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addaddress);
        dbHelper = new MyDatabaseHelper(this,"addressList",null,1);
        Button add_Btn =  findViewById(R.id.add_Btn);
        EditText name =  findViewById(R.id.name);
        EditText number =  findViewById(R.id.number);

        add_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().trim().equals("")||number.getText().toString().equals("")) {
                    Toast.makeText(AddActivity.this, "姓名或号码不能为空", Toast.LENGTH_SHORT).show();
                }
                else{
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("id", gloablId.getId());
                    values.put("name", String.valueOf(name.getText()));
                    values.put("mobile", String.valueOf(number.getText()));
                    db.insert("contact", null , values);
                    gloablId.setId(gloablId.getId()+1);
                    AddActivity.this.finish();
                }

            }
        });
    }
}