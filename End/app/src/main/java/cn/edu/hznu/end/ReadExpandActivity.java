package cn.edu.hznu.end;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ReadExpandActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_expand);
        TextView textView = findViewById(R.id.read_text);
        ImageView read_back = findViewById(R.id.read_back);
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");

        if(id == R.id.book1){
            textView.setText(R.string.news1);
        }
        else if(id == R.id.book2){
            textView.setText(R.string.news2);
        }
        else if(id == R.id.book3){
            textView.setText(R.string.news3);
        }
        else if(id == R.id.book4){
            textView.setText(R.string.news4);
        }
        read_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}