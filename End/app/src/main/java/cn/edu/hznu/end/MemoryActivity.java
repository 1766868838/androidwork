package cn.edu.hznu.end;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MemoryActivity extends AppCompatActivity {
    String username = Data.getUsername();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //String username = bundle.getString("name");
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "words.db", null, 2);
        dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d("1234",username);
        Cursor cursorLearning = db.query(Data.getUsername(),null,"state=?",new String[]{"0"},null,null,null);
        Cursor cursorLearned = db.query(Data.getUsername(),null,null,null,null,null,null);
        int countLearning = 20 - cursorLearning.getCount();
        int countLearned = cursorLearned.getCount();
        ContentValues values = new ContentValues();
        db.beginTransaction();
        while((countLearning--)>0){
            values.put("id",++countLearned);
            db.insert(Data.getUsername(),null,values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        setContentView(R.layout.activity_memory);
        Button study_button = findViewById(R.id.study_button);
        ImageView reviewText = findViewById(R.id.reviewText);
        ImageView studyText = findViewById(R.id.studyText);
        ImageView readText = findViewById(R.id.readText);
        ImageView myText = findViewById(R.id.myText);

        startService(new Intent(this,FloatingButtonService.class));
        study_button.setOnClickListener(new clickListener());
        reviewText.setOnClickListener(new clickListener());
        studyText.setOnClickListener(new clickListener());
        readText.setOnClickListener(new clickListener());
        myText.setOnClickListener(new clickListener());
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.study_button){
                Intent intent = new Intent(MemoryActivity.this,MainActivity.class);
                startActivity(intent);
            }
            else if(v.getId()==R.id.reviewText){
                Intent intent = new Intent(MemoryActivity.this,ReviewActivity.class);
                startActivity(intent);
            }
            else if(v.getId()==R.id.readText){
                Intent intent = new Intent(MemoryActivity.this,ReadActivity.class);
                startActivity(intent);
            }
            else if(v.getId()==R.id.myText){
                Intent intent = new Intent(MemoryActivity.this,MyActivity.class);
                startActivity(intent);
            }
        }
    }



    //在一个主界面(主Activity)通过意图跳转至多个不同子Activity上去，当子模块的代码执行完毕后再次返回主页面，将子activity中得到的数据显示在主界面/完成的数据交给主Activity处理。
    //第一个参数：这个整数requestCode用于与startActivityForResult中的requestCode中值进行比较判断，是以便确认返回的数据是从哪个Activity返回的。
    //第二个参数：这整数resultCode是由子Activity通过其setResult()方法返回。适用于多个activity都返回数据时，来标识到底是哪一个activity返回的值。
    //第三个参数：一个Intent对象，带有返回的数据。可以通过data.getXxxExtra( );方法来获取指定数据类型的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                    startService(new Intent(MemoryActivity.this, FloatingButtonService.class));
                }
            }
        }
    }

    //用户手动设置权限,用户点击按钮首先调用这个
    @SuppressLint("WrongViewCast")
    public void startFloatingButtonService(View view) {
        if (FloatingButtonService.isStarted) {
            view.setBackgroundResource(R.drawable.circle_grey);
            FloatingButtonService.isStarted=false;
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
            } else {
                view.setBackgroundResource(R.drawable.circle_blue);
                startService(new Intent(MemoryActivity.this, FloatingButtonService.class));
                FloatingButtonService.isStarted=true;
            }
        }

    }
}