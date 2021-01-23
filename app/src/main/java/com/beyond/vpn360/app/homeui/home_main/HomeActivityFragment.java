package com.beyond.vpn360.app.homeui.home_main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beyond.vpn360.app.UtilsVpnLocations;
import com.beyond.vpn360.app.adapter.VpnLocationAdapter;
import com.beyond.vpn360.app.myhelper.GetSpeedTestHostsHandler;
import com.beyond.vpn360.app.speedtest.HttpDownloadTest;
import com.beyond.vpn360.app.speedtest.HttpUploadTest;
import com.beyond.vpn360.app.speedtest.PingTest;
import com.bumptech.glide.Glide;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
//import com.google.gson.Gson;

import com.beyond.vpn360.app.R;
import com.beyond.vpn360.app.SpeedTestActivity;
import com.beyond.vpn360.app.adapter.VpnCountryAdapter;
import com.beyond.vpn360.app.ads.AdViewController;
import com.beyond.vpn360.app.config.VpnAutoConfig;
import com.beyond.vpn360.app.myhelper.VpnRegionPrefs;
import com.beyond.vpn360.app.myhelper.RewardAdPref;
import com.beyond.vpn360.app.myhelper.MyVPNHelper;
import com.google.android.gms.internal.ads.zzaiu;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import de.blinkt.openvpn.core.ConnectionStatus;
import de.blinkt.openvpn.core.VpnStatus;

public class HomeActivityFragment extends Fragment implements VpnStatus.StateListener, VpnCountryAdapter.LocationClickListener {

    private VpnAutoConfig mConfig;
    private TextView tvstatusview, or_text, tv_connected, tv_upspeed, tv_downspeed, taplocation;
    private AdView footerAdView;
    private VpnCountryAdapter adapter;
    private AdViewController controller;
    private View vStatus;
    private ImageView imageviewstatus, cornerview_circle;
    private CardView cvStatus, cv_location;
    private boolean isConnected = false;

