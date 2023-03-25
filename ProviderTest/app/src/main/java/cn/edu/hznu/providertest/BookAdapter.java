package cn.edu.hznu.providertest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hzwind on 2018/5/9.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private int resourceId;
    public BookAdapter(Context context, int textViewResouceId, List<Book> objects){
        super(context, textViewResouceId, objects);
        resourceId=textViewResouceId;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Book book=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent, false);
        TextView txtNo=(TextView)view.findViewById(R.id.txtNo);
        TextView txtBookName=(TextView)view.findViewById(R.id.txtBookName);
        TextView txtAuthor=(TextView)view.findViewById(R.id.txtAuthor);
        TextView txtPages=(TextView)view.findViewById(R.id.txtPages);
        TextView txtPrice=(TextView)view.findViewById(R.id.txtPrice);
        txtNo.setText(""+(position+1));
        txtBookName.setText(book.getBookName());
        txtAuthor.setText(book.getAuthor());
        txtPages.setText(""+book.getPages());
        txtPrice.setText(""+book.getPrice());
        return view;
    }
}
