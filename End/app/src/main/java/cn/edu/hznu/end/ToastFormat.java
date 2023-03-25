package cn.edu.hznu.end;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastFormat {//用于显示自定义的toast
    private Context context;
    private TextView tipsText;
    private Toast toast = null;
    private static final int ContentID = R.id.btn;
    private static final int LayoutID = R.layout.toast;
    public ToastFormat(Context context){
        this.context = context;
    }
    public void InitToast(){
        if (toast == null) {
            toast = new Toast(context);
            View view = LayoutInflater.from(context).inflate(LayoutID, null, false);
            tipsText = view.findViewById(ContentID);
            toast.setView(view);
        }
    }
    public void setText(String tips){
        tipsText.setText(tips);
    }
    public void show(){
        toast.show();
    }
    public void setShowTime(int time){
        toast.setDuration(time);
    }
    public void setTextColor(int color){
        tipsText.setTextColor(context.getResources().getColor(color));
    }
    public void setTextSize(float size){
        tipsText.setTextSize(size);
    }
}
