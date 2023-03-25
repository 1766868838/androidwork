package cn.edu.hznu.myapplication;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class GuideLayout extends FrameLayout {

    private WindowManager wManager;
    private WindowManager.LayoutParams wmParams;
    private View addView;

    private TextView mTextView;
    private ImageView mImageView;

    private boolean isAddView;
    private AnimatorSet mShowAnimatorSet, mHideAnimatorSet;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    showAnimator();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    /**
     * @param context
     */
    public GuideLayout(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param context
     * @param attrs
     */
    public GuideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public GuideLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addView = LayoutInflater.from(context).inflate(R.layout.guide_layout,
                this);
        mTextView = (TextView) addView.findViewById(R.id.tv);
        mImageView = (ImageView) addView.findViewById(R.id.iv);
        mTextView.setVisibility(View.GONE);
        mImageView.setVisibility(View.GONE);
        setAnimator();
        getWindowManager(context);
    }

    /**
     * @param context
     * @category 实例化WindowManager 初次模拟位置时候使用
     */
    private void getWindowManager(final Context context) {
        wManager = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        wmParams.format = PixelFormat.TRANSPARENT;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        wmParams.gravity = Gravity.CENTER;
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
    }

    private void setAnimator() {
        mShowAnimatorSet = new AnimatorSet();
        Animator[] showAnimator = new Animator[2];
        showAnimator[0] = ObjectAnimator.ofFloat(mTextView, "alpha",
                new float[]{0.0F, 1.0F});
        showAnimator[1] = ObjectAnimator.ofFloat(mImageView, "alpha",
                new float[]{0.0F, 1.0F});
        mShowAnimatorSet.playTogether(showAnimator);
        mShowAnimatorSet.setDuration(1500l);

        mHideAnimatorSet = new AnimatorSet();
        Animator[] hideAnimator = new Animator[2];
        hideAnimator[0] = ObjectAnimator.ofFloat(mTextView, "alpha",
                new float[]{1.0F, 0.0F});
        hideAnimator[1] = ObjectAnimator.ofFloat(mImageView, "alpha",
                new float[]{1.0F, 0.0F});
        mHideAnimatorSet.playTogether(hideAnimator);
        mHideAnimatorSet.setDuration(1500l);
    }

    public void showAnimator() {
        mTextView.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.VISIBLE);
        mShowAnimatorSet.start();
        isAddView = true;
    }

    public void hideAnimator() {
        mHideAnimatorSet.start();
        mHideAnimatorSet.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                mTextView.setVisibility(View.INVISIBLE);
                mImageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });

    }

    public void sendMessage() {
        if (isAddView) {
            wManager.removeView(this);
            mHandler.removeMessages(1);
            isAddView = false;
        }
        mHandler.sendEmptyMessage(1);
        wManager.addView(this, wmParams);
    }
}

