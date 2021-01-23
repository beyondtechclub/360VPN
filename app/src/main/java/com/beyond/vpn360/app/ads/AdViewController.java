package com.beyond.vpn360.app.ads;

import android.app.Activity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class AdViewController {

    private final Activity context;

    private InterstitialAd interstitialAd;

    public AdViewController(Activity context) {
        this.context = context;
    }


    public void loadBannerAds(AdView footerAdView) {

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("BAD4BF3E75085088079835BCF34EB526")
                .addTestDevice("206942360A63A907F9B8D906096C537F")
                .addTestDevice("EB798838C864BCDFF7A12729ECCD95B6")
                .build();

        footerAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
//                    Toast.makeText(context, "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
//                Toast.makeText(context, "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
//                Toast.makeText(context, "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        footerAdView.loadAd(adRequest);
    }
}
