package cn.edu.hznu.providertest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String newId;
    private List<Book> books=new ArrayList<Book>();
    private BookAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter= new BookAdapter(MainActivity.this, R.layout.book_item, books);
        listView=(ListView) findViewById(R.id.bookList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book=books.get(position);
                //打开修改页面
                Intent intent=new Intent(MainActivity.this,ModifyActivity.class);
                intent.putExtra("id",""+book.getId());
                intent.putExtra("bookName",book.getBookName());
                intent.putExtra("author",book.getAuthor());
                intent.putExtra("pages",""+book.getPages());
                intent.putExtra("price",""+book.getPrice());
                startActivity(intent);
            }
        });

        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        Button queryData = (Button) findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAll();
            }
        });

    }

    public void queryAll(){
        books.clear();
        // 查询数据
        Uri uri = Uri.parse("content://cn.edu.hznu.sqlitedbtest.provider/book");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        //cursor=getContentResolver().query(uri,new String[]{"name","author"},"price>?",new String[]{"20"},"name");


        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex ("id"));
                String name = cursor.getString(cursor. getColumnIndex("name"));
                String author = cursor.getString(cursor. getColumnIndex("author"));
                int pages = cursor.getInt(cursor.getColumnIndex ("pages"));
                double price = cursor.getDouble(cursor. getColumnIndex("price"));

                Book book=new Book();
                book.setId(id);
                book.setBookName(name);
                book.setAuthor(author);
                book.setPages(pages);
                book.setPrice(price);
                books.add(book);
                //Log.d("MainActivity", "Book name: " + name+" Author:"+ author+" Pages: "+pages+" Price:"+price);
            }
            cursor.close();
        }

        //更新ListView的数据
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryAll();
    }




}
