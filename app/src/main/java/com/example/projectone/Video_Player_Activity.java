package com.example.projectone;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class Video_Player_Activity extends AppCompatActivity {
    private TextView videoNameTv,VideoTimeTV;
    private ImageButton backIb,forwardIB,playPauseIB;
    private SeekBar videoSeekBar;
    private VideoView videoView;
    private RelativeLayout controlRl,videoRL;
    boolean isOpen = true;
    private String videoName,videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        videoName = getIntent().getStringExtra("videoName");
        videoPath = getIntent().getStringExtra("videoPath");
        videoNameTv = findViewById(R.id.idTVVideoName);
        VideoTimeTV = findViewById(R.id.idTVTime);
        backIb = findViewById(R.id.idIBBack);
        forwardIB = findViewById(R.id.idIBForward);
        playPauseIB = findViewById(R.id.idIBPause);
        videoSeekBar = findViewById(R.id.idSeekbarProgressBar);
        videoView = findViewById(R.id.idVideoView);
        controlRl = findViewById(R.id.idRLControl);
        videoRL = findViewById(R.id.idRLVideo);

        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
               videoSeekBar.setMax(videoView.getDuration());
               videoView.start();
            }
        });
        videoNameTv.setText(videoName);
        backIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               videoView.seekTo(videoView.getCurrentPosition()-10000);
            }
        });
        forwardIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.seekTo(videoView.getCurrentPosition()+10000);
            }
        });
        playPauseIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isPlaying()){
                    videoView.pause();
                    playPauseIB.setImageDrawable(getResources().getDrawable(R.drawable.play_ic));
                }else {
                    videoView.start();
                    playPauseIB.setImageDrawable(getResources().getDrawable(R.drawable.pause_ic));
                }
            }
        });
        videoRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen){
                    hideControls();
                    isOpen = false;
                }else {
                    showControls();
                    isOpen = true;
                }
            }
        });

        setHandler();
        initialzeSeekBar();
    }

    private void setHandler() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (videoView.getDuration()>0){
                    int curPos = videoView.getCurrentPosition();
                    videoSeekBar.setProgress(curPos);
                    VideoTimeTV.setText(""+convertTime(videoView.getDuration()-curPos));
                }
                handler.postDelayed(this,0);
            }
        };
        handler.postDelayed(runnable,500);
    }
    private String convertTime(int ms){
        String time;
        int x,seconds,minutes,hours;
        x = ms/100;
        seconds = x%60;
        x /=60;
        minutes = x%60;
        x /=60;
        hours = x%24;
        if (hours!=0){
            time = String.format("%2d",hours)+":"+String.format("%02d",minutes)+":"+String.format("&2d",seconds);
        }else {
            time = String.format("%02d",minutes)+":"+String.format("%02d",seconds);
        }
        return time;
    }

    private void initialzeSeekBar() {
        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (videoSeekBar.getId() == R.id.idSeekbarProgressBar){
                    if (fromUser){
                        videoView.seekTo(progress);
                        videoView.start();
                        int curPos = videoView.getCurrentPosition();
                        VideoTimeTV.setText(""+ convertTime(videoView.getDuration()-curPos));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void showControls() {
        controlRl.setVisibility(View.VISIBLE);

        final Window window = this.getWindow();
        if (window==null){
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView = window.getDecorView();
        if (decorView!=null){
            int uiOption = decorView.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT>=14){
                uiOption&= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if (Build.VERSION.SDK_INT>=16){
                uiOption&= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if (Build.VERSION.SDK_INT>=19){
                uiOption&= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
    }

    private void hideControls() {
        controlRl.setVisibility(View.GONE);

        final Window window = this.getWindow();
        if (window==null){
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView = window.getDecorView();
        if (decorView!=null){
            int uiOption = decorView.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT>=14){
                uiOption |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if (Build.VERSION.SDK_INT>=16){
                uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if (Build.VERSION.SDK_INT>=19){
                uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
    }
}