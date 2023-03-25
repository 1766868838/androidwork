package com.example.tongxunlu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity{
    private Button iv_add;
    private Button iv_del;
    private ListView lv_show;
    private EditText et_name,et_phone;
    private MyAdapter myAdapter;
    private DBHelper dbHelper;
    private List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_add = findViewById(R.id.iv_add);
        iv_del = findViewById(R.id.iv_del);
        lv_show = findViewById(R.id.lv_show);
        if (userList != null) {
            userList.clear();
        }
        dbHelper = new DBHelper(MainActivity.this, "user.db", null, 1);
        updateListView();
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
        iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
                updateListView();
              //  deleteData(1);
              //  deleteData(2);

//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                ListView recyclerView = findViewById(R.id.lv_show);
//                for (int i = 0 ; i < recyclerView.getChildCount()-1; i++) {
//
//                    Log.d("MainActivity", String.valueOf(recyclerView.getChildAt(i)));
//                    LinearLayout layout = (LinearLayout) recyclerView.getChildAt(i);
//                    CheckBox cheBox = layout.findViewById(R.id.checkb);
//                    //TextView textView = layout.findViewById(R.id.nam);
//                    if(cheBox.isChecked()){
//                        deleteData(i);
//                    }
//                }
            }
        });
        lv_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                updateData(position);
               // deleteData(position);
            }
        });
        lv_show.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //deleteData(position);
                return true;
            }
        });
    }
    private void addData() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        View dialogView=View.inflate(MainActivity.this,R.layout.layout_dialog,null);
        builder.setIcon(R.drawable.icon)
                .setTitle("添加联系人")
                .setView(dialogView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        et_name=dialogView.findViewById(R.id.et_name);
                        et_phone=dialogView.findViewById(R.id.et_pwd);
                        String name=et_name.getText().toString();
                        String phone=et_phone.getText().toString();
                        if (phone.length()!=11){
                            showToast("电话号码长度不符合要求");
                        }else {
                            User user=dbHelper.get(name);
                            if (user.getName()==null){
                                if (dbHelper.insert(name,phone)){
                                    showToast("添加成功");
                                    updateListView();
                                }else{
                                    showToast("添加失败");
                                }
                            }else {
                                showToast("该联系人已存在");
                            }
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    private void deleteData() {
                ListView recyclerView = findViewById(R.id.lv_show);
                for (int i = 0 ; i < recyclerView.getChildCount(); i++) {

                    Log.d("MainActivity", String.valueOf(recyclerView.getChildCount()));
                    LinearLayout layout = (LinearLayout) recyclerView.getChildAt(i);
                    CheckBox cheBox = layout.findViewById(R.id.checkb);
                    Log.d("MainActivity", String.valueOf(cheBox));
                    //TextView textView = layout.findViewById(R.id.nam);
                    if(cheBox.isChecked()){
                        User user = (User) myAdapter.getItem(i);
                        Log.d("MainActivity", String.valueOf(myAdapter.getItem(i)));
                        String deleteId = user.getId();
                        dbHelper.delete(deleteId);
                    }

                }

    }

                //.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    //@Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
               // });


    private void updateData(int position) {
        View dialogView = View.inflate(MainActivity.this, R.layout.layout_dialog, null);
        User user = (User) myAdapter.getItem(position);
        et_name = dialogView.findViewById(R.id.et_name);
        et_phone = dialogView.findViewById(R.id.et_pwd);
        et_name.setText(user.getName());
        et_phone.setText(user.getPhone());
        String findId = user.getId();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.drawable.icon)
                .setTitle("修改联系人")
                .setView(dialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = et_name.getText().toString();
                        String phone = et_phone.getText().toString();
                        if (phone.length()!=11){
                            showToast("电话号码长度不符合要求");
                        }else {
                            User user=dbHelper.get(name);
                            if (user.getName()==null){
                                if (dbHelper.update(findId,name,phone)){
                                    showToast("修改成功");
                                    updateListView();
                                }else{
                                    showToast("修改失败");
                                }
                            }else {
                                showToast("该联系人已存在");
                            }
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void updateListView(){
        userList=dbHelper.query();
        myAdapter=new MyAdapter(userList,MainActivity.this);
        lv_show.setAdapter(myAdapter);
    }
    public void showToast(String msg){
        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
}
