package cn.edu.hznu.newsprj;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by hzwind on 2017/8/1.
 */
//双页活动
public class NewsContentFragment extends Fragment {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.news_content_frag,container,false);
        return view;
    }

    public void refresh(String title, String content){
        View visibilityLayout=view.findViewById(R.id.visibility_layout);
        visibilityLayout.setVisibility(View.VISIBLE);
        TextView txtTitle=(TextView)view.findViewById(R.id.news_title);
        TextView txtContent=(TextView)view.findViewById(R.id.news_content);
        txtTitle.setText(title);
        txtContent.setText(content);

    }
}
