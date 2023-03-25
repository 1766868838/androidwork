package cn.edu.hznu.sqlitedbtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private TextView txtShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);
        Button createDatabase = (Button) findViewById(R.id.create_database);
        txtShow=(TextView)findViewById(R.id.txtShow);
        createDatabase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });
        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(MainActivity.this, AddActivity.class);
                //startActivityForResult(intent,1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 454);
                values.put("price", 16.96);
                db.insert("Book", null, values);
                values.clear();
                values.put("name", "The Lost Symbol");
                values.put("author", "Dan Brown");
                values.put("pages", 510);
                values.put("price", 19.95);
                db.insert("Book", null, values);

            }
        });
        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 10.99);
                db.update("Book", values, "name = ?",
                        new String[] { "The Da Vinci Code" });
            }
        });
        Button deleteButton = (Button) findViewById(R.id.delete_data);
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Book", "pages > ?", new String[] { "500" });
            }
        });
        Button queryButton = (Button) findViewById(R.id.query_data);
        queryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Uri uri = Uri
                //        .parse("content://com.example.hznu.sqlitedbtest.provider/book");
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                Cursor cursor = db.query("Book", null, null,null,null, null, null);
                StringBuilder sb=new StringBuilder();
                if (cursor != null) {
                    int no=0;
                    while (cursor.moveToNext()) {
                        no++;
                        String name = cursor.getString(cursor
                                .getColumnIndex("name"));
                        String author = cursor.getString(cursor
                                .getColumnIndex("author"));
                        int pages = cursor.getInt(cursor
                                .getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor
                                .getColumnIndex("price"));
                        Log.d("MainActivity", "book name:" + name);
                        Log.d("MainActivity", "book author is " + author);
                        Log.d("MainActivity", "book pages is " + pages);
                        Log.d("MainActivity", "book price is " + price);
                        //show in TextView component
                        sb.append(""+no+": "+name+", "+author+", "+pages+", "+price+"\n");

                    }
                    txtShow.setText(sb.toString());
                    cursor.close();
                }
            }
        });
        Button replaceData = (Button) findViewById(R.id.replace_data);
        replaceData.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.beginTransaction();
                try {
                    db.delete("Book", null, null);
                    // if (true) {
                    // throw new NullPointerException();
                    // }
                    ContentValues values = new ContentValues();
                    values.put("name", "Game of Thrones");
                    values.put("author", "George Martin");
                    values.put("pages", 720);
                    values.put("price", 20.85);
                    db.insert("Book", null, values);
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    String msg_add=data.getStringExtra("msg_add");
                    txtShow.setText(msg_add);
                }
        }
    }
}
