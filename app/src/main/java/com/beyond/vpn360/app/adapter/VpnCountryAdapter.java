package com.beyond.vpn360.app.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.vpn360.app.UtilsVpnLocations;
import com.bumptech.glide.Glide;
import com.beyond.vpn360.app.R;
import com.beyond.vpn360.app.myhelper.VpnRegionPrefs;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class VpnCountryAdapter extends RecyclerView.Adapter<VpnCountryAdapter.RecentViewHolder> implements Filterable {
    private final Context context;
    private final HashMap<String, Long> selectionMap;
    private List<VpnLocationAdapter> locationList;
    private final LocationClickListener locationClickListener;
    private final SlidingUpPanelLayout dialog;
    private final VpnRegionPrefs vpnRegionPrefs;
    private List<VpnLocationAdapter> filteredLocationList;

    public VpnCountryAdapter(Context context, List<VpnLocationAdapter> locationList, LocationClickListener locationClickListener, SlidingUpPanelLayout dialog, HashMap<String, Long> selectionMap) {
        this.context = context;
        this.locationList = locationList;
        this.filteredLocationList = locationList;
        this.locationClickListener = locationClickListener;
        this.dialog = dialog;
        this.vpnRegionPrefs = new VpnRegionPrefs(context);
        this.selectionMap = selectionMap;
    }

    @Override
    public RecentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spinner_item, viewGroup, false);
        return new RecentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecentViewHolder viewHolder, int position) {
        final VpnLocationAdapter location = filteredLocationList.get(viewHolder.getAdapterPosition());
        viewHolder.txtLocationName.setText(location.getName());

        Glide.with(context).load(location.getCountryflag()).into(viewHolder.ivCountryFlag);

        if (location.isReward()) {
            viewHolder.txtDefault.setVisibility(View.GONE);
            if (selectionMap.containsKey(location.getName()) && !UtilsVpnLocations.checkForUnlock(selectionMap.get(location.getName()))) {
                viewHolder.btnUnlock.setVisibility(View.GONE);
            } else {
                viewHolder.btnUnlock.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.txtDefault.setVisibility(View.VISIBLE);
            viewHolder.btnUnlock.setVisibility(View.GONE);
        }

        if (vpnRegionPrefs.getRegionName().equals(location.getName())) {
            viewHolder.txtDefault.setTextColor(Color.GREEN);
            viewHolder.txtDefault.setText(R.string.default_make);
        } else {
            viewHolder.txtDefault.setText(R.string.make_default);
            viewHolder.txtDefault.setTextColor(Color.BLACK);
        }


        viewHolder.itemView.setOnClickListener(v -> {
            dialog.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            dialog.setVisibility(View.GONE);
            locationClickListener.onClickItemListener(viewHolder.getAdapterPosition(), location);
        });

    }



    @Override
    public int getItemCount() {
        return filteredLocationList.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredLocationList = locationList;
                } else {
                    List<VpnLocationAdapter> filteredList = new ArrayList<>();
                    for (VpnLocationAdapter row : locationList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filteredLocationList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredLocationList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredLocationList = (ArrayList<VpnLocationAdapter>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }


    class RecentViewHolder extends RecyclerView.ViewHolder {
        private TextView txtLocationName;
        private ImageView ivCountryFlag;
        private TextView txtDefault;
        private TextView btnUnlock;

        final Typeface medium = Typeface.createFromAsset(context.getAssets(), "fonts/cera_pro_medium.ttf");
        final Typeface regular = Typeface.createFromAsset(context.getAssets(), "fonts/cera_pro_regular.ttf");
        final Typeface bold = Typeface.createFromAsset(context.getAssets(), "fonts/cera_pro_bold.ttf");

        RecentViewHolder(View itemView) {
            super(itemView);
            txtLocationName = itemView.findViewById(R.id.txt_name);
            ivCountryFlag = itemView.findViewById(R.id.country_flag);
            txtDefault = itemView.findViewById(R.id.txt_default);
            btnUnlock = itemView.findViewById(R.id.btn_unlock);
            txtLocationName.setTypeface(medium);
            txtDefault.setTypeface(regular);
            btnUnlock.setTypeface(bold);


        }
    }

    public interface LocationClickListener {
        void onClickItemListener(int position, VpnLocationAdapter location);
    }
}
