package cn.edu.hznu.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class BarrageView extends RelativeLayout {

    private Context mContext;

    private BarrageHandler mHandler = new BarrageHandler();

    private Random random = new Random(System.currentTimeMillis());

    // 两个弹幕的最小间隔时间
    private static final long BARRAGE_GAP_MIN_DURATION = 1000;

    // 两个弹幕的最大间隔时间
    private static final long BARRAGE_GAP_MAX_DURATION = 2000;

    // 速度,ms
    private int maxSpeed = 12000;

    // 速度,ms
    private int minSpeed = 8000;

    // 文字最大值
    private int maxSize = 50;

    // 文字最小值
    private int minSize = 10;

    private int totalHeight = 0;

    private int lineHeight = 0;// 每一行弹幕的高度

    private int totalLine = 0;// 弹幕的行数

    private String[] itemText = { "他们都说蔡睿智很帅,但他总觉得自己很丑",
            "他们都说蔡睿智是男神,但他只觉得自己是男生", "蔡睿智不是男神,蔡睿智是男生", "蔡睿智貌似是gay", "蔡睿智是弯的",
            "蔡睿智是弯的,还好现在掰回来了", "他承受了他这个年纪不该有的机智与帅气，他好累",
            "我恨自己的颜值，我觉得自己的才华才是吸引别人的地方", "他为什么对妹子不感兴趣呢？为什么？", "他为什么不想谈恋爱","他不会去爱别人,同时也不希望别人去爱他,他已经习惯一个人了",
            "他的心里是否住着一个苍老的小孩", "他的世界一直就是他和他的影子,直到遇到她", "她引导他走出了自己的世界，改变他的很多看法",
            "他渐渐的发现自己已经离不开他,他选择不再去压抑自己", "因为他已经不是那个无能为力的年纪","她经常说他 高冷,现在越来越觉得他恨闷骚","开始他一直与她保持朋友距离,但他发现自己根本作不到"};
    private int textCount;
    private MainActivity barrageController;

    public BarrageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        _init();
    }



    public BarrageView(Context context, AttributeSet attrs) {
        this(context, null, 0);

    }

    public BarrageView(Context context) {
        this(context, null);

    }

    private void _init() {
        textCount = itemText.length;
        int duration = (int) ((BARRAGE_GAP_MAX_DURATION - BARRAGE_GAP_MIN_DURATION) * Math
                .random());
        mHandler.sendEmptyMessageDelayed(0, duration);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        totalHeight = getMeasuredHeight();
        lineHeight = getLineHeight();
        totalLine = totalHeight / lineHeight;
    }

    private void generateItem() {
        BarrageItem item = new BarrageItem();
        String tx = itemText[(int) (Math.random() * textCount)];
        int sz = (int) (minSize + (maxSize - minSize) * Math.random());
        item.textView = new TextView(mContext);
        item.textView.setText(tx);
        item.textView.setTextSize(sz);
        item.textView.setTextColor(Color.rgb(random.nextInt(256),
                random.nextInt(256), random.nextInt(256)));
        item.textMeasuredWidth = (int) getTextWidth(item, tx, sz);
        item.moveSpeed = (int) (minSpeed + (maxSpeed - minSpeed)
                * Math.random());
        if (totalLine == 0) {
            totalHeight = getMeasuredHeight();
            lineHeight = getLineHeight();
            totalLine = totalHeight / lineHeight;
        }
        item.verticalPos = random.nextInt(Math.abs(totalLine)+1) * lineHeight+411;
        showBarrageItem(item);
    }

    private void showBarrageItem(final BarrageItem item) {
        int leftMargin = this.getRight() - this.getLeft()
                - this.getPaddingLeft();
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.topMargin = item.verticalPos;
        this.addView(item.textView, params);
        Animation anim = generateTranslateAnim(item, leftMargin);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                item.textView.clearAnimation();
                BarrageView.this.removeView(item.textView);
            }
        });
        item.textView.startAnimation(anim);
    }

    private TranslateAnimation generateTranslateAnim(BarrageItem item,
                                                     int leftMargin) {
        TranslateAnimation anim = new TranslateAnimation(leftMargin,
                -item.textMeasuredWidth, 0, 0);
        anim.setDuration(item.moveSpeed);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setFillAfter(true);
        return anim;
    }

    /**
     * 计算TextView中字符串的长度
     *
     * @param item
     * @param text
     *            要计算的字符串
     * @param Size
     *            字体大小
     * @return TextView中字符串的长度
     */
    public float getTextWidth(BarrageItem item, String text, float Size) {
        Rect bounds = new Rect();
        TextPaint paint;
        paint = item.textView.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    /**
     * 获得每一行弹幕的最大高度
     */
    private int getLineHeight() {
        BarrageItem item = new BarrageItem();
        String tx = itemText[0];
        item.textView = new TextView(mContext);
        item.textView.setText(tx);
        item.textView.setTextSize(maxSize);

        Rect bounds = new Rect();
        TextPaint paint;
        paint = item.textView.getPaint();
        paint.getTextBounds(tx, 0, tx.length(), bounds);
        return bounds.height();
    }

    public void setBarrageController(MainActivity barrageController) {
        this.barrageController = barrageController;
    }

    class BarrageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            generateItem();
            // 每个弹幕产生的时间随机
            int duration = (int) ((BARRAGE_GAP_MAX_DURATION - BARRAGE_GAP_MIN_DURATION) * Math
                    .random());
            this.sendEmptyMessageDelayed(0, duration);
        }
    }

}