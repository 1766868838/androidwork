package cn.edu.hznu.end;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read);
        LinearLayout linearLayout1 = findViewById(R.id.book1);
        LinearLayout linearLayout2 = findViewById(R.id.book2);
        LinearLayout linearLayout3 = findViewById(R.id.book3);
        LinearLayout linearLayout4 = findViewById(R.id.book4);

        linearLayout1.setOnClickListener(new clickListener());
        linearLayout2.setOnClickListener(new clickListener());
        linearLayout3.setOnClickListener(new clickListener());
        linearLayout4.setOnClickListener(new clickListener());

        ImageView reviewText = findViewById(R.id.reviewText);
        ImageView studyText = findViewById(R.id.studyText);
        ImageView readText = findViewById(R.id.readText);
        ImageView myText = findViewById(R.id.myText);
        reviewText.setOnClickListener(new clickListener());
        studyText.setOnClickListener(new clickListener());
        readText.setOnClickListener(new clickListener());
        myText.setOnClickListener(new clickListener());

    }
    class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.reviewText) {
                Intent intent = new Intent(ReadActivity.this, ReviewActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.studyText) {
                Intent intent = new Intent(ReadActivity.this, MemoryActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.myText) {
                Intent intent = new Intent(ReadActivity.this, MyActivity.class);
                startActivity(intent);
            }
            else if(v.getId()==R.id.book1||v.getId()==R.id.book2||v.getId()==R.id.book3||v.getId()==R.id.book4){
                Bundle bundle = new Bundle();
                bundle.putInt("id",v.getId());
                Intent intent = new Intent(ReadActivity.this, ReadExpandActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }
}