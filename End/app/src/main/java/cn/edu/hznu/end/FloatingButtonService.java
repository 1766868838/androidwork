package cn.edu.hznu.end;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class FloatingButtonService extends Service {

    public static boolean isStarted;//isStarted=false代表按钮未点击，弹幕没出现
    private Bundle bundle;
    MyDatabaseHelper myDatabaseHelper;
    SQLiteDatabase db;

    private LinearLayout linearLayout;

    private WindowManager windowManager;

    //WindowManager.LayoutParams 是 WindowManager 接口的嵌套类

    private WindowManager.LayoutParams layoutParams;

    private final int Time = 8000;    //时间间隔，   单位 ms

    private int N = 0;      //用来观测重复执行
    final List<Word> wordList = new ArrayList<>();
    //    public String[] wordList={"abandon","intelligible","rehabilitate","scandal","futile"};
    public String[] wordList1;
    public String[] meaningList={"放弃","可理解的哈哈哈","改造","丑闻","徒劳"};



    private Button button;


    @Override
    public void onCreate() {
        super.onCreate();
        //获取windowmanager服务

        // getSystemService是Android很重要的一个API，它是Activity的一个方法，根据传入的NAME来取得对应的Object，而后转换成相应的服务对象。如下介绍系统相应的服务

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        layoutParams = new WindowManager.LayoutParams();

        //Build.VERSION.SDK_INT是一个静态变量，代表运行该应用的手机系统的SDK版本

        //Build.VERSION_CODES.M是android sdk中的一个常量，代表的就是不同的SDK版本号,按下ctrl单击就知道了版本号是多少

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //在Android 8.0之后，如果需要实现在其他应用和窗口上方显示提醒窗口，那么必须该为TYPE_APPLICATION_OVERLAY的类型。

            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        } else {
            //在Android 8.0之前，悬浮窗口设置可以为TYPE_PHONE，这种类型是用于提供用户交互操作的非应用窗口。
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        layoutParams.format = PixelFormat.RGBA_8888;//设置窗口背景色透明

        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;//当设置了 Gravity.LEFT 或 Gravity.TOP之后，x,y值就表示到特定边的距离。

        //FLAG_NOT_TOUCH_MODAL可覆盖输入法窗口

        //如果设置了FLAG_NOT_FOCUSABLE，那么屏幕上弹窗之外的地方能够点击

        layoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        layoutParams.width = 650;//视口宽度

        layoutParams.height = 250;//视口高度

        layoutParams.x = 1550;//那么它表示窗口的绝对X位置是300

        layoutParams.y = 350;//那么它表示窗口的绝对Y位置是800

        //layoutParams.windowAnimations = R.style.anim_view;

    }





    //该方法是Service都必须实现的方法，该方法会返回一个 IBinder对象，app通过该对象与Service组件进行通信！

    @Nullable

    @Override

    public IBinder onBind(Intent intent) {

        return null;

    }

    private void dataUpdate() {
        //2学会了，1学过没学会，0没学过
        wordList.clear();
        //DatabaseUtil d = new DatabaseUtil();
        String username = Data.getUsername();
        Log.d("MainActivitu", String.valueOf(username));
        myDatabaseHelper = new MyDatabaseHelper(this, "words.db", null, 2);
        db = myDatabaseHelper.getWritableDatabase();
        String sql = "select * from "+username+" natural join words where state = 1";
        Cursor cursor = db.rawQuery(sql,null);
        //Cursor cursor = db.execSQL(sql);


        while(cursor.moveToNext()){

            String word = cursor.getString(2);
            String explanation = cursor.getString(5);

            String[] str = explanation.split(",");
            String[] str1 = str[0].split("\\(");
            String[] str2 = str1[0].split(" ");

            wordList.add(new Word(word,str2[0]));
        }
        cursor.close();
    }



    //首次启动会创建一个Service实例,依次调用onCreate()和onStartCommand()方法,此时Service 进入运行状态,

    // 如果再次调用StartService启动Service,将不会再创建新的Service对象, 系统会直接复用前面创建的Service对象,调用它的onStartCommand()方法！

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dataUpdate();
        Log.d("aaaaaaaaaaa", String.valueOf(wordList.size()));
        //for(int i =0;i<wordList.size();i++){
        // wordList1[i]=wordList.get(i).getWord();
        //}
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("HandlerLeak")
    final
    Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if(layoutParams.x<=-340){
                        handler1.removeCallbacks(runnable1);
                        layoutParams.x=1550;
                        windowManager.updateViewLayout(linearLayout,layoutParams);
                    }else if(!isStarted){
                        handler.removeCallbacks(runnable);
                        handler1.removeCallbacks(runnable1);
                        layoutParams.x=1550;
                        windowManager.removeView(linearLayout);
                    }else {
                        layoutParams.x=layoutParams.x-5;
                        windowManager.updateViewLayout(linearLayout,layoutParams);
                    }

                    //System.out.println("第" + N + "次执行");

                    break;
                default:
                    break;
            }
        }

        ;
    };

    //然后创建一个Runnable对象
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            handler1.sendEmptyMessage(0);
            handler1.postDelayed(this,10);
        }
    };

    @SuppressLint("HandlerLeak")
    final
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (wordList.size() != 0) {
                        if (!isStarted) {
                            handler.removeCallbacks(runnable);
                            handler1.removeCallbacks(runnable1);
                            layoutParams.x = 1500;
                            windowManager.updateViewLayout(linearLayout, layoutParams);
                        }
                        if (N >= wordList.size()) {
                            N = 0;
                        }
                        layoutParams.x = 1550;
                        windowManager.updateViewLayout(linearLayout, layoutParams);
                        //System.out.println("第" + N + "次执行");
                        button.setText(wordList.get(N).getExplanation() + "\n" + wordList.get(N).getWord());
                        button.setAllCaps(false);//这行代码不能设置在setText之前
                        button.setGravity(Gravity.CENTER);//button默认文本就是居中的，但是他并不是特别居中，设置一行强调一下就完全居中了。可能是安卓原生代码的小bug
                        handler1.postDelayed(runnable1, 50);
                        N = N + 1;
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    //然后创建一个Runnable对象
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
            handler.postDelayed(this,Time);
        }
    };


    public void startDanmu(){

        handler.post(runnable);
    }

    //点击按钮开始的时候会调用，关闭的时候不会调用
    //因为开始的时候会开始一个线程，所以关闭的时候也能在线程中判断逻辑
    @SuppressLint("SetTextI18n")
    private void showFloatingWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                //新建悬浮窗控件
                //上下文就是应用程序对象当前状态的环境。它允许新创建的对象了解正在发生的事情
                button = new Button(getApplicationContext());//getApplicationConte xt() 是返回应用的上下文
                button.setText("Floating Window");//设置文本内容
                button.setTextColor(android.graphics.Color.RED);
                button.setBackgroundColor(Color.TRANSPARENT);//设置按钮背景颜色,Color.TRANSPARENT代表设置透明
                button.setTextSize(20);
                // windowManager.addView(button, layoutParams);
                //用一个LinerLayout包裹button
                linearLayout =new LinearLayout(this);
                linearLayout.addView(button);
                //button.startAnimation(animation);

                //将linerlayout包裹到window
                windowManager.addView(linearLayout, layoutParams);
                startDanmu();
                // 将悬浮窗控件添加到WindowManager
                //button.setOnTouchListener(new FloatingOnTouchListener());
            }

        }

    }





    private class FloatingOnTouchListener implements View.OnTouchListener {

        private int x;

        private int y;



        @Override

        public boolean onTouch(View view, MotionEvent event) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    x = (int) event.getRawX();

                    y = (int) event.getRawY();

                    break;

                case MotionEvent.ACTION_MOVE:

                    int nowX = (int) event.getRawX();

                    int nowY = (int) event.getRawY();

                    int movedX = nowX - x;

                    int movedY = nowY - y;

                    x = nowX;

                    y = nowY;

                    layoutParams.x = layoutParams.x + movedX;

                    layoutParams.y = layoutParams.y + movedY;

                    windowManager.updateViewLayout(view, layoutParams);

                    break;

                default:

                    break;

            }

            return false;

        }

    }

}