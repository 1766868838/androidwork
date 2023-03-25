package cn.edu.hznu.labbasicui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText num = (EditText) findViewById(R.id.number);
        Button c_btn = (Button) findViewById(R.id.c_btn);
        Button s_btn = (Button) findViewById(R.id.s_btn);
        ProgressBar pb = (ProgressBar) findViewById(R.id.pb) ;
        AlertDialog.Builder i_dialog = new AlertDialog.Builder (MainActivity.this);
        i_dialog.setTitle("Indication");
        i_dialog.setMessage("输入的数字不合法");
        i_dialog.setCancelable(false);
        i_dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("MainActivity","ok");
            }
        });

        AlertDialog.Builder a_dialog = new AlertDialog.Builder (MainActivity.this);
        a_dialog.setTitle("Progress Value");
        a_dialog.setCancelable(false);
        a_dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("MainActivity","ok");
            }
        });

        c_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(num.getText().toString());
                if(n < 100 && n > 0){
                    pb.setProgress(n);
                }
                else{
                    i_dialog.show();
                    num.setText("");
                }
            }
        });

        s_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a_dialog.setMessage("当前值:"+Integer.parseInt(num.getText().toString()));
                a_dialog.show();
            }
        });
    }
}