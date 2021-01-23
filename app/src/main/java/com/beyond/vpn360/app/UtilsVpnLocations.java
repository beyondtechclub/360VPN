package com.beyond.vpn360.app;

import android.content.Context;

import com.beyond.vpn360.app.adapter.VpnLocationAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class UtilsVpnLocations {


    public static List<VpnLocationAdapter> getLocatons(Context context) {
        // Spinner Drop down elements
        List<VpnLocationAdapter> locations = new ArrayList<VpnLocationAdapter>();

        //for users


//        locations.add(new VpnLocationAdapter("test", new String[]{"test.ovpn"}, R.drawable.ic_australia, false));
        locations.add(new VpnLocationAdapter(context.getResources().getString(R.string.austrailia_country), new String[]{"Sydney.ovpn", "Sydney2.ovpn"}, R.drawable.ic_australia, false));
        locations.add(new VpnLocationAdapter(context.getString(R.string.canada_country), new String[]{"Canada.ovpn", "Canada2.ovpn"}, R.drawable.ic_canada, false));
        locations.add(new VpnLocationAdapter(context.getString(R.string.england_country), new String[]{"London.ovpn", "London2.ovpn"}, R.drawable.ic_united_kingdom, false));
        locations.add(new VpnLocationAdapter(context.getString(R.string.france_country), new String[]{"France.ovpn", "France2.ovpn"}, R.drawable.ic_france, false));
        locations.add(new VpnLocationAdapter(context.getString(R.string.germany_country), new String[]{"Germany.ovpn", "Germany2.ovpn"}, R.drawable.ic_germany, false));
        locations.add(new VpnLocationAdapter(context.getString(R.string.india_country), new String[]{"India.ovpn","india2.ovpn"}, R.drawable.ic_india, false));
        locations.add(new VpnLocationAdapter(context.getString(R.string.ireland_country), new String[]{"Ireland.ovpn", "Ireland2.ovpn"}, R.drawable.ic_ireland, false));
        locations.add(new VpnLocationAdapter(context.getString(R.string.japan_country), new String[]{"Japan.ovpn","Japan2.ovpn"}, R.drawable.ic_japan, false));
        locations.add(new VpnLocationAdapter(context.getString(R.string.netherlands_country), new String[]{"netherlands.ovpn", "netherlands2.ovpn"}, R.drawable.ic_netherlands, false));
        locations.add(new VpnLocationAdapter(context.getString(R.string.singapore_country), new String[]{"Singapore.ovpn", "Singapore2.ovpn"}, R.drawable.ic_singapore, false));
        locations.add(new VpnLocationAdapter(context.getString(R.string.southkorea_country), new String[]{"Korea.ovpn", "Korea2.ovpn"}, R.drawable.ic_south_korea, false));
        locations.add(new VpnLocationAdapter(context.getString(R.string.usa_country), new String[]{"US.ovpn", "USA2.ovpn"}, R.drawable.ic_united_states_of_america, false));


        return locations;

    }

    public static String getRandomFastServer() {
        String[] servers = new String[]{ "netherlands_fast.ovpn", "germany_fast.ovpn"};
        Random random = new Random();
        return servers[random.nextInt(servers.length)];
    }

    public static String getRandomServer(String[] servers) {
        Random random = new Random();
        return servers[random.nextInt(servers.length)];
    }

    public static boolean checkForUnlock(long time_ago) {
        long cur_time = System.currentTimeMillis();
        long time_elapsed = (cur_time - time_ago) / 1000;
        int hours = Math.round(time_elapsed / 3600);

        return hours > 24;
    }

}
