package cn.edu.hznu.providertest;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ModifyActivity extends AppCompatActivity {

    TextView txtBookName;
    TextView txtAuthor;
    TextView txtPages;
    TextView txtPrice;

    Button btnSave;
    String newId;

    String bookName;
    String author;
    String pages;
    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        Intent intent=getIntent();
        bookName=intent.getStringExtra("bookName");
        author=intent.getStringExtra("author");
        pages=intent.getStringExtra("pages");
        price=intent.getStringExtra("price");
        newId=intent.getStringExtra("id");

        txtBookName = (TextView)findViewById(R.id.txtBookName);
        txtAuthor = (TextView)findViewById(R.id.txtAuthor);
        txtPages = (TextView)findViewById(R.id.txtPages);
        txtPrice = (TextView)findViewById(R.id.txtPrice);
        btnSave =(Button)findViewById(R.id.btnSave);

        txtBookName.setText(bookName);
        txtAuthor.setText(author);
        txtPages.setText(pages);
        txtPrice.setText(price);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新数据
                Uri uri = Uri.parse("content://cn.edu.hznu.sqlitedbtest.provider/book");
                ContentValues values = new ContentValues();
                values.put("name", txtBookName.getText().toString());
                values.put("author", txtAuthor.getText().toString());
                values.put("pages", txtPages.getText().toString());
                values.put("price",txtPrice.getText().toString());
                ContentResolver resolver = getContentResolver();
                resolver.update(uri, values, "id=?", new String[]{newId});
                finish();
            }
        });

        Button btnDelete = (Button)findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除数据
                Uri uri = Uri.parse("content://cn.edu.hznu.sqlitedbtest.provider/book/" + newId);
                getContentResolver().delete(uri, null, null);
                finish();
            }
        });
    }
}
