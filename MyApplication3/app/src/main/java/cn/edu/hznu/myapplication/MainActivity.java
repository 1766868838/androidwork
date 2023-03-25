package cn.edu.hznu.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.InputStream;


public class MainActivity extends Activity {

    /**
     * 按行读取txt
     *
     * @param is
     * @return
     * @throws Exception
     */
    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db = new DatabaseHelper(this, "my.db")
                .getWritableDatabase();

        try {

            *InputStream in = getAssets().open("garden.sql");

            String sqlUpdate = null;
            try {
                sqlUpdate = readTextFromSDcard(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String[] s = sqlUpdate.split(";");
            for (int i = 0; i < s.length; i++) {
                if (!TextUtils.isEmpty(s[i])) {
                    db.execSQL(s[i]);
                }
            }
            in.close();
        } catch (SQLException e) {
        } catch (IOException e) {
        }

    }

}
