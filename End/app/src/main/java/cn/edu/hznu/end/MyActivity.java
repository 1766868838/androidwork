package cn.edu.hznu.end;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        ImageView reviewText = findViewById(R.id.reviewText);
        ImageView studyText = findViewById(R.id.studyText);
        ImageView readText = findViewById(R.id.readText);
        ImageView myText = findViewById(R.id.myText);
        Button button = findViewById(R.id.exitBtn);
        TextView userImg = findViewById(R.id.userimg);
        TextView userText = findViewById(R.id.username);
        reviewText.setOnClickListener(new clickListener());
        studyText.setOnClickListener(new clickListener());
        readText.setOnClickListener(new clickListener());
        myText.setOnClickListener(new clickListener());
        button.setOnClickListener(new clickListener());
        userText.setText(Data.getUsername());
        userImg.setText(userText.getText().subSequence(0,1));
    }

    class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if(v.getId()==R.id.studyText){
                Intent intent = new Intent(MyActivity.this,MemoryActivity.class);
                startActivity(intent);
            }
            else if(v.getId()==R.id.readText){
                Intent intent = new Intent(MyActivity.this,ReadActivity.class);
                startActivity(intent);
            }
            else if(v.getId()==R.id.reviewText){
                Intent intent = new Intent(MyActivity.this,ReviewActivity.class);
                startActivity(intent);
            }
            else if(v.getId()==R.id.exitBtn){
                exitApp();
            }
        }
    }

    public void exitApp() {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            System.exit(0);
        } else {// android2.1
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            am.restartPackage(getPackageName());
        }
    }
}