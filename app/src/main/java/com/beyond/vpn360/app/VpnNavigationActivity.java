package com.beyond.vpn360.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class VpnNavigationActivity extends AppCompatActivity {


    private AlertDialog rateusdialog;
    private ImageView nav;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actiondrawertoggle;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_navigation);

        nav = findViewById(R.id.nav_img);


        drawerLayout = findViewById(R.id.drawer_layout);
        actiondrawertoggle = setupDrawerToggle();

        // Setup toggle to display hamburger icon with nice animation
        actiondrawertoggle.setDrawerIndicatorEnabled(true);
        actiondrawertoggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        drawerLayout.addDrawerListener(actiondrawertoggle);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = newBase.getSharedPreferences("LANG", Context.MODE_PRIVATE);
        super.attachBaseContext(VpnContextWrapper.wrap(newBase, preferences.getString("My_lang", "en")));
    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
    }


    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        switch (menuItem.getItemId()) {

            case R.id.nav_rate:
                showRateusDialog();


                break;


            case R.id.nav_share:

                final String shareurl = "https://play.google.com/store/apps/details?id=com.beyond.vpn360.app";


                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                String sharebody = "360 VPN : Enjoy free vpn proxy with our super fast dedicated servers with securer protocols like ssl & ipsec over openvpn protocol which will encrypt all your traffic.";
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareurl);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, sharebody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));


                break;

            case R.id.nav_privacy:

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://360-vpn.flycricket.io/privacy.html")));
                break;


            case R.id.lang:


                showLanguageDialog();
                break;

            case R.id.nav_speed_test:


             startActivity(new Intent(getApplicationContext(),SpeedTestActivity.class));
                break;
        }


        drawerLayout.closeDrawers();
    }

    private void showLanguageDialog() {
        final String[] langitems = {"english", "हिन्दी", "عربى", "اردو"};
        AlertDialog.Builder langdialog = new AlertDialog.Builder(VpnNavigationActivity.this);
        langdialog.setTitle("Choose Language...");
        langdialog.setSingleChoiceItems(langitems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {
                    setLocale("en");
                    recreate();
                } else if (i == 1) {
                    setLocale("hi");
                    recreate();
                } else if (i == 2) {
                    setLocale("ar");
                    recreate();
                } else if (i == 3) {
                    setLocale("ur");
                    recreate();
                }
                dialog.dismiss();

            }
        });
        AlertDialog mDialog = langdialog.create();
        mDialog.show();

    }

    private void showRateusDialog() {

        final AlertDialog.Builder ratedialogbuilder = new AlertDialog.Builder(this);
        ratedialogbuilder.setTitle(R.string.rate_us_title);
        ratedialogbuilder.setMessage(R.string.rateus_message);
        ratedialogbuilder.setPositiveButton(R.string.rateus_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface ratedialog, int which) {
                ratedialog.dismiss();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.beyond.vpn360.app")));

            }
        });

        ratedialogbuilder.setNegativeButton(R.string.rateus_later, (dialog, which) -> dialog.dismiss());


        rateusdialog = ratedialogbuilder.create();
        rateusdialog.show();

    }


    public void setLocale(String lang) {
        //save in Shaered  preference
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LANG", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("My_lang", lang);  // Saving string
        editor.apply(); // commit changes
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

}
