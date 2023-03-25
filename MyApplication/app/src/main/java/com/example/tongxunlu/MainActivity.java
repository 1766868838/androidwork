package com.example.tongxunlu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import cn.edu.hznu.myapplication.R;


public class MainActivity extends AppCompatActivity{
    private Button iv_add;
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
        lv_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                updateData(position);
                deleteData(position);
            }
        });
        lv_show.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteData(position);
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
    private void deleteData(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.drawable.icon)
                .setTitle("提示")
                .setMessage("是否删除该联系人？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User user = (User) myAdapter.getItem(position);
                        String deleteId = user.getId();
                        if (dbHelper.delete(deleteId)) {
                            updateListView();
                            showToast("删除成功");
                        } else {
                            showToast("删除失败");
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
