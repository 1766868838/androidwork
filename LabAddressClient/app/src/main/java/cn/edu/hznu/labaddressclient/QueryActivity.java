package cn.edu.hznu.labaddressclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class QueryActivity extends AppCompatActivity {

    private int checkedId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        Button query_Btn= (Button) findViewById(R.id.query);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        EditText editText = (EditText) findViewById(R.id.keyText);
        checkedId = radioGroup.getChildAt(0).getId();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) findViewById(i);
                checkedId=i;
                Toast.makeText(QueryActivity.this,
                        "你选择的是:" + radioButton.getText(),Toast.LENGTH_SHORT).show();
            }
        });
        query_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("content://cn.edu.hznu.addressbook.provider/addressList");
                Cursor cursor = null;
                Bundle bundle = new Bundle();
                String key = String.valueOf(editText.getText());
                if(key.equals("")){
                    Toast.makeText(QueryActivity.this,"搜索内容不能为空！",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    if(checkedId==R.id.radioName){
                        cursor = query(uri,"name",key);
                    }
                    else if(checkedId==R.id.radioMobile){
                        cursor = query(uri,"mobile",key);
                    }
                    if(cursor.getCount()!=0){
                        int count = cursor.getCount();
                        String[] nameList =new String[count];
                        String[] mobileList =new String[count];
                        count=0;
                        while(cursor.moveToNext()){
                            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                            String mobile = cursor.getString(cursor.getColumnIndexOrThrow("mobile"));
                            nameList[count]=name;
                            mobileList[count]=mobile;
                            count++;
                            Log.d("QueryActivity", "name:" + name);
                            Log.d("QueryActivity", "mobile:" + mobile);
                        }
                        cursor.close();

                        bundle.putStringArray("name",nameList);
                        bundle.putStringArray("mobile",mobileList);
                        bundle.putInt("count",count);
                        Intent intent = new Intent(QueryActivity.this,ResultActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(QueryActivity.this,"未查询到结果",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public Cursor query(Uri uri, String type, String key){
        Cursor cursor = getContentResolver().query(uri,
                null,
                type + " like '%" + key + "%' ",
                null,
                null);
        return cursor;
    }
}