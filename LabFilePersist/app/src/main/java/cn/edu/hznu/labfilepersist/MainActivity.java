package cn.edu.hznu.labfilepersist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button saveBtn = (Button) findViewById(R.id.saveBtn);
        Button loadBtn = (Button) findViewById(R.id.loadBtn);
        EditText fName = (EditText) findViewById(R.id.fName);
        EditText cont = (EditText) findViewById(R.id.cont);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Warning");
        builder.setMessage("文件名不能为空!");
        builder.setCancelable(false);
        builder.setPositiveButton("OK",null);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(fName.getText());
                String content = String.valueOf(cont.getText());
                FileOutputStream out;
                BufferedWriter writer = null;

                if(name.equals("")){
                    builder.show();
                }
                else{
                    try {
                        out = openFileOutput(name, Context.MODE_PRIVATE);
                        writer = new BufferedWriter(new OutputStreamWriter(out));
                        writer.write(content);
                        cont.setText("");

                        Toast.makeText(MainActivity.this,"data have bean saved",Toast.LENGTH_SHORT).show();

                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        try {
                            if(writer!=null) {
                                writer.close();
                            }
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(fName.getText());
                FileInputStream in;
                BufferedReader reader = null;
                StringBuilder content = new StringBuilder();
                if(name.equals("")){
                    builder.show();
                }
                else{
                    try {
                        in = openFileInput(name);
                        reader = new BufferedReader(new InputStreamReader(in));
                        String line;
                        while((line = reader.readLine())!=null){
                            content.append(line);
                        }
                        cont.setText(content);
                        Toast.makeText(MainActivity.this,"data have bean loaded",Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        if(reader!=null){
                            try {
                                reader.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }


            }
        });
    }
}