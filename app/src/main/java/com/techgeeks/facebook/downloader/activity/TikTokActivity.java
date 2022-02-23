package com.techgeeks.facebook.downloader.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.techgeeks.facebook.downloader.R;
import com.techgeeks.facebook.downloader.api.CommonClassForAPI;
import com.techgeeks.facebook.downloader.databinding.LayoutGlobalUiBinding;
import com.techgeeks.facebook.downloader.model.TiktokModel;
import com.techgeeks.facebook.downloader.util.Adclick;
import com.techgeeks.facebook.downloader.util.Ads;
import com.techgeeks.facebook.downloader.util.AppLangSessionManager;
import com.techgeeks.facebook.downloader.util.SharePrefs;
import com.techgeeks.facebook.downloader.util.Utils;

import java.util.Locale;

import io.reactivex.observers.DisposableObserver;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.techgeeks.facebook.downloader.util.Utils.RootDirectoryTikTok;
import static com.techgeeks.facebook.downloader.util.Utils.createFileFolder;
import static com.techgeeks.facebook.downloader.util.Utils.startDownload;

public class TikTokActivity extends AppCompatActivity implements Adclick {
    private LayoutGlobalUiBinding binding;
    TikTokActivity activity;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;
    private ClipboardManager clipBoard;
    AppLangSessionManager appLangSessionManager;
    private Ads methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_global_ui);
        activity = this;
        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        methods = new Ads();
        methods.interstitialLoad(this);
        FrameLayout frameLayout = findViewById(R.id.id_native_ad);
        methods.loadNativeAd(this,frameLayout);
        createFileFolder();
        initViews();

        binding.imAppIcon.setImageDrawable(getResources().getDrawable(R.drawable.tiktok_logo));
        binding.tvAppName.setText(getResources().getString(R.string.tiktok_app_name));


    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        PasteText();
    }

    private void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.imInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
            }
        });

        Glide.with(activity)
                .load(R.drawable.tt1)
                .into(binding.layoutHowTo.imHowto1);

        Glide.with(activity)
                .load(R.drawable.tt2)
                .into(binding.layoutHowTo.imHowto2);

        Glide.with(activity)
                .load(R.drawable.tt3)
                .into(binding.layoutHowTo.imHowto3);

        Glide.with(activity)
                .load(R.drawable.tt4)
                .into(binding.layoutHowTo.imHowto4);


        binding.layoutHowTo.tvHowTo1.setText(getResources().getString(R.string.open_tiktok));
        binding.layoutHowTo.tvHowTo3.setText(getResources().getString(R.string.open_tiktok));
        if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISSHOWHOWTOTT)) {
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISSHOWHOWTOTT, true);
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
        } else {
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.GONE);
        }

        binding.tvPaste.setOnClickListener(v -> {
            PasteText();
        });

        binding.loginBtn1.setOnClickListener(v -> {
            String LL = binding.etText.getText().toString();
            if (LL.equals("")) {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            } else {
                GetTikTokData();
            }
        });

        binding.LLOpenApp.setOnClickListener(v -> {
            Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically.go");
            Intent launchIntent1 = activity.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically");
            if (launchIntent != null) {
                activity.startActivity(launchIntent);
            } else if (launchIntent1 != null) {
                activity.startActivity(launchIntent1);
            } else {
                Utils.setToast(activity, getResources().getString(R.string.app_not_available));
            }

        });
    }

    private void GetTikTokData() {
        try {
            createFileFolder();
            String host = binding.etText.getText().toString();
            if (host.contains("tiktok")) {
                Utils.showProgressDialog(activity);
                callVideoDownload(binding.etText.getText().toString());
                showInterstitialAds();

            } else {
                Utils.setToast(activity, "Enter Valid Url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void callVideoDownload(String Url) {
        try {
            Utils utils = new Utils(activity);
            if (utils.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    commonClassForAPI.callTiktokVideo(tiktokObserver, Url);
                }
            } else {
                Utils.setToast(activity, "No Internet Connection");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private DisposableObserver<TiktokModel> tiktokObserver = new DisposableObserver<TiktokModel>() {
        @Override
        public void onNext(TiktokModel tiktokModel) {
            Utils.hideProgressDialog(activity);
            try {
                if (tiktokModel.getResponsecode().equals("200")) {
                    startDownload(tiktokModel.getData().getMainvideo(),
                            RootDirectoryTikTok, activity, "tiktok_"+System.currentTimeMillis()+".mp4");
                    binding.etText.setText("");
                    showInterstitialAds();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog(activity);
            e.printStackTrace();
        }
        @Override
        public void onComplete() {
            Utils.hideProgressDialog(activity);
        }
    };

    private void PasteText() {
        try {
            binding.etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {

                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("tiktok")) {
                        binding.etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("tiktok")) {
                        binding.etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("tiktok")) {
                    binding.etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


    }


    private void showInterstitialAds() {
        if (methods.interstitialAd != null && methods.interstitialAd.isLoaded()) {
            methods.showInd(this,this);
        }
    }

    //RewardedAds : End

    @Override
    public void onclicl() {

    }
}