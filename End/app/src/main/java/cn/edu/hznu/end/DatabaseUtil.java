package cn.edu.hznu.end;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;


public class DatabaseUtil extends Application {//复制res/raw下的数据库到手机里
    @SuppressLint("SdCardPath")
    public static void packDataBase(Context context){

        String DB_PATH = "/data/data/cn.edu.hznu.end/databases/";
        String DB_NAME = "words.db";
        // 检查 SQLite 数据库文件是否存在
        if (!(new File(DB_PATH + DB_NAME)).exists()) {
            // 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
            File f = new File(DB_PATH);
            // 如 database 目录不存在，新建该目录
            if (!f.exists()) {
                f.mkdir();
            }

            try {
                // 得到输入流，输出流
                InputStream is = context.getResources().openRawResource(R.raw.words);
                OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);

                // 文件写入
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
