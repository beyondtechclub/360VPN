package com.beyond.vpn360.app.config;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class VpnAutoConfig {
    private Context mContext;
    private String mCacheString;

    public VpnAutoConfig(Context context) {
        mContext = context;
    }

    public String getAutoConfig(String file) {
        return getAutoConfigFromAsset(file);
    }

    private String getAutoConfigFromAsset(String file) {
        String config = "";
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = mContext.getAssets().open(file);
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while (true) {
                line = br.readLine();
                if (line == null)
                    break;
                config += line + "\n";
            }
            br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }
}
