package dongzhong.testforfloatingwindow;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
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


import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dongzhong on 2018/5/30.
 */

public class FloatingButtonService extends Service {
    public static boolean isStarted;//isStarted=false代表按钮未点击，弹幕没出现

    private WindowManager windowManager;
    //WindowManager.LayoutParams 是 WindowManager 接口的嵌套类
    private WindowManager.LayoutParams layoutParams;
    private final int Time = 2500;    //时间间隔，   单位 ms
    private int N = 0;      //用来观测重复执行

    private Handler handler = new Handler();

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
        layoutParams.width = 2000;//视口宽度
        layoutParams.height = 1500;//视口高度
        layoutParams.x = -400;//那么它表示窗口的绝对X位置是300
        layoutParams.y = 800;//那么它表示窗口的绝对Y位置是800
        //layoutParams.windowAnimations = R.style.anim_view;
    }


    //该方法是Service都必须实现的方法，该方法会返回一个 IBinder对象，app通过该对象与Service组件进行通信！
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //首次启动会创建一个Service实例,依次调用onCreate()和onStartCommand()方法,此时Service 进入运行状态,
    // 如果再次调用StartService启动Service,将不会再创建新的Service对象, 系统会直接复用前面创建的Service对象,调用它的onStartCommand()方法！
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        showFloatingWindow();


        return super.onStartCommand(intent, flags, startId);
    }

    private void showFloatingWindow() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (Settings.canDrawOverlays(this)) {
                //新建悬浮窗控件
                //上下文就是应用程序对象当前状态的环境。它允许新创建的对象了解正在发生的事情
                button = new Button(getApplicationContext());//getApplicationContext() 是返回应用的上下文
                button.setText("Floating Window");//设置文本内容
                button.setBackgroundColor(Color.TRANSPARENT);//设置按钮背景颜色,Color.TRANSPARENT代表设置透明
                // windowManager.addView(button, layoutParams);
                Animation animation = new TranslateAnimation(1200,-350,50,50);
                animation.setRepeatCount(200);
                animation.setDuration(2500);
                final LinearLayout linearLayout =new LinearLayout(this);
                linearLayout.addView(button);
                button.startAnimation(animation);
                windowManager.addView(linearLayout, layoutParams);


                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(this, Time);
                        //每隔一段时间要重复执行的代码
                        N = N + 1;
                        button.setText("第" + N + "次执行");
                        windowManager.updateViewLayout(linearLayout, layoutParams);
                        System.out.println("第" + N + "次执行");

                        if(!isStarted){
                            handler.removeCallbacks(this);

                        }

                    }
                };
                handler.postDelayed(runnable, 0);	//启动计时器

                Log.d("dddd", String.valueOf(isStarted));
                // 将悬浮窗控件添加到WindowManager
                //button.setOnTouchListener(new FloatingOnTouchListener());
            }
        }
    }

    public static void stopDanmu(){

        Log.d("zdvcf", "stopDanmu: sxsx");
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