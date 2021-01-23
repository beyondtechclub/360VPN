package com.beyond.vpn360.app.myhelper;

import android.content.Context;

import com.beyond.vpn360.app.config.VpnAutoConfig;

import de.blinkt.openvpn.OpenVpnConnector;
import de.blinkt.openvpn.core.VpnStatus;


public class MyVPNHelper {

    private VpnAutoConfig config;
    private Context context;

    public MyVPNHelper(Context context, VpnAutoConfig config) {
        this.context = context;
        this.config = config;
    }

    public void connectOrDisconnect(String locationFileName) {
        boolean isActive = VpnStatus.isVPNActive();
        if (isActive) {
            disconnectFromVpn();
        } else {
            connectToVpn(locationFileName);
        }
    }

    public void connectToVpn(String locationFileName) {
        try {
            String autoConfig = config.getAutoConfig(locationFileName);
            OpenVpnConnector.connectToVpn(context, autoConfig, null, null);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void disconnectFromVpn() {
        OpenVpnConnector.disconnectFromVpn(context);
    }

}
