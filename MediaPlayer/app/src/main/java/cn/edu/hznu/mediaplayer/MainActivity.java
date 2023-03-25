package cn.edu.hznu.mediaplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private final MediaPlayer mediaPlayer = new MediaPlayer();
    ImageView albumImg ;
    TextView bTitleText ;
    TextView bDurationText ;
    private listAdapter adapter;
    private RecyclerView recyclerView;
    private SeekBar seekBar;
    private LinearLayoutManager linearLayoutManager;
    private final List<Music> musicList = new ArrayList<>();
    private Animation circle_anim;
    private boolean isSeekBarChanging = false;
    int currentPausePosition = 0;
    int currentPosition = -1;
    int tempPosition = -1;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        albumImg =findViewById(R.id.albumImg);
        bTitleText = findViewById(R.id.titleText);
        bDurationText = findViewById(R.id.durationText);

        circle_anim = AnimationUtils.loadAnimation(this, R.anim.anim_round_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);


        //绑定事件
        seekBar = findViewById(R.id.seekBar);
        ImageView stop = findViewById(R.id.stop);
        ImageView pause = findViewById(R.id.pause);
        ImageView last = findViewById(R.id.last);
        ImageView play = findViewById(R.id.play);
        ImageView next = findViewById(R.id.next);
        stop.setOnClickListener(new OnclickListener());
        pause.setOnClickListener(new OnclickListener());
        last.setOnClickListener(new OnclickListener());
        play.setOnClickListener(new OnclickListener());
        next.setOnClickListener(new OnclickListener());


        //进度条相关事件
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//按下停止播放
                mediaPlayer.pause();
                isSeekBarChanging = true;
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//松开时继续播放
                isSeekBarChanging = false;
                mediaPlayer.seekTo(seekBar.getProgress());
                mediaPlayer.start();
            }
        });

        //播放完毕自动切换下一首歌
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextMusic();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });


    //动态获取权限
        if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE ,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else {
            initMediaPlayer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMediaPlayer();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }


    public void updateDatabase(){

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null, null, null,
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        int id =0;
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            id++;
            String album =cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
            long albumId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            Music music = new Music(id,title,artist,duration,size,album,url,albumId);
            //下面这行代码获取的id是数据库内部的id，我不清楚排列规则，就在之后自己定义了一个从1开始的id
            //int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            musicList.add(music);
            //Log.d("Mainactivity", album);
        }
        cursor.close();
    }
    public void initMediaPlayer(){
        updateDatabase();
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new listAdapter(musicList);
        recyclerView.setAdapter(adapter);
    }
    public class listAdapter extends RecyclerView.Adapter<listAdapter.ViewHolder>{
        private final List<Music> mMusicList;
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView idText;
            TextView titleText;
            TextView artistText;
            TextView durationText;
            View updownLine;
            public  ViewHolder(View view){
                super(view);
                idText = view.findViewById(R.id.id);
                titleText =  view.findViewById(R.id.title);
                artistText = view.findViewById(R.id.artist);
                durationText = view.findViewById(R.id.duration);
                updownLine = itemView.findViewById(R.id.updownLine);
            }
        }
        public listAdapter(List<Music> musicList){
            mMusicList = musicList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.musicview,parent,false);
            return new ViewHolder(view);
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.itemView.setSelected(holder.getLayoutPosition()==currentPosition);
            Music music = mMusicList.get(position);
            holder.idText.setText(String.valueOf(music.getId()));
            holder.titleText.setText(music.getName());
            holder.artistText.setText(music.getArtist());
            holder.durationText.setText(durationToDate(music.getDuration()));

            if(position == mMusicList.size()-1) {
                holder.updownLine.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //切歌
                    changeMusic(music);

                    //改样式
                    holder.itemView.setSelected(true);
                    tempPosition =currentPosition;
                    currentPosition = holder.getLayoutPosition();
                    notifyItemChanged(tempPosition);

                }
            });

        }
        @Override
        public int getItemCount() {
            return mMusicList.size();
        }
    }

    //为图标绑定事件
    public class OnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.stop){
                stopMusic();
            }
            else if(v.getId()==R.id.pause){
                pauseMusic();
            }
            else if(v.getId()==R.id.last){
                lastMusic();
            }
            else if(v.getId()==R.id.next){
                nextMusic();
            }
            else if(v.getId()==R.id.play){
                playMusic();
            }
        }
    }

    public void changeMusic(Music music){
        //Log.d("Mainactivity",music.getName());
        try {
            isSeekBarChanging = false;
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();

            seekBar.setMax(music.getDuration());
            seekBar.setProgress(0);

            bTitleText.setText(music.getName());
            bDurationText.setText(durationToDate(music.getDuration()));

            if (circle_anim != null) {
                albumImg.startAnimation(circle_anim);  //开始动画
            }
            running();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    //进度条是一直在跑着的，可能会比较耗费资源
    //period是调用间隔
    private void running() {




        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //这里面写事件
                if(!isSeekBarChanging){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        },0,50);
    }

    public void stopMusic(){
        mediaPlayer.stop();
        currentPausePosition=0;
        isSeekBarChanging = true;
        seekBar.setProgress(0);
        albumImg.clearAnimation();
    }
    public void pauseMusic(){
        currentPausePosition = mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();
        albumImg.clearAnimation();
    }
    public void playMusic(){
        if(mediaPlayer!=null&&!mediaPlayer.isPlaying()){
            isSeekBarChanging = false;
            if(currentPausePosition ==0){
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                mediaPlayer.seekTo(currentPausePosition);
                mediaPlayer.start();
            }
        }
        albumImg.startAnimation(circle_anim);
    }
    public void lastMusic(){
        if(currentPosition==0){
            Toast.makeText(this,"已经是第一首歌",Toast.LENGTH_SHORT).show();
        }
        else{
            tempPosition = currentPosition;
            currentPosition--;
            changeCss();
        }
    }
    public void nextMusic(){
        if(currentPosition==musicList.size()-1){
            Toast.makeText(this,"已经是最后一首歌",Toast.LENGTH_SHORT).show();
        }
        else{
            tempPosition = currentPosition;
            currentPosition++;
            changeCss();
        }
    }
    public void changeCss(){
        Music music = musicList.get(currentPosition);
        recyclerView.getChildAt(currentPosition).setSelected(true);
        changeMusic(music);

        adapter.notifyItemChanged(tempPosition);
    }
    //毫秒转成分钟与秒
    @NonNull
    public static String durationToDate(int duration){
        return String.valueOf(duration/60000+":"
                +((duration/1000) - (duration/60000)*60));
    }

}