package com.beyond.vpn360.app.myhelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class RewardAdPref {
    private final SharedPreferences sharedPreferences;
    private final static String LOCATION_MAP = "location_map";
    private final Gson gson;

    public RewardAdPref(Context context) {
        sharedPreferences = context.getSharedPreferences("com.open.vpn.app.reward_pref", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public HashMap<String, Long> getLocationMap() {

        Type type = new TypeToken<HashMap<String,Long>>(){}.getType();

        String map = sharedPreferences.getString(LOCATION_MAP, null);

        if (map == null) {
            return new HashMap<>();
        }
        return gson.fromJson(map, type);

    }

    public void saveLocationMap(HashMap<String,Long> map) {
        String mapString = gson.toJson(map);
        sharedPreferences.edit().putString(LOCATION_MAP, mapString).apply();
    }

}

