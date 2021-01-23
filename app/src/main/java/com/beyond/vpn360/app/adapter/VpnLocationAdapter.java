package com.beyond.vpn360.app.adapter;

public class VpnLocationAdapter {
    private String name;
    private String[] vpnFileName;
    private boolean isReward;


    public int getCountryflag() {
        return countryflag;
    }

    public void setCountryflag(int countryflag) {
        this.countryflag = countryflag;
    }

    private int countryflag;

    public VpnLocationAdapter(String name, String[] vpnFileName, int countryflag, boolean isReward) {
        this.name = name;
        this.vpnFileName = vpnFileName;
        this.countryflag = countryflag;
        this.isReward = isReward;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getVpnFileName() {
        return vpnFileName;
    }

    public void setVpnFileName(String[] vpnFileName) {
        this.vpnFileName = vpnFileName;
    }

    public boolean isReward() {
        return isReward;
    }

}
