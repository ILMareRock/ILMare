package moe.mzry.ilmare.fragments;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import moe.mzry.ilmare.MainApp;
import moe.mzry.ilmare.R;
import moe.mzry.ilmare.service.IlMareLocationProvider;
import moe.mzry.ilmare.service.data.eddystone.Beacon;

import static com.google.android.gms.internal.zzhu.runOnUiThread;

/**
 * Message list adapter
 */
public class BeaconListAdapter extends RecyclerView.Adapter<BeaconListAdapter.BeaconViewHolder> {

    public static class BeaconViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView beaconId;
        private TextView distance;

        public BeaconViewHolder(View messageView) {
            super(messageView);
            beaconId = (TextView) messageView.findViewById(R.id.beacon_id);
            distance = (TextView) messageView.findViewById(R.id.beacon_distance);
            messageView.setOnClickListener(this);
        }

        public void setBeacon(Beacon beacon) {
            beaconId.setText(beacon.getDeviceAddress());
            distance.setText("" + beacon.distanceFromRssi());
        }

        @Override
        public void onClick(View v) {
        }
    }

    private List<Beacon> beaconList;

    public BeaconListAdapter() {
        beaconList = new ArrayList<>();
    }

    public void apply(List<Beacon> beacons) {
        beaconList = beacons;
        this.notifyDataSetChanged();
    }

    @Override
    public BeaconViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.beacon_list_item,
                viewGroup, false);
        return new BeaconViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BeaconViewHolder viewHolder, int i) {
        viewHolder.setBeacon(beaconList.get(i));
    }

    @Override
    public int getItemCount() {
        return beaconList.size();
    }
}
