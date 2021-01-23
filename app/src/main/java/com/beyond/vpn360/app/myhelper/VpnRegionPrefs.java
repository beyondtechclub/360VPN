package com.beyond.vpn360.app.myhelper;

import android.content.Context;
import android.content.SharedPreferences;

public class VpnRegionPrefs {
    private final SharedPreferences sharedPreferences;
    private final static String LOCATION_FILE = "location_file";
    private final static String LOCATION_NAME = "location_name";
    private final static String LOCATION_POSITION = "position";
    private final static String LOCATION_SELECTED = "selected_location";
    private final static String HELP_TEXT = "help_text_shown";

    public VpnRegionPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences("com.open.vpn.app.region_pref", Context.MODE_PRIVATE);
    }

    public String getRegion() {
        return sharedPreferences.getString(LOCATION_FILE, "");
    }

    public void saveRegion(String location, String locationFileName, int position) {
        sharedPreferences.edit().putString(LOCATION_NAME, location).apply();
        sharedPreferences.edit().putString(LOCATION_FILE, locationFileName).apply();
        sharedPreferences.edit().putInt(LOCATION_POSITION, position).apply();
    }

    public String getRegionName() {
        return sharedPreferences.getString(LOCATION_NAME, "");
    }

    public int getPosition() {
        return sharedPreferences.getInt(LOCATION_POSITION, -1);
    }


    public String getSelectedLocation() {
        return sharedPreferences.getString(LOCATION_SELECTED, "");
    }

    public void setSelectedLocation(String locationName) {
        sharedPreferences.edit().putString(LOCATION_SELECTED, locationName).apply();
    }

    public boolean getHelpShown() {
        return sharedPreferences.getBoolean(HELP_TEXT, false);
    }

    public void setHelpShown(boolean status) {
        sharedPreferences.edit().putBoolean(HELP_TEXT, status).apply();
    }

    public boolean checkForCurrentRegion(String location) {
        String selectedLocation  = getRegionName();
        return !selectedLocation.isEmpty() && selectedLocation.equalsIgnoreCase(location);
    }
}
