package cn.edu.hznu.end;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class listAdapter extends RecyclerView.Adapter<listAdapter.ViewHolder>{
    private List<Word> mWordList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView wordText;
        TextView eVoiceText;
        TextView aVoiceText;
        TextView expText;
        public  ViewHolder(View view){
            super(view);
            wordText = view.findViewById(R.id.word);
            eVoiceText = view.findViewById(R.id.eVoice);
            aVoiceText = view.findViewById(R.id.aVoice);
            expText = view.findViewById(R.id.explanation);
        }
    }
    public listAdapter(List<Word> wordList){
        mWordList = wordList;
    }
    public void setData(List<Word> wordList){
        mWordList = wordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordview,parent,false);
        return new ViewHolder(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Word word = mWordList.get(position);
        holder.wordText.setText(String.valueOf(word.getWord()));
        holder.eVoiceText.setText(word.getaVoice());
        holder.aVoiceText.setText(word.geteVoice());
        holder.expText.setText(word.getExplanation());


    }
    @Override
    public int getItemCount() {
        return mWordList.size();
    }
}
