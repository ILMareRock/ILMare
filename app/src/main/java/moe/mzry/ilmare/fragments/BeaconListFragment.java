package moe.mzry.ilmare.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import moe.mzry.ilmare.MainApp;
import moe.mzry.ilmare.R;
import moe.mzry.ilmare.service.Callback;
import moe.mzry.ilmare.service.IlMareDataProvider;
import moe.mzry.ilmare.service.data.eddystone.Beacon;


/**
 * Message list fragment.
 */
public class BeaconListFragment extends Fragment implements ServiceConnection {

    private final BeaconListAdapter beaconListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView beaconListView;

    public static BeaconListFragment newInstance() {
        BeaconListFragment fragment = new BeaconListFragment();
        return fragment;
    }

    public BeaconListFragment() {
        // Required empty public constructor
        beaconListAdapter = new BeaconListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("BeaconListFragment", "onCreateView, id = " + this.hashCode());
        View view = inflater.inflate(R.layout.fragment_beacon_list, container, false);
        beaconListView = (RecyclerView) view.findViewById(R.id.beaconListView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        beaconListView.setLayoutManager(mLayoutManager);

        beaconListView.setAdapter(beaconListAdapter);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.i("BeaconListFragment", "onServiceConnected");
        MainApp.getLocationProvider().addBeaconListener(new Callback<List<Beacon>>() {
            @Override
            public void apply(List<Beacon> data) {
                Log.i("BeaconListFragment", "Update beacon list, adapter = " + beaconListAdapter);
                if (beaconListAdapter != null) {
                    beaconListAdapter.apply(data);
                }
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
