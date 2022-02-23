package com.techgeeks.facebook.downloader.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.techgeeks.facebook.downloader.R;
import com.techgeeks.facebook.downloader.util.Adclick;
import com.techgeeks.facebook.downloader.util.Ads;


public class VideoPlayerActivity extends AppCompatActivity implements Adclick {

    private Ads methods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_player);
        VideoView videoView=findViewById(R.id.videoView);
        methods = new Ads();
        LinearLayout adView = findViewById(R.id.fb_banner_Container);
        methods.BannerAd(this,adView);
        methods.interstitialLoad(this);
        Intent intent=getIntent();
        String videoPath=intent.getStringExtra("PathVideo");



        try {
            MediaController mediaController = new MediaController(VideoPlayerActivity.this);
            mediaController.setAnchorView(videoView);
            Uri video = Uri.parse(videoPath);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.start();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    showInterstitialAds();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void showInterstitialAds() {
        if (methods.interstitialAd != null && methods.interstitialAd.isLoaded()) {
            methods.showInd(this,this);
        }
    }

    @Override
    public void onclicl() {

    }
}