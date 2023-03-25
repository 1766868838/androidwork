package cn.edu.hznu.labactivitylifecycle;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class NormalActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.normal_layout);
    }
}
