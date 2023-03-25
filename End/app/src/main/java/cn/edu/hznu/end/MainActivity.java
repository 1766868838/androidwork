package cn.edu.hznu.end;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //private Bundle bundle;
    MyDatabaseHelper myDatabaseHelper;
    SQLiteDatabase db;
    int currentPosition;
    final List<Word> wordList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //bundle = getIntent().getExtras();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        dataUpdate();

        listAdapter adapter = new listAdapter(wordList);
        recyclerView.setAdapter(adapter);
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(recyclerView); //超好用的东西，滑动自动居中。

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (recyclerView != null && recyclerView.getChildCount() > 0) {
                        try {
                            // 获取居中的具体位置
                            currentPosition = ((RecyclerView.LayoutParams) linearSnapHelper.findSnapView(recyclerView.getLayoutManager()).getLayoutParams()).getViewAdapterPosition();
                            Log.d("MainActivitu", String.valueOf(currentPosition));
                        } catch (Exception e) {
                        }
                    }
                }
            }
        });
        Button comBtn = findViewById(R.id.complete);
        Button iComBtn = findViewById(R.id.incomplete);
        ImageView backImg = findViewById(R.id.study_Back);
        backImg.setOnClickListener(new clickListener());
        comBtn.setOnClickListener(new clickListener(adapter));
        iComBtn.setOnClickListener(new clickListener(adapter));
    }

    private void dataUpdate() {
        wordList.clear();
        String username = Data.getUsername();
        myDatabaseHelper = new MyDatabaseHelper(this, "words.db", null, 2);
        db = myDatabaseHelper.getWritableDatabase();
        String sql = "select * from "+username+" natural join words where state = 0";
        Cursor cursor = db.rawQuery(sql,null);
        //Cursor cursor = db.execSQL(sql);
        Log.d("MainActivitu", String.valueOf(cursor.getCount()));

        while(cursor.moveToNext()){

            int id = cursor.getInt(0);
            String word = cursor.getString(2);
            String eVoice = cursor.getString(3);
            String aVoice = cursor.getString(4);
            String explanation = cursor.getString(5);

            wordList.add(new Word(id,word,eVoice,aVoice,explanation));
        }
        cursor.close();
    }

    private class clickListener implements View.OnClickListener {
        Word word;
        ContentValues values = new ContentValues();
        listAdapter adapter;
        public clickListener(listAdapter adapter) {
            this.adapter = adapter;
        }
        public clickListener() {}
        @Override
        public void onClick(View v) {
            word = wordList.get(currentPosition);
            if(v.getId()==R.id.study_Back){
                finish();
            }
            if(v.getId()==R.id.complete){
                word.setState(2);
                Log.d("MainActivity",String.valueOf(word.getId()));
                values.put("state", 2);
                db.update(Data.getUsername(), values, "id = ?", new String[] {String.valueOf(word.getId())});
                if(wordList.isEmpty()){
                    ToastFormat toastFormat = new ToastFormat(MainActivity.this);
                    toastFormat.InitToast();
                    toastFormat.show();
                }
                wordList.remove(currentPosition);
                adapter.setData(wordList);
                adapter.notifyItemRemoved(currentPosition);
            }
            else if(v.getId()==R.id.incomplete){
                word.setState(1);
                values.put("state", 1);
                db.update(Data.getUsername(), values, "id = ?", new String[] {String.valueOf(word.getId())});
                wordList.remove(currentPosition);
                adapter.setData(wordList);
                adapter.notifyItemRemoved(currentPosition);
            }

        }
    }
}