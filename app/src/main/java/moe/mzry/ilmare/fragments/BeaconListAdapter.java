package moe.mzry.ilmare.fragments;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import moe.mzry.ilmare.MainApp;
import moe.mzry.ilmare.R;
import moe.mzry.ilmare.service.Callback;
import moe.mzry.ilmare.service.data.FirebaseLatLng;
import moe.mzry.ilmare.service.data.LocationSpec;
import moe.mzry.ilmare.service.data.eddystone.Beacon;

/**
 * Message list adapter
 */
public class BeaconListAdapter extends RecyclerView.Adapter<BeaconListAdapter.BeaconViewHolder> {

    public static class BeaconViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView beaconId;
        private TextView distance;
        private Beacon beacon;

        public BeaconViewHolder(View messageView) {
            super(messageView);
            beaconId = (TextView) messageView.findViewById(R.id.beacon_id);
            distance = (TextView) messageView.findViewById(R.id.beacon_distance);
            messageView.setOnClickListener(this);
        }

        public void setBeacon(Beacon beacon) {
            this.beacon = beacon;
            beaconId.setText(beacon.getDeviceAddress());
            distance.setText("" + beacon.distanceFromRssi());
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),
                    R.style.Theme_AppCompat_Light_Dialog);
            builder.setView(R.layout.dialog_beacon_editor);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Save",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText latitude =
                                    (EditText) alertDialog.findViewById(R.id.beacon_lat_edit_text);
                            EditText longitude =
                                    (EditText) alertDialog.findViewById(R.id.beacon_lng_edit_text);
                            EditText level = (EditText) alertDialog.findViewById(
                                    R.id.beacon_level_edit_text);
                            Double lat = Double.valueOf(latitude.getText().toString());
                            Double lng = Double.valueOf(longitude.getText().toString());
                            Long lev = Long.valueOf(level.getText().toString());
                            MainApp.getLocationProvider().setBeaconLocation(beacon,
                                    new LocationSpec(new FirebaseLatLng(lat, lng), lev, 1.0));
                        }
                    });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                        }
                    });
            MainApp.getLocationProvider().getBeaconLocation(beacon, new Callback<LocationSpec>() {
                @Override
                public void apply(LocationSpec data) {
                    alertDialog.show();
                    EditText latitude =
                            (EditText) alertDialog.findViewById(R.id.beacon_lat_edit_text);
                    EditText longitude =
                            (EditText) alertDialog.findViewById(R.id.beacon_lng_edit_text);
                    EditText level =
                            (EditText) alertDialog.findViewById(R.id.beacon_level_edit_text);
                    latitude.setText("" + data.getLocation().getLatitude());
                    longitude.setText("" + data.getLocation().getLongitude());
                    level.setText("" + data.getLevel());
                }
            });
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
