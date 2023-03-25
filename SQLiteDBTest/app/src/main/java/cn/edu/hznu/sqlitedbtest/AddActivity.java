package cn.edu.hznu.sqlitedbtest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddActivity extends AppCompatActivity {
    private Button btnSave;
    MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        btnSave = (Button)findViewById(R.id.btnSave);
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookName = ((TextView)findViewById(R.id.txtBookName)).getText().toString();
                String author   = ((TextView)findViewById(R.id.txtAuthor)).getText().toString();
                String pages    = ((TextView)findViewById(R.id.txtPages)).getText().toString();
                String price    = ((TextView)findViewById(R.id.txtPrice)).getText().toString();

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", bookName);
                values.put("author", author);
                values.put("pages", pages);
                values.put("price", price);
                db.insert("Book", null, values);

                String msg_add="数据已经成功添加，请查询确认";
                Intent intent=new Intent();
                intent.putExtra("msg_add",msg_add);
                setResult(RESULT_OK,intent);

                finish();
            }
        });
    }

}
