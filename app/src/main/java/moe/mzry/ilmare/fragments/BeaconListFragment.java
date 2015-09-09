package moe.mzry.ilmare.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moe.mzry.ilmare.R;
import moe.mzry.ilmare.service.IlMareDataProvider;


/**
 * Message list fragment.
 */
public class BeaconListFragment extends Fragment {

    private BeaconListAdapter beaconListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView beaconListView;

    public static BeaconListFragment newInstance() {
        BeaconListFragment fragment = new BeaconListFragment();
        return fragment;
    }

    public BeaconListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beacon_list, container, false);
        beaconListView = (RecyclerView) view.findViewById(R.id.beaconListView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        beaconListView.setLayoutManager(mLayoutManager);

        beaconListAdapter = new BeaconListAdapter();
        beaconListView.setAdapter(beaconListAdapter);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
