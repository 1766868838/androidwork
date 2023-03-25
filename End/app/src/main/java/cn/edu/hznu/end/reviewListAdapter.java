package cn.edu.hznu.end;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class reviewListAdapter extends RecyclerView.Adapter<reviewListAdapter.reviewViewHolder>{
    private final List<Word> mWordList;
    static class reviewViewHolder extends RecyclerView.ViewHolder{

        TextView rWordText;
        TextView rExplanation;

        public  reviewViewHolder(View view){
            super(view);
            rWordText = view.findViewById(R.id.rWord);
            rExplanation =  view.findViewById(R.id.rExplanation);

        }
    }
    public reviewListAdapter(List<Word> wordList){
        mWordList = wordList;
    }
    @NonNull
    @Override
    public reviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviewview,parent,false);
        return new reviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull reviewViewHolder holder, int position) {
        Word word = mWordList.get(position);
        holder.rWordText.setText(String.valueOf(word.getWord()));
        holder.rExplanation.setText(word.getExplanation());

    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }
}
