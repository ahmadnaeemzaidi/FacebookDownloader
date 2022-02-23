package com.techgeeks.facebook.downloader.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import com.techgeeks.facebook.downloader.R;
import com.techgeeks.facebook.downloader.util.AppLangSessionManager;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {
    SplashScreen activity;
    Context context;
    AppLangSessionManager appLangSessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = activity = this;
        appLangSessionManager = new AppLangSessionManager(activity);

        setLocale(appLangSessionManager.getLanguage());
        HomeScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        appLangSessionManager = new AppLangSessionManager(activity);
    }

    public void HomeScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
            }
        }, 1000);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode != RESULT_OK) {
                HomeScreen();
            } else {
                HomeScreen();
            }
        }
    }


    public void setLocale(String lang) {
        if (lang.equals("")){
            lang="en";
        }
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);



    }


}
