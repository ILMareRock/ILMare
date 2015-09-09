package moe.mzry.ilmare.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import moe.mzry.ilmare.MainApp;
import moe.mzry.ilmare.R;
import moe.mzry.ilmare.service.IlMareLocationProvider;
import moe.mzry.ilmare.service.data.Message;
import moe.mzry.ilmare.service.data.MessageComparators;
import moe.mzry.ilmare.service.data.eddystone.Beacon;

import static com.google.android.gms.internal.zzhu.runOnUiThread;

/**
 * Message list adapter
 */
public class BeaconListAdapter extends RecyclerView.Adapter<BeaconListAdapter.BeaconViewHolder> {

    public static class BeaconViewHolder extends RecyclerView.ViewHolder {
        private TextView beaconId;
        private TextView distance;

        public BeaconViewHolder(View messageView) {
            super(messageView);
            beaconId = (TextView) messageView.findViewById(R.id.beacon_id);
            distance = (TextView) messageView.findViewById(R.id.beacon_distance);
        }

        public void setBeacon(Beacon beacon) {
            beaconId.setText(beacon.getDeviceAddress());
            distance.setText("" + beacon.distanceFromRssi());
        }
    }

    private List<Beacon> beaconList;

    public BeaconListAdapter() {
        beaconList = new ArrayList<>();
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                IlMareLocationProvider locationProvider =
                                        MainApp.getLocationProvider();
                                if (locationProvider != null) {
                                    apply(locationProvider.getNearbyBeacons());
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
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
