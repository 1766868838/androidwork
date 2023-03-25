package cn.edu.hznu.end;

import static androidx.recyclerview.widget.ItemTouchHelper.Callback.makeFlag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {
    MyDatabaseHelper myDatabaseHelper;
    SQLiteDatabase db;
    private final List<Word> wordList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ContentValues values = new ContentValues();
        values.put("state", 2);
        RecyclerView recyclerView = findViewById(R.id.recycler_view2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        dataUpdate();

        reviewListAdapter adapter = new reviewListAdapter(wordList);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                //方向
                int dragFlags= ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                //控制快速滑动的方向（一般是左右）
                int swipeFlags = ItemTouchHelper.LEFT;

                return makeMovementFlags(dragFlags, swipeFlags);//计算movement flag值
            }
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //拖拽处理
                Log.d("47Review", String.valueOf(viewHolder.getItemId()));
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //滑动处理

                Log.d("54Review", String.valueOf(viewHolder.getLayoutPosition()));
                db.update(Data.getUsername(),values, "id = ?", new String[]
                        {String.valueOf(wordList.get(viewHolder.getLayoutPosition()).getId())});
                wordList.remove(viewHolder.getLayoutPosition());
                adapter.notifyItemRemoved(viewHolder.getLayoutPosition());


            }
            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
            }
            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);


        //关联recyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView);

        ImageView reviewText = findViewById(R.id.reviewText);
        ImageView studyText = findViewById(R.id.studyText);
        ImageView readText = findViewById(R.id.readText);
        ImageView myText = findViewById(R.id.myText);

        reviewText.setOnClickListener(new clickListener());
        studyText.setOnClickListener(new clickListener());
        readText.setOnClickListener(new clickListener());
        myText.setOnClickListener(new clickListener());

    }

    private void dataUpdate() {
        wordList.clear();
        String username = Data.getUsername();
        myDatabaseHelper = new MyDatabaseHelper(this, "words.db", null, 2);
        db = myDatabaseHelper.getWritableDatabase();
        String sql = "select * from "+username+" natural join words where state = 1";
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

    class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if(v.getId()==R.id.studyText){
                Intent intent = new Intent(ReviewActivity.this,MemoryActivity.class);
                startActivity(intent);
            }
            else if(v.getId()==R.id.readText){
                Intent intent = new Intent(ReviewActivity.this,ReadActivity.class);
                startActivity(intent);
            }
            else if(v.getId()==R.id.myText){
                Intent intent = new Intent(ReviewActivity.this,MyActivity.class);
                startActivity(intent);
            }
        }
    }
}