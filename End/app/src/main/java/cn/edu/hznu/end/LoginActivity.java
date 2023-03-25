package cn.edu.hznu.end;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText name; //创建账号
    EditText passwd; //创建密码
    EditText passwd2;
    LinearLayout passwd3;
    Button loginBtn; //登录按钮
    Button signBtn;
    String user;
    String pwd;
    SQLiteDatabase db;
    Boolean flag = false; //点击注册按钮的次数，用来判断是否显示确认密码
    private MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DatabaseUtil.packDataBase(this);
        dbHelper = new MyDatabaseHelper(this,"words.db",null,2);
        dbHelper.getReadableDatabase();

        name = findViewById(R.id.name);
        passwd = findViewById(R.id.passwd);
        passwd2 = findViewById(R.id.passwd2);
        passwd3=findViewById(R.id.passwd3);
        loginBtn = findViewById(R.id.button);
        signBtn = findViewById(R.id.button2);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建两个String类，存储从输入文本框获取到的内容，并且去掉开头结尾的空格
                String user = name.getText().toString().trim();
                String pwd = passwd.getText().toString().trim();
                //查询数据
                //dbHelper.getWritableDatabase();
                db = dbHelper.getWritableDatabase();
                Cursor cursor1 = db.query("users",null,"name=?",new String[]{user},null,null,null);
                Cursor cursor2 = db.query("users",null,"name=?and password=?",new String[]{user,pwd},null,null,null);
                if(cursor1.getCount()>0) {
                    cursor1.moveToNext();
                    String password = cursor1.getString(1);
                    Log.d("1234",password);
                    Log.d("1234",pwd);
                    if (password.equals(pwd)) {
                        Data.setUsername(user);
                        Intent intent = new Intent(LoginActivity.this, MemoryActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = name.getText().toString().trim();
                String pwd = passwd.getText().toString().trim();
                db = dbHelper.getWritableDatabase();
                Cursor cursor1 = db.query("users",null,"name=?",new String[]{user},null,null,null);
                if(cursor1.getCount()>0){
                    Toast.makeText(LoginActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                }
                else if(!flag){
                    flag=true;
                    passwd2.setVisibility(View.VISIBLE);
                    passwd3.setVisibility(View.VISIBLE);
                    loginBtn.setVisibility(View.GONE);
                }
                else{
                    Log.d("1111", String.valueOf(passwd.getText()));
                    if(String.valueOf(passwd.getText()).equals(String.valueOf(passwd2.getText()))){
                        final String CREATE_USERWORD = "create table "+user+" ("
                            + "id integer primary key,"
                            + "state integer default 0)";
                        db.execSQL(CREATE_USERWORD);
                        ContentValues values = new ContentValues();
                        values.put("name",user);
                        values.put("password",pwd);
                        db.insert("users","null",values);
                        Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        passwd2.setVisibility(View.GONE);
                        passwd3.setVisibility(View.GONE);
                        loginBtn.setVisibility(View.VISIBLE);

                    }
                    else{
                        Toast.makeText(LoginActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}