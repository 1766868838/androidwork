package cn.edu.hznu.end;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ExpandTabTextView extends LinearLayout implements View.OnClickListener {
    //声明上下文对象
    private Context context;
    //声明文本视图对象
    private TextView text_view;
    //声明按钮对象
    private Button btn;
    //正常显示的行数
    private final int line_number = 3;
    //是否被选中
    private boolean isSelect = false;

    public ExpandTabTextView(Context context) {
        super(context);
    }

    public ExpandTabTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //从布局文件中获取展示内容
        LayoutInflater.from(context).inflate(R.layout.text_expand, this, true);
    }

    //在布局展示完毕后调用，因为getLineHeight方法(获取TextView的行高)要等渲染完成后才能得知具体高度
    //行高是指一行文字上方距离下一行文字上方的距离。
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //从布局文件中获取名叫ll_content的线性布局
        LinearLayout ll_content = findViewById(R.id.ll_content);
        //设置布局的点击事件
        btn=findViewById(R.id.btn);
        btn.setText("查看更多");
        btn.setOnClickListener(this);
        //从布局文件中获取text_view的文本视图
        text_view = findViewById(R.id.text_view);
        //设置文本的高度为n行文字这么高
        /*TextView的首行和最后一行有一个额外的padding间距，
          这导致实际行高要大于getLineHeight()方法得到的行高
          所以在设置文本高度时：（普通行高+6）*行数
        */
        text_view.setHeight((text_view.getLineHeight() + 6) * line_number);
    }

    //设置文本内容
    public void setText(String content) {
        text_view.setText(content);
    }

    //设置文本的资源编号
    public void setText(int id) {
        setText(context.getResources().getString(id));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn) {
            isSelect = !isSelect;
            //清除文本视图的动画
            text_view.clearAnimation();
            final int deltaValue;
            //获得文本视图的实际高度
            final int startValue = text_view.getHeight();
            if (isSelect) {//如果选中则展开后面的所有文字  getLineCount():获取TextView文字行数
                //结果为新增的文本视图的高度
                deltaValue = (text_view.getLineHeight() + 6) * text_view.getLineCount() - startValue;
                btn.setText("隐藏更多");
            } else {
                //结果为0
                deltaValue = (text_view.getLineHeight() + 6) * line_number - startValue;
                btn.setText("查看更多");
            }
            //创建一个文字展开 收缩动画
            Animation animation = new Animation() {
                //该方法就是动画的具体实现
                /*
                    第一个参数：interpolatedTime代表动画的时间进行比。
                    不管动画实际的持续时间如何，当动画播放时，该参数总是自动从0变化到1
                    第二个参数：Transformation t代表了补间动画在不同时刻对图形或组件的变形程度。
                 */
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if (isSelect) {//如果选中则展开后面的所有文字  getLineCount():获取TextView文字行数
                        //结果为新增的文本视图的高度
                        text_view.setHeight(1200);
                    } else {
                        //结果为0
                        text_view.setHeight(200);
                    }
                    //随着时间的流逝重新设置文本的行高
                 //text_view.setHeight((int) (startValue + deltaValue * interpolatedTime));
                    //text_view.setHeight(1200);
                }
            };
            //设置动画的持续时间为500毫秒
            animation.setDuration(500);
            //开始文本视图的动画展示
            text_view.startAnimation(animation);
        }
    }
}

