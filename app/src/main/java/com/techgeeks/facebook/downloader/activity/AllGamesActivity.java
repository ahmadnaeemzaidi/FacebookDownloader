package com.techgeeks.facebook.downloader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.techgeeks.facebook.downloader.R;
import com.techgeeks.facebook.downloader.databinding.ActivityAllGamesBinding;
import com.techgeeks.facebook.downloader.util.Ads;

public class AllGamesActivity extends AppCompatActivity {
    ActivityAllGamesBinding binding;
    AllGamesActivity activity;
    private Ads methods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_games);
        activity = this;
        methods = new Ads();
        FrameLayout frameLayout = findViewById(R.id.id_native_ad);
        methods.loadNativeAd(this,frameLayout);
        initViews();
    }

    private void initViews() {
        binding.imBack.setOnClickListener(v -> onBackPressed());
        binding.RL2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, GamesPlayActivity.class);
                i.putExtra("url","2048");
                startActivity(i);
            }
        });
        binding.RLHelix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, GamesPlayActivity.class);
                i.putExtra("url","Helix");
                startActivity(i);
            }
        });
    }
}