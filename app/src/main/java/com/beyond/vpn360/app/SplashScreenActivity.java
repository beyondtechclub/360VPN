package com.beyond.vpn360.app;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.util.Locale;


public class SplashScreenActivity extends AppCompatActivity {


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            intentMain();
        }
    };

    private Handler handler = new Handler();
    private ImageView imgicon;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);


        final Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/cera_pro_medium.ttf");
        final Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/cera_pro_regular.ttf");
        final Typeface bold = Typeface.createFromAsset(getAssets(), "fonts/cera_pro_bold.ttf");



        imgicon = findViewById(R.id.app_icon);

//        Glide.with(SplashScreenActivity.this).load(R.drawable.ic_logo_big).into(imgicon);


        loadlocale();
    }

    public void loadlocale(){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("LANG", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang","");
        setLocale(language);
    }

    public void setLocale(String lang) {


        Resources activityRes = getResources();
        Configuration activityConf = activityRes.getConfiguration();
        Locale newLocale = new Locale(lang);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            activityConf.setLocale(newLocale);
        }
        activityRes.updateConfiguration(activityConf, activityRes.getDisplayMetrics());

        Resources applicationRes = getApplicationContext().getResources();
        Configuration applicationConf = applicationRes.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            applicationConf.setLocale(newLocale);
        }
        applicationRes.updateConfiguration(applicationConf,
                applicationRes.getDisplayMetrics());
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (handler != null) {
            handler.postDelayed(runnable, 2000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    private void intentMain() {
        Intent intent = new Intent(SplashScreenActivity.this, VpnNavigationActivity.class);
        startActivity(intent);
        SplashScreenActivity.this.finish();
    }
}
