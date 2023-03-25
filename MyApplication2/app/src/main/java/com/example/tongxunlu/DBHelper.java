package com.example.tongxunlu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    ContentValues contentValues = new ContentValues();
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user(id integer primary key autoincrement,name text,phone text)");
    }
    //添加数据
    public boolean insert(String name,String phone) {
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        long result = db.insert("user", null, contentValues);
        return result > 0 ? true : false;
    }
    public boolean delete(String id){
        int result=db.delete("user","id=?",new String[]{id});
        return result>0?true:false;
    }
    //修改数据，根据id进行修改
    public boolean update(String id,String name,String phone){
        contentValues.put("id",id);
        contentValues.put("name",name);
        contentValues.put("phone",phone);
        int result=db.update("user",contentValues,"id=?",new String[]{id});
        return result>0?true:false;
    }
    //查询数据,查询表中的所有内容，将查询的内容用note的对象属性进行存储，并将该对象存入集合中。
    public List<User> query(){
        List<User> list=new ArrayList<>();
        Cursor result=db.query("user",null,null,null,
                null,null,null,null);
        if (result!=null){
            while (result.moveToNext()) {
                User user = new User();
                user.setId(String.valueOf(result.getInt(0)));
                user.setName(result.getString(1));   //1即第二列，指内容
                user.setPhone(result.getString(2));  //2即第三列，指时间
                list.add(user);
            }result.close();
        }return list;
    }

    public User get(String name) {
        User user=new User();
        Cursor result=db.query("user",null,"name=?",new String[]{name},
                null,null,null,null);
        if (result.getCount()==1){
            result.moveToFirst();
            user.setName(result.getString(1));
            user.setPhone(result.getString(2));
            return user;
        }else {
            user.setName(null);
            user.setPhone(null);
            return user;
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
