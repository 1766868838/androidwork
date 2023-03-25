package cn.edu.hznu.providertest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddActivity extends AppCompatActivity {
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        btnSave = (Button)findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookName = ((TextView)findViewById(R.id.txtBookName)).getText().toString();
                String author   = ((TextView)findViewById(R.id.txtAuthor)).getText().toString();
                String pages    = ((TextView)findViewById(R.id.txtPages)).getText().toString();
                String price    = ((TextView)findViewById(R.id.txtPrice)).getText().toString();

                Uri uri = Uri.parse("content://cn.edu.hznu.sqlitedbtest.provider/book");

                ContentValues values = new ContentValues();
                values.put("name", bookName);
                values.put("author", author);
                values.put("pages", pages);
                values.put("price", price);

                Uri newUri = getContentResolver().insert(uri, values);

                finish();
            }
        });
    }

}