    private Typeface bold, medium;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isConnected) {
                myVpnHelper.disconnectFromVpn();
            }
        }
    };
    private InterstitialAd interstitialAdv;
    private SlidingUpPanelLayout locationlayout;
    private MyVPNHelper myVpnHelper;
    private VpnRegionPrefs vpnRegionPrefs;
    private AlertDialog defaultlocationdialog;
    private AlertDialog Rewarddialog;

    private CardView cv_auto;
    private VpnLocationAdapter vpnSelectedLocation;
    private static final String REWARD_UNIT_ID = "place your id here";
    private RewardedAd rewardedAd;
    private boolean isLoading;
    private RewardAdPref rewardadpref;
    private HashMap<String, Long> selectionMap;
    private LinearLayout speed_linear;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initView(root);


        mConfig = new VpnAutoConfig(getContext());
        controller = new AdViewController(getActivity());
        myVpnHelper = new MyVPNHelper(getContext(), mConfig);
        vpnRegionPrefs = new VpnRegionPrefs(getContext());
        rewardadpref = new RewardAdPref(getContext());

        selectionMap = rewardadpref.getLocationMap();

        footerAdView = root.findViewById(R.id.av_footer);
        controller.loadBannerAds(footerAdView);


        interstitialAdv = new InterstitialAd(getContext());
        interstitialAdv.setAdUnitId("place your id here");
        loadInterstitialAd();
        loadRewardedAd();


        return root;
    }

    public void loadlocale() {
        SharedPreferences prefs = getContext().getSharedPreferences("LANG", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "");
        setLocale(language);
    }


    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = getContext().getResources();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getContext().createConfigurationContext(configuration);
        }
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    private void initView(View view) {


        tvstatusview = view.findViewById(R.id.textview_status);

        vStatus = view.findViewById(R.id.v_status);
        imageviewstatus = view.findViewById(R.id.imageview_status);
        cvStatus = view.findViewById(R.id.cardview_status);
        cornerview_circle = view.findViewById(R.id.imageview_circle);


        locationlayout = view.findViewById(R.id.bottom_layout);


        tv_connected = view.findViewById(R.id.text_connected);
        tv_downspeed = view.findViewById(R.id.tv_downloadspeed);
        tv_upspeed = view.findViewById(R.id.tv_upload);
        cv_location = view.findViewById(R.id.cardview_location);


        taplocation = view.findViewById(R.id.taplocation);


        bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/cera_pro_bold.ttf");
        medium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/cera_pro_medium.ttf");
        speed_linear = view.findViewById(R.id.speed_linear);

        tvstatusview.setTypeface(medium);

        tv_connected.setTypeface(bold);


        cvStatus.setOnClickListener(view1 -> {
            boolean isActive = VpnStatus.isVPNActive();
            if (!isActive) {
                if (checkDefaultRegion()) {
                    showlocationlayout();
                } else {
                    String locationFileName = vpnRegionPrefs.getRegion();
                    vpnRegionPrefs.setSelectedLocation(vpnRegionPrefs.getRegionName());
                    connecttoVPN(locationFileName);
                    Log.d("Region", locationFileName);
                }
            } else {
                locationlayout.setVisibility(View.GONE);
                myVpnHelper.disconnectFromVpn();
            }
        });




        cv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showlocationlayout();

            }
        });

    }

    private void loadInterstitialAd() {
        if (!interstitialAdv.isLoading() && !interstitialAdv.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("BAD4BF3E75085088079835BCF34EB526").addTestDevice("206942360A63A907F9B8D906096C537F").addTestDevice("EB798838C864BCDFF7A12729ECCD95B6").build();
            interstitialAdv.loadAd(adRequest);
        }
    }


    private void loadRewardedAd() {
        if (rewardedAd == null || !rewardedAd.isLoaded()) {
            rewardedAd = new RewardedAd(getContext(), REWARD_UNIT_ID);
            isLoading = true;
            rewardedAd.loadAd(
                    new AdRequest.Builder().build(),
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onRewardedAdLoaded() {
                            // Ad successfully loaded.
                            isLoading = false;

                        }

                        @Override
                        public void onRewardedAdFailedToLoad(int errorCode) {
                            // Ad failed to load.
                            isLoading = false;

                        }
                    });
        }
    }

    private boolean checkDefaultRegion() {
        return vpnRegionPrefs.getRegion().isEmpty();
    }

    private void showInterstitialAd() {

        if (interstitialAdv != null && interstitialAdv.isLoaded()) {
            interstitialAdv.show();
        } else {
            loadInterstitialAd();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (footerAdView != null) {
            footerAdView.pause();
        }

        release();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (footerAdView != null) {
            footerAdView.resume();
        }


        VpnStatus.addStateListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (footerAdView != null) {
            footerAdView.destroy();
        }
        removeCallbacks();

        release();
    }

    private void removeCallbacks() {
        try {
            handler.removeCallbacks(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
    }

    public void release() {
        VpnStatus.removeStateListener(this);
    }


    private boolean checkForUnlock(long time_ago) {
        long cur_time = System.currentTimeMillis();
        long time_elapsed = (cur_time - time_ago) / 1000;
        int hours = Math.round(time_elapsed / 3600);

        return hours > 24;
    }

    private void showlocationlayout() {
        locationlayout.setVisibility(View.VISIBLE);
        adapter = new VpnCountryAdapter(getContext(), UtilsVpnLocations.getLocatons(getContext()), this, locationlayout, selectionMap);
        final RecyclerView recyclerView = locationlayout.findViewById(R.id.recycler_locations);
        final ImageView imageView = locationlayout.findViewById(R.id.img_back);

        final CardView rel_auto = locationlayout.findViewById(R.id.cv_autoconnect);

        final EditText etSearch = locationlayout.findViewById(R.id.edit_search);

        final LinearLayoutManager ab = new LinearLayoutManager(getContext());


        rel_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationlayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                locationlayout.setVisibility(View.GONE);
                String name = UtilsVpnLocations.getRandomFastServer();
                vpnRegionPrefs.setSelectedLocation(name.split("\\.")[0]);
                connecttoVPN(name);
            }
        });


        recyclerView.setLayoutManager(ab);
        recyclerView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s.toString());
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationlayout.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void updateState(String state, String logmessage, int localizedResId, final ConnectionStatus level, Intent intent) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String stateMessage = VpnStatus.getLastCleanLogMessage(getContext());
                updateDesign(stateMessage, level);
            }
        });
    }

    @Override
    public void setConnectedVPN(String uuid) {

    }

    private void updateDesign(String stateMessage, ConnectionStatus level) {

        String vpnSelectedLocation = vpnRegionPrefs.getSelectedLocation();

        if (level == ConnectionStatus.LEVEL_CONNECTED) {
            cvStatus.setEnabled(true);
            tvstatusview.setText(String.format("%s  : %s", getString(R.string.connected), vpnSelectedLocation));
            showInterstitialAd();
            vStatus.setBackgroundResource(R.drawable.connect_bg);
            taplocation.setText(String.format("%s To : %s", getString(R.string.connected), vpnSelectedLocation));
            Glide.with(getContext()).load(R.drawable.ic_off).into(imageviewstatus);
            Glide.with(getContext()).load(R.drawable.ic_corner_circle1).into(cornerview_circle);
            tv_connected.setText("Stop");
            speed_linear.setVisibility(View.GONE);
            isConnected = true;
            cv_location.setClickable(false);
        } else if (level == ConnectionStatus.LEVEL_NOTCONNECTED) {
            tvstatusview.setText(getString(R.string.disconnected));
            cvStatus.setEnabled(true);
            vStatus.setBackgroundResource(R.drawable.disconnect_bg);
            Glide.with(getContext()).load(R.drawable.ic_onn).into(imageviewstatus);
            Glide.with(getContext()).load(R.drawable.ic_corner_circle).into(cornerview_circle);
            cv_location.setClickable(true);
            taplocation.setText(getString(R.string.tap_region));
            tv_connected.setText("start");
            speed_linear.setVisibility(View.GONE);
            showInterstitialAd();
        } else {
            cvStatus.setEnabled(true);
            tvstatusview.setText(stateMessage);
            vStatus.setBackgroundResource(R.drawable.status_bg);
        }
    }


    @Override
    public void onClickItemListener(int position, VpnLocationAdapter vpnLocationAdapter) {

        if (vpnLocationAdapter.isReward()) {
            vpnSelectedLocation = vpnLocationAdapter;
            vpnRegionPrefs.setSelectedLocation(vpnLocationAdapter.getName());
            showRewareddAd();
        } else {
            if (vpnRegionPrefs.checkForCurrentRegion(vpnLocationAdapter.getName())) {
                vpnRegionPrefs.setSelectedLocation(vpnLocationAdapter.getName());
                connecttoVPN(UtilsVpnLocations.getRandomServer(vpnLocationAdapter.getVpnFileName()));
            } else {
                showDefaultDialog(position, vpnLocationAdapter.getName(), UtilsVpnLocations.getRandomServer(vpnLocationAdapter.getVpnFileName()));
            }
        }

    }

    private void showRewareddAd() {
        if (selectionMap.get(vpnSelectedLocation.getName()) == null) {
            showRewardaddialog();
        } else {
            long time = selectionMap.get(vpnSelectedLocation.getName());
            if (checkForUnlock(time)) {
                showRewardaddialog();
            } else {
                connecttoVPN(UtilsVpnLocations.getRandomServer(vpnSelectedLocation.getVpnFileName()));
                vpnSelectedLocation = null;
            }
        }
    }

    private void showAd() {
        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback =
                    new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {

                        }

                        @Override
                        public void onRewardedAdClosed() {
                            loadRewardedAd();
                        }

                        @Override
                        public void onUserEarnedReward(RewardItem rewardItem) {
                            vpnRegionPrefs.setSelectedLocation(vpnSelectedLocation.getName());

                            selectionMap.put(vpnSelectedLocation.getName(), System.currentTimeMillis());
                            rewardadpref.saveLocationMap(selectionMap);
                            vpnSelectedLocation = null;
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
                            vpnSelectedLocation = null;
                            loadRewardedAd();

                        }
                    };
            rewardedAd.show(getActivity(), adCallback);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the fragment menu items.
        inflater.inflate(R.menu.navigation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();


        if (itemId == R.id.menu_location) {
            showlocationlayout();
        } else if (itemId == R.id.menu_speed) {
            Intent intent = new Intent(getActivity(), SpeedTestActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    private void startTimer() {
        removeCallbacks();
        handler.postDelayed(runnable, 10000);
    }


    private void showDefaultDialog(final int position, final String location, final String locationFileName) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.defauldialog_title);
        builder.setMessage(R.string.default_message);
        builder.setPositiveButton(R.string.default_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                vpnRegionPrefs.saveRegion(location, locationFileName, position);
                vpnRegionPrefs.setSelectedLocation(location);
                connecttoVPN(locationFileName);
                Log.d("region 1", locationFileName);


            }
        });

        builder.setNegativeButton(R.string.default_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                vpnRegionPrefs.setSelectedLocation(location);
                connecttoVPN(locationFileName);
                Log.d("Region", locationFileName);
            }
        });

        defaultlocationdialog = builder.create();
        defaultlocationdialog.show();

    }

    private void showRewardaddialog() {
        final AlertDialog.Builder rewardbuilder = new AlertDialog.Builder(getActivity());
        rewardbuilder.setTitle(R.string.reward_title);
        rewardbuilder.setMessage(R.string.reward_message);
        rewardbuilder.setPositiveButton(R.string.reward_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showAd();


            }
        });

        rewardbuilder.setNegativeButton(R.string.reward_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        defaultlocationdialog = rewardbuilder.create();
        defaultlocationdialog.show();

    }

    private void connecttoVPN(final String locationFileName) {
        startTimer();
        myVpnHelper.connectOrDisconnect(locationFileName);

    }



}