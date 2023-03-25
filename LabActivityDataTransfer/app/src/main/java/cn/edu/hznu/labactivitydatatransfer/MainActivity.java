package cn.edu.hznu.labactivitydatatransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_reg = (Button)findViewById(R.id.register);
        btn_reg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText name = (EditText) findViewById(R.id.name);
                EditText passwd = (EditText) findViewById(R.id.passwd);
                RadioButton male = (RadioButton) findViewById(R.id.male);
                String gender = male.isChecked() ? "男" : "女";

                Bundle bundle = new Bundle();
                bundle.putString("name",name.getText().toString());
                bundle.putString("passwd",passwd.getText().toString());
                bundle.putString("gender",gender);
                Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });
    }
}